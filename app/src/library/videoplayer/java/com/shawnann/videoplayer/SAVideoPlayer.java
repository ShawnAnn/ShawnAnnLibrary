/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.shawn.shawnannlibrary.R;
import com.shawnann.basic.util.FileUtils;
import com.shawnann.basic.util.LogUtil;
import com.shawnann.basic.util.NetStateUtil;
import com.shawnann.videoplayer.util.CommonUtil;
import com.shawnann.videoplayer.video.SABaseVideoPlayer;


import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.shawnann.videoplayer.util.CommonUtil.getTextSpeed;


public abstract class SAVideoPlayer extends SABaseVideoPlayer implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, TextureView.SurfaceTextureListener {

    public static final int CURRENT_STATE_NORMAL = 0; //正常
    public static final int CURRENT_STATE_PREPAREING = 1; //准备中
    public static final int CURRENT_STATE_PLAYING = 2; //播放中
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3; //开始缓冲
    public static final int CURRENT_STATE_PAUSE = 5; //暂停
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6; //自动播放结束
    public static final int CURRENT_STATE_ERROR = 7; //错误状态

    public static final int FULL_SCREEN_NORMAL_DELAY = 2000;

    protected static int BACKUP_PLAYING_BUFFERING_STATE = -1;

    protected static boolean IF_FULLSCREEN_FROM_NORMAL = false;

    public static boolean IF_RELEASE_WHEN_ON_PAUSE = true;

    public static boolean WIFI_TIP_DIALOG_SHOWED = false;


    protected static Timer UPDATE_PROGRESS_TIMER;


    protected View mStartButton;
    protected SeekBar mProgressBar;
    protected ImageView mFullscreenButton;
    protected TextView mCurrentTimeTextView, mTotalTimeTextView;
    protected ViewGroup mTopContainer, mBottomContainer;
    protected SATextureView mTextureView;
    protected Surface mSurface;
    protected ImageView mBackButton;

    protected ProgressTimerTask mProgressTimerTask;

    protected AudioManager mAudioManager; //音频焦点的监听

    protected Handler mHandler = new Handler();

    protected String mPlayTag = ""; //播放的tag，防止错误，因为普通的url也可能重复

    protected int mPlayPosition = -22; //播放的tag，防止错误，因为普通的url也可能重复

    protected float mDownX;//触摸的X

    protected float mDownY; //触摸的Y

    protected float mBrightnessData = -1; //亮度

    protected int mDownPosition; //手指放下的位置

    protected int mGestureDownVolume; //手势调节音量的大小

    protected int mScreenWidth; //屏幕宽度

    protected int mScreenHeight; //屏幕高度

    protected int mThreshold = 80; //手势偏差值

    protected int mSeekToInAdvance = -1; //// TODO: 2016/11/13 跳过广告


    protected int mRotate = 0; //针对某些视频的旋转信息做了旋转处理

    protected int mSeekTimePosition; //手动改变滑动的位置

    protected long mPauseTime; //保存暂停时的时间

    protected long mCurrentPosition; //当前的播放位置

    protected boolean mTouchingProgressBar = false;

    protected boolean mIsTouchWiget = false;

    protected boolean mChangeVolume = false;//是否改变音量

    protected boolean mChangePosition = false;//是否改变播放进度

    protected boolean mBrightness = false;//是否改变亮度

    protected boolean mFirstTouch = false;//是否首次触摸

    protected boolean mCacheFile = false; //是否是缓存的文件


    /**
     * 当前UI
     */
    public abstract int getLayoutId();

    /**
     * 开始播放
     */
    public abstract void startPlayLogic();

    public SAVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public SAVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        View.inflate(context, getLayoutId(), this);
        mStartButton = findViewById(R.id.start);
        mSmallClose = findViewById(R.id.small_close);
        mBackButton = (ImageView) findViewById(R.id.back);
        mFullscreenButton = (ImageView) findViewById(R.id.fullscreen);
        mProgressBar = (SeekBar) findViewById(R.id.progress);
        mCurrentTimeTextView = (TextView) findViewById(R.id.current);
        mTotalTimeTextView = (TextView) findViewById(R.id.total);
        mBottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        mTextureViewContainer = (RelativeLayout) findViewById(R.id.surface_container);
        mTopContainer = (ViewGroup) findViewById(R.id.layout_top);
        if (isInEditMode())
            return;
        mStartButton.setOnClickListener(this);
        mFullscreenButton.setOnClickListener(this);
        mProgressBar.setOnSeekBarChangeListener(this);
        mBottomContainer.setOnClickListener(this);
        mTextureViewContainer.setOnClickListener(this);
        mProgressBar.setOnTouchListener(this);

        mTextureViewContainer.setOnTouchListener(this);
        mFullscreenButton.setOnTouchListener(this);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param objects
     * @return
     */
    public boolean setUp(String url, boolean cacheWithPlay, Object... objects) {
        return setUp(url, cacheWithPlay, ((File) null), objects);
    }


    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径
     * @param mapHeadData
     * @param objects
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, Object... objects) {
        if (setUp(url, cacheWithPlay, cachePath, objects)) {
            this.mMapHeadData.clear();
            if (mapHeadData != null)
                this.mMapHeadData.putAll(mapHeadData);
            return true;
        }
        return false;
    }

    /**
     * 设置播放URL
     *
     * @param url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径
     * @param objects
     * @return
     */
    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object... objects) {
        mCache = cacheWithPlay;
        mCachePath = cachePath;
        if (isCurrentMediaListener() &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
            return false;
        mCurrentState = CURRENT_STATE_NORMAL;
        if (cacheWithPlay && url.startsWith("http") && !url.contains("127.0.0.1")) {
            mOriginUrl = url;
            HttpProxyCacheServer proxy = SAVideoManager.getProxy(getContext().getApplicationContext(), cachePath);
            url = proxy.getProxyUrl(url);
            mCacheFile = (!url.startsWith("http"));
        }
        this.mUrl = url;
        this.mObjects = objects;
        setStateAndUi(CURRENT_STATE_NORMAL);
        return true;
    }

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    protected void setStateAndUi(int state) {
        mCurrentState = state;
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                if (isCurrentMediaListener()) {
                    cancelProgressTimer();
                    SAVideoManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_PREPAREING:
                resetProgressAndTime();
                break;
            case CURRENT_STATE_PLAYING:
                startProgressTimer();
                break;
            case CURRENT_STATE_PAUSE:
                startProgressTimer();
                break;
            case CURRENT_STATE_ERROR:
                if (isCurrentMediaListener()) {
                    SAVideoManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                cancelProgressTimer();
                mProgressBar.setProgress(100);
                mCurrentTimeTextView.setText(mTotalTimeTextView.getText());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            if (TextUtils.isEmpty(mUrl)) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR) {
                if (!mUrl.startsWith("file") && !NetStateUtil.hasWifi() && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startButtonLogic();
            } else if (mCurrentState == CURRENT_STATE_PLAYING) {
                SAVideoManager.instance().getMediaPlayer().pause();
                setStateAndUi(CURRENT_STATE_PAUSE);
                if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                    if (mIfCurrentIsFullscreen) {
                        LogUtil.i("点击全屏");
                        mVideoAllCallBack.onClickStopFullscreen(mUrl, mObjects);
                    } else {
                        LogUtil.i("点击停止");
                        mVideoAllCallBack.onClickStop(mUrl, mObjects);
                    }
                }
            } else if (mCurrentState == CURRENT_STATE_PAUSE) {
                if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                    if (mIfCurrentIsFullscreen) {
                        LogUtil.i("点击恢复全屏");
                        mVideoAllCallBack.onClickResumeFullscreen(mUrl, mObjects);
                    } else {
                        LogUtil.i("点击恢复");
                        mVideoAllCallBack.onClickResume(mUrl, mObjects);
                    }
                }
                SAVideoManager.instance().getMediaPlayer().start();
                setStateAndUi(CURRENT_STATE_PLAYING);
            } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                startButtonLogic();
            }
        } else if (i == R.id.surface_container && mCurrentState == CURRENT_STATE_ERROR) {
            if (mVideoAllCallBack != null) {
                LogUtil.i("错误情况下点击开始");
                mVideoAllCallBack.onClickStartError(mUrl, mObjects);
            }
            prepareVideo();
        }
    }

    protected void showWifiDialog() {
    }

    /**
     * 播放按键的逻辑
     */
    private void startButtonLogic() {
        if (mVideoAllCallBack != null && mCurrentState == CURRENT_STATE_NORMAL) {
            LogUtil.i("点击开始开关");
            mVideoAllCallBack.onClickStartIcon(mUrl, mObjects);
        } else if (mVideoAllCallBack != null) {
            LogUtil.i("点击错误的开始");
            mVideoAllCallBack.onClickStartError(mUrl, mObjects);
        }
        prepareVideo();
    }

    /**
     * 开始状态视频播放
     */
    protected void prepareVideo() {
        if (SAVideoManager.instance().listener() != null) {
            SAVideoManager.instance().listener().onCompletion();
        }
        SAVideoManager.instance().setListener(this);
        SAVideoManager.instance().setPlayTag(mPlayTag);
        SAVideoManager.instance().setPlayPosition(mPlayPosition);
        addTextureView();
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SAVideoManager.instance().prepare(mUrl, mMapHeadData, mLooping, mSpeed);
        setStateAndUi(CURRENT_STATE_PREPAREING);
    }

    /**
     * 监听是否有外部其他多媒体开始播放
     */
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            releaseAllVideos();
                        }
                    });
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (SAVideoManager.instance().getMediaPlayer().isPlaying()) {
                        SAVideoManager.instance().getMediaPlayer().pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };


    /**
     * 重置
     */
    public void onVideoReset() {
        setStateAndUi(CURRENT_STATE_NORMAL);
    }

    /**
     * 暂停状态
     */
    public void onVideoPause() {
        if (SAVideoManager.instance().getMediaPlayer().isPlaying()) {
            setStateAndUi(CURRENT_STATE_PAUSE);
            mPauseTime = System.currentTimeMillis();
            mCurrentPosition = SAVideoManager.instance().getMediaPlayer().getCurrentPosition();
            if (SAVideoManager.instance().getMediaPlayer() != null)
                SAVideoManager.instance().getMediaPlayer().pause();
        }
    }

    /**
     * 恢复暂停状态
     */
    public void onVideoResume() {
        mPauseTime = 0;
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            if (mCurrentPosition > 0 && SAVideoManager.instance().getMediaPlayer() != null) {
                setStateAndUi(CURRENT_STATE_PLAYING);
                SAVideoManager.instance().getMediaPlayer().seekTo(mCurrentPosition);
                SAVideoManager.instance().getMediaPlayer().start();
            }
        }
    }

    /**
     * 添加播放的view
     */
    protected void addTextureView() {
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }
        mTextureView = null;
        mTextureView = new SATextureView(getContext());
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setRotation(mRotate);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTextureViewContainer.addView(mTextureView, layoutParams);
    }

    /**
     * 小窗口
     **/
    @Override
    protected void setSmallVideoTextureView(View.OnTouchListener onTouchListener) {
        mTextureView.setOnTouchListener(onTouchListener);
        mProgressBar.setOnTouchListener(null);
        mFullscreenButton.setOnTouchListener(null);
        mTextureView.setOnClickListener(null);
        mSmallClose.setVisibility(VISIBLE);
        mSmallClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSmallVideo();
                releaseAllVideos();
            }
        });
    }

    /**
     * 设置界面选择
     */
    public void setRotationView(int rotate) {
        this.mRotate = rotate;
        mTextureView.setRotation(rotate);
    }

    public void refreshVideo() {
        if (mTextureView != null) {
            mTextureView.requestLayout();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        SAVideoManager.instance().setDisplay(mSurface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        SAVideoManager.instance().setDisplay(null);
        surface.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**
     * 亮度、进度、音频
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int id = v.getId();
        if (id == R.id.fullscreen) {
            return false;
        }
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchingProgressBar = true;
                    mDownX = x;
                    mDownY = y;
                    mChangeVolume = false;
                    mChangePosition = false;
                    mBrightness = false;
                    mFirstTouch = true;

                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);

                    if (mIfCurrentIsFullscreen || mIsTouchWiget) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            if (absDeltaX > mThreshold || absDeltaY > mThreshold) {
                                cancelProgressTimer();
                                if (absDeltaX >= mThreshold) {
                                    mChangePosition = true;
                                    mDownPosition = getCurrentPositionWhenPlaying();
                                } else {
                                    if (mFirstTouch) {
                                        mBrightness = mDownX < mScreenWidth * 0.5f;
                                        mFirstTouch = false;
                                    }
                                    if (!mBrightness) {
                                        mChangeVolume = true;
                                        mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    }
                                }
                            }
                        }
                    }
                    if (mChangePosition) {
                        int totalTimeDuration = getDuration();
                        mSeekTimePosition = (int) (mDownPosition + deltaX * totalTimeDuration / mScreenWidth);
                        if (mSeekTimePosition > totalTimeDuration)
                            mSeekTimePosition = totalTimeDuration;
                        String seekTime = CommonUtil.string4Time(mSeekTimePosition);
                        String totalTime = CommonUtil.string4Time(totalTimeDuration);
                        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
                    } else if (mChangeVolume) {
                        deltaY = -deltaY;
                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);

                        showVolumeDialog(-deltaY, volumePercent);
                    } else if (!mChangePosition && mBrightness) {
                        float percent = -deltaY / mScreenHeight;
                        onBrightnessSlide(percent);
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    mTouchingProgressBar = false;
                    dismissProgressDialog();
                    dismissVolumeDialog();
                    dismissBrightnessDialog();
                    if (mChangePosition) {
                        SAVideoManager.instance().getMediaPlayer().seekTo(mSeekTimePosition);
                        int duration = getDuration();
                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
                        mProgressBar.setProgress(progress);
                        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                            LogUtil.i("触摸位置改变");
                            mVideoAllCallBack.onTouchScreenSeekPosition(mUrl, mObjects);
                        }
                    } else if (mBrightness) {
                        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                            LogUtil.i("触摸亮度改变");
                            mVideoAllCallBack.onTouchScreenSeekLight(mUrl, mObjects);
                        }
                    } else if (mChangeVolume) {
                        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                            LogUtil.i("触摸声音改变");
                            mVideoAllCallBack.onTouchScreenSeekVolume(mUrl, mObjects);
                        }
                    }
                    startProgressTimer();
                    break;
            }
        } else if (id == R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    cancelProgressTimer();
                    ViewParent vpdown = getParent();
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true);
                        vpdown = vpdown.getParent();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    startProgressTimer();
                    ViewParent vpup = getParent();
                    while (vpup != null) {
                        vpup.requestDisallowInterceptTouchEvent(false);
                        vpup = vpup.getParent();
                    }
                    mBrightnessData = -1f;
                    break;
            }
        }

        return false;
    }


    protected void showProgressDialog(float deltaX,
                                      String seekTime, int seekTimePosition,
                                      String totalTime, int totalTimeDuration) {
    }

    protected void dismissProgressDialog() {

    }

    protected void showVolumeDialog(float deltaY, int volumePercent) {

    }

    protected void dismissVolumeDialog() {

    }

    protected void showBrightnessDialog(float percent) {

    }

    protected void dismissBrightnessDialog() {

    }

    protected void onClickUiToggle() {

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /***
     * 拖动进度条
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            if (isIfCurrentIsFullscreen()) {
                LogUtil.i("onClickSeekbarFullscreen");
                mVideoAllCallBack.onClickSeekbarFullscreen(mUrl, mObjects);
            } else {
                LogUtil.i("onClickSeekbar");
                mVideoAllCallBack.onClickSeekbar(mUrl, mObjects);
            }
        }
        if (SAVideoManager.instance().getMediaPlayer() != null && SAVideoManager.instance().getMediaPlayer().isPlaying()) {
            int time = seekBar.getProgress() * getDuration() / 100;
            SAVideoManager.instance().getMediaPlayer().seekTo(time);
        }
    }

    @Override
    public void onPrepared() {
        if (mCurrentState != CURRENT_STATE_PREPAREING) return;
        SAVideoManager.instance().getMediaPlayer().start();
        if (mSeekToInAdvance != -1) {
            SAVideoManager.instance().getMediaPlayer().seekTo(mSeekToInAdvance);
            mSeekToInAdvance = -1;
        }
        startProgressTimer();
        setStateAndUi(CURRENT_STATE_PLAYING);
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            LogUtil.i("onPrepared");
            mVideoAllCallBack.onPrepared(mUrl, mObjects);
        }
        mHadPlay = true;
    }

    @Override
    public void onAutoCompletion() {
        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            LogUtil.i("onAutoComplete");
            mVideoAllCallBack.onAutoComplete(mUrl, mObjects);
        }
        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
            IF_FULLSCREEN_FROM_NORMAL = false;
            SAVideoManager.instance().lastListener().onAutoCompletion();
        }
        SAVideoManager.instance().setLastListener(null);
        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onCompletion() {
        //make me normal first
        setStateAndUi(CURRENT_STATE_NORMAL);
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
            IF_FULLSCREEN_FROM_NORMAL = false;
            SAVideoManager.instance().lastListener().onCompletion();//回到上面的onAutoCompletion
        }
        SAVideoManager.instance().setListener(null);
        SAVideoManager.instance().setLastListener(null);
        SAVideoManager.instance().setCurrentVideoHeight(0);
        SAVideoManager.instance().setCurrentVideoWidth(0);

        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mCurrentState != CURRENT_STATE_NORMAL && mCurrentState != CURRENT_STATE_PREPAREING) {
            setTextAndProgress(percent);
            //循环清除进度
            if (mLooping && percent == 0 && mProgressBar.getProgress() > 3) {
                loopSetProgressAndTime();
            }
            LogUtil.i("Net speed: " + getNetSpeedText());
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {
        if (what != 38 && what != -38) {
            setStateAndUi(CURRENT_STATE_ERROR);
            if (mCacheFile) {
                LogUtil.i(" mCacheFile Local Error " + mUrl);
                //可能是因为缓存文件除了问题
                FileUtils.deleteFile(mUrl.replace("file://", ""));
                mUrl = mOriginUrl;
            } else if (mUrl.contains("127.0.0.1")) {
                LogUtil.i(" mCacheFile Download Error " + mUrl);
                FileUtils.deleteFile(mUrl.replace("file://", "") + ".downlad");
                mUrl = mOriginUrl;
            }
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            BACKUP_PLAYING_BUFFERING_STATE = mCurrentState;
            if (mLooping && mHadPlay) {
                //循环在播放的不显示
            } else {
                setStateAndUi(CURRENT_STATE_PLAYING_BUFFERING_START);
            }
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (BACKUP_PLAYING_BUFFERING_STATE != -1) {
                if (mLooping && mHadPlay) {
                    //循环在播放的不显示
                } else {
                    setStateAndUi(BACKUP_PLAYING_BUFFERING_STATE);
                }
                BACKUP_PLAYING_BUFFERING_STATE = -1;
            }
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            mRotate = extra;
            if (mTextureView != null)
                mTextureView.setRotation(mRotate);
        }
    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = SAVideoManager.instance().getCurrentVideoWidth();
        int mVideoHeight = SAVideoManager.instance().getCurrentVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            mTextureView.requestLayout();
        }
    }

    @Override
    public void onBackFullscreen() {

    }

    protected void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }

    }

    protected class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextAndProgress(0);
                    }
                });
            }
        }
    }

    /**
     * 获取当前播放进度
     */
    public int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
            try {
                position = (int) SAVideoManager.instance().getMediaPlayer().getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    /**
     * 获取当前总时长
     */
    public int getDuration() {
        int duration = 0;
        try {
            duration = (int) SAVideoManager.instance().getMediaPlayer().getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    protected void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!mTouchingProgressBar) {
            if (progress != 0) mProgressBar.setProgress(progress);
        }
        if (secProgress > 95) secProgress = 100;
        if (secProgress != 0) mProgressBar.setSecondaryProgress(secProgress);
        mCurrentTimeTextView.setText(CommonUtil.string4Time(currentTime));
        mTotalTimeTextView.setText(CommonUtil.string4Time(totalTime));
    }


    protected void resetProgressAndTime() {
        mProgressBar.setProgress(0);
        mProgressBar.setSecondaryProgress(0);
        mCurrentTimeTextView.setText(CommonUtil.string4Time(0));
        mTotalTimeTextView.setText(CommonUtil.string4Time(0));
    }


    protected void loopSetProgressAndTime() {
        mProgressBar.setProgress(0);
        mProgressBar.setSecondaryProgress(0);
        mCurrentTimeTextView.setText(CommonUtil.string4Time(0));
    }

    /**
     * 页面销毁了记得调用是否所有的video
     */
    public static void releaseAllVideos() {
        if (IF_RELEASE_WHEN_ON_PAUSE) {
            if (SAVideoManager.instance().listener() != null) {
                SAVideoManager.instance().listener().onCompletion();
            }
            SAVideoManager.instance().releaseMediaPlayer();
        } else {
            IF_RELEASE_WHEN_ON_PAUSE = true;
        }
    }

    /**
     * if I am playing release me
     */
    public void release() {
        if (isCurrentMediaListener() &&
                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            releaseAllVideos();
        }
        mHadPlay = false;
    }


    protected boolean isCurrentMediaListener() {
        return SAVideoManager.instance().listener() != null
                && SAVideoManager.instance().listener() == this;
    }


    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightnessData < 0) {
            mBrightnessData = ((Activity) (mContext)).getWindow().getAttributes().screenBrightness;
            if (mBrightnessData <= 0.00f) {
                mBrightnessData = 0.50f;
            } else if (mBrightnessData < 0.01f) {
                mBrightnessData = 0.01f;
            }
        }
        WindowManager.LayoutParams lpa = ((Activity) (mContext)).getWindow().getAttributes();
        lpa.screenBrightness = mBrightnessData + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        showBrightnessDialog(lpa.screenBrightness);
        ((Activity) (mContext)).getWindow().setAttributes(lpa);
    }

    public boolean isTouchWiget() {
        return mIsTouchWiget;
    }

    /**
     * 是否可以滑动界面改变进度，声音等
     */
    public void setIsTouchWiget(boolean isTouchWiget) {
        this.mIsTouchWiget = isTouchWiget;
    }

    /**
     * 获取播放按键
     */
    public View getStartButton() {
        return mStartButton;
    }

    /**
     * 获取全屏按键
     */
    public ImageView getFullscreenButton() {
        return mFullscreenButton;
    }

    /**
     * 获取返回按键
     */
    public ImageView getBackButton() {
        return mBackButton;
    }

    /**
     * 获取当前播放状态
     */
    public int getCurrentState() {
        return mCurrentState;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     */
    public String getPlayTag() {
        return mPlayTag;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     *
     * @param playTag 保证不重复就好
     */
    public void setPlayTag(String playTag) {
        this.mPlayTag = playTag;
    }


    public int getPlayPosition() {
        return mPlayPosition;
    }

    /**
     * 设置播放位置防止错位
     */
    public void setPlayPosition(int playPosition) {
        this.mPlayPosition = playPosition;
    }

    /**
     * 显示小窗口的关闭按键
     */
    public void setSmallCloseShow() {
        mSmallClose.setVisibility(VISIBLE);
    }

    /**
     * 隐藏小窗口的关闭按键
     */
    public void setSmallCloseHide() {
        mSmallClose.setVisibility(GONE);
    }

    /**
     * 退出全屏，主要用于返回键
     *
     * @return 返回是否全屏
     */
    public static boolean backFromWindowFull(Context context) {
        boolean backFrom = false;
        ViewGroup vp = (ViewGroup) (CommonUtil.scanForActivity(context)).findViewById(Window.ID_ANDROID_CONTENT);
        View oldF = vp.findViewById(FULLSCREEN_ID);
        if (oldF != null) {
            backFrom = true;
            SAVideoManager.instance().lastListener().onBackFullscreen();
        }
        return backFrom;
    }

    /**
     * 网络速度
     * 注意，这里如果是开启了缓存，因为读取本地代理，缓存成功后还是存在速度的
     * 再打开已经缓存的本地文件，网络速度才会回0.因为是播放本地文件了
     */
    public long getNetSpeed() {
        return SAVideoManager.instance().getMediaPlayer().getTcpSpeed();

    }

    /**
     * 网络速度
     * 注意，这里如果是开启了缓存，因为读取本地代理，缓存成功后还是存在速度的
     * 再打开已经缓存的本地文件，网络速度才会回0.因为是播放本地文件了
     */
    public String getNetSpeedText() {
        long speed = SAVideoManager.instance().getMediaPlayer().getTcpSpeed();
        return getTextSpeed(speed);
    }

}