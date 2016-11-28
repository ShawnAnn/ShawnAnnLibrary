/*
 * Created by ShawnAnn on 16-11-25 下午1:22
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 类似中华万年历主页体验布局
 * 使用注意事项：
 * 1、必须包含三个自布局，依次是主视图、顶部视图和底部视图
 * 2、每个视图默认必须是clickable的（用于消费事件）
 * 3、没有对主视图做动画，可以自己添加{@link #moveCenterView(float)}
 * 4、给主视图添加动画后需要在{@link #move2Bottom()}和{@link #move2Top()}中添加相应的复位处理
 *
 * @author ShawnAnn
 * @since 2016/11/24
 */

public class SlideBox extends FrameLayout {

    Context context;
    View centerView;
    View topView;
    View bottomView;
    int minTop;
    int maxTop;

    int centerHeight;// 中间视图高度
    int centerWidth;// 中间视图宽度
    int bottomHeight;//底部视图高度
    int bottomWidth;//底部视图宽度
    int topHeight;//顶部视图高度
    int topWidth;//顶部视图宽度

    int totalMoveLength;

    private float downX;//按下时的x坐标
    private float downY;//按下时的y坐标
    private float lastY;
    private ValueAnimator mAnimator = null;
    private static final int ANIM_PAGE_DURATION = 300;

    private int type_HV = -1;//拖动类型（水平/垂直）
    private static final int TYPE_HOR = 1;
    private static final int TYPE_V = 2;

    private int type_TB = -1;//拖动类型（上/下）
    private static final int DRAG_TYPE_TOP = 1;//手指向上滑
    private static final int DRAG_TYPE_BOTTOM = 2;//手指向下滑

    // 默认展示顶部布局
    private boolean showTopView = false;


    public SlideBox(Context context) {
        this(context, null);
    }

    public SlideBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        centerView = getChildAt(0);
        topView = getChildAt(1);
        bottomView = getChildAt(2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setChildFirstLocation();
    }

    /**
     * 该方法是View的放置方法，在View类实现。
     * 调用该方法需要传入放置View的矩形空间左上角left、top值和右下角right、bottom值。
     * 这四个值是相对于父控件而言的。例如传入的是（10, 10, 100, 100），
     * 则该View在距离父控件的左上角位置(10, 10)处显示，显示的大小是宽高是90(参数r,b是相对左上角的)，
     */
    public void setChildFirstLocation() {
        totalMoveLength = centerView.getMeasuredHeight() - topView.getMeasuredHeight();
        topHeight = minTop = topView.getMeasuredHeight();
        topWidth = topView.getMeasuredWidth();
        maxTop = minTop + totalMoveLength;
        centerHeight = centerView.getMeasuredHeight();
        centerWidth = centerView.getMeasuredWidth();
        bottomHeight = bottomView.getMeasuredHeight();
        bottomWidth = bottomView.getMeasuredWidth();

        if (showTopView) {
            int height = topHeight;
            topView.layout(0, 0, topWidth, height);
            centerView.setVisibility(VISIBLE);
            bottomView.layout(0, height, bottomWidth, height + bottomHeight);
        } else {
            int height = topHeight;
            topView.layout(0, -height, topWidth, 0);
            centerView.setVisibility(VISIBLE);
            bottomView.layout(0, centerHeight, bottomWidth, centerHeight + bottomHeight);

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mAnimator != null && mAnimator.isRunning()) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                lastY = ev.getY();
                type_HV = -1;
                type_TB = -1;
                break;
            case MotionEvent.ACTION_MOVE://确定type_HV（水平/垂直）
                float x = ev.getX();
                float y = ev.getY();
                // 判断滑动的方向
                if (type_HV == -1) {
                    if (Math.abs(x - downX) > 0 && Math.abs(x - downX) > Math.abs(y - downY)) {
                        type_HV = TYPE_HOR;
                        requestDisallowInterceptTouchEvent(true);
                    }
                    if (Math.abs(y - downY) > 0 && Math.abs(y - downY) > Math.abs(x - downX)) {
                        type_HV = TYPE_V;
                        requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return type_HV != -1;

    }

    private float move_plus = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        move_plus = 0;
        if (mAnimator != null && mAnimator.isRunning()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //随手指滑动而滑动
                float moveY = y - lastY + move_plus;
                lastY = y;
                //确定type_TB（上/下）
                type_TB = (moveY) > 0 ? DRAG_TYPE_BOTTOM : DRAG_TYPE_TOP;
                // 取整的小数部分
                move_plus = moveY - (int) moveY;
                moveViews(moveY);
                break;
            case MotionEvent.ACTION_UP:
                if (type_TB == DRAG_TYPE_BOTTOM) {
                    move2Bottom();
                } else if (type_TB == DRAG_TYPE_TOP) {
                    move2Top();
                }
                break;
        }
        return type_HV != -1;
    }


    // 提示下滑界面标志
    boolean flag = false;


    private void moveViews(float moveY) {
        moveBottomView(moveY);
        moveTopView(-moveY);
        // moveCenterView(moveY);
    }

    //bottomView跟随手指的滑动而滑动
    private void moveBottomView(float moveY) {
        float newTop = bottomView.getTop() + moveY + bottomPlus;
        if (newTop < minTop) newTop = minTop;
        if (newTop > maxTop) newTop = maxTop;
        int newTop1 = (int) newTop;

        bottomPlus = newTop - newTop1;

        bottomView.layout(0, newTop1, bottomWidth, newTop1 + bottomHeight);
        //centerView.layout(0, -centerHeight + newTop1, centerWidth, newTop1);
        if (!flag) {
            centerView.setVisibility(VISIBLE);
            flag = true;
        }
    }

    private float topPlus = 0;
    private float bottomPlus = 0;

    //跟随手指的滑动而滑动
    private void moveTopView(float moveY) {
        float index = 1f * (centerHeight - topHeight) / topHeight;
        if (moveY / index == 0 && moveY != 0) {
            moveY = 1f * moveY / Math.abs(moveY);
        } else {
            moveY /= index;
        }
        int newTop = (int) (topView.getTop() + moveY + topPlus);
        topPlus = topView.getTop() + moveY + topPlus - newTop;
        if (newTop < -topHeight) newTop = -topHeight;
        if (newTop > 0) newTop = 0;
        topView.layout(0, newTop, topWidth, newTop + topHeight);
    }

    //跟随手指的滑动而缩放
    //float minScale = 0.7f;

    private void moveCenterView(float moveY) {
        float newTop = bottomView.getTop() + moveY;
        if (newTop < minTop) newTop = minTop;
        if (newTop > maxTop) newTop = maxTop;
        int newTop1 = (int) newTop;
        centerView.layout(0, -centerHeight + newTop1, centerWidth, newTop1);
        //centerView.invalidate();
    }

    //bottomView自动滚动到最上方
    public void move2Top() {
        flag = false; // 恢复滑动标志
        showTopView = true;
        final int length = bottomView.getTop() - minTop;
        final int length_topView = Math.abs(topView.getTop());

        mAnimator = ValueAnimator.ofFloat(1f, 0f);
        mAnimator.setDuration(ANIM_PAGE_DURATION);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                int extra = (int) (length * value);
                bottomView.layout(0, minTop + extra, bottomWidth, minTop + bottomHeight + extra);
                // centerView.layout(0, -centerHeight + minTop + extra, centerWidth, minTop + extra);
                int extra_top = (int) (length_topView * value);
                topView.layout(0, 0 - extra_top, topWidth, 0 - extra_top + topHeight);
            }
        });
        mAnimator.start();
        centerView.setVisibility(VISIBLE);
    }

    //bottomView自动滚动到最下方
    public void move2Bottom() {

        flag = false; // 恢复滑动标志
        showTopView = false;
        final int length = maxTop - bottomView.getTop();
        final int length_topView = topHeight + topView.getTop();

        mAnimator = ValueAnimator.ofFloat(1f, 0f);
        mAnimator.setDuration(ANIM_PAGE_DURATION);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                int extra = (int) (length * value);
                bottomView.layout(0, maxTop - extra, bottomWidth, maxTop + bottomHeight - extra);
                //  centerView.layout(0, -centerHeight + maxTop - extra, centerWidth, maxTop - extra);
                int extra_top = (int) (length_topView * value);
                topView.layout(0, -topHeight + extra_top, topWidth, extra_top);
            }
        });
        mAnimator.start();
        centerView.setVisibility(VISIBLE);
    }
}
