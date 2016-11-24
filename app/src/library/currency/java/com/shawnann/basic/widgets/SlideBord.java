/*
 * Created by ShawnAnn on 16-11-24 上午11:15
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.shawnann.basic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShawnAnn on 2016/11/24.
 */

public class SlideBord extends FrameLayout {

    private int type_HV = -1;//拖动类型（水平/垂直）
    private static final int TYPE_HOR = 1;
    private static final int TYPE_V = 2;

    private int slide_orientation = 0;
    private static final int DRAG_TYPE_LEFT = 1;//手指向左滑
    private static final int DRAG_TYPE_RIGHT = 2;//手指向右滑
    private static final int DRAG_TYPE_TOP = 3;//手指向上滑
    private static final int DRAG_TYPE_BOTTOM = 4;//手指向下滑

    private float downX;//按下时的x坐标
    private float downY;//按下时的y坐标

    private Scroller mScroller = null;

    private Animator mAnimator = null;

    private static final int ANIM_PAGE_DURATION = 300;

    private Context context;

    // 移动时被计算忽略的的小数
    private float move_plus_x = 0;
    private float move_plus_y = 0;

    private List<View> horizontalViews = new ArrayList<>();
    private List<View> verticalViews = new ArrayList<>();


    public SlideBord(Context context) {
        this(context, null);
    }

    public SlideBord(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBord(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mScroller = new Scroller(context);
    }

    // 一次性初始化控件集合
    private boolean initlayout = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!initlayout) {
            horizontalViews.clear();
            horizontalViews.add(getChildAt(0));
            horizontalViews.add(getChildAt(1));
            horizontalViews.add(getChildAt(2));
            verticalViews.clear();
            verticalViews.add(getChildAt(3));
            verticalViews.add(getChildAt(4));
            initlayout = true;
        }
        layoutChild();
    }

    /**
     * 定制layout,要求5个View组成一个十字
     */
    private void layoutChild() {
        horizontalViews.get(0).layout(getWidth() * (-1), 0, getWidth() * (-1) + horizontalViews.get(0).getWidth(), getHeight());
        horizontalViews.get(1).layout(0, 0, 0 + horizontalViews.get(1).getWidth(), getHeight());
        horizontalViews.get(2).layout(getWidth() * (1), 0, getWidth() * (1) + horizontalViews.get(2).getWidth(), getHeight());

        verticalViews.get(0).layout(0, getHeight() * (-1), verticalViews.get(0).getWidth(), 0);
        verticalViews.get(1).layout(0, getHeight(), verticalViews.get(1).getWidth(), getHeight() * 2);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                type_HV = -1;
                LogUtil.i("按下");
                break;
            case MotionEvent.ACTION_MOVE://确定type_HV（水平/垂直）
                float x = ev.getX();
                float y = ev.getY();
                if (type_HV == -1) {
                    if (Math.abs(x - downX) > 0 && Math.abs(x - downX) > Math.abs(y - downY)) {
                        type_HV = TYPE_HOR;
                        requestDisallowInterceptTouchEvent(true);
                    }
                    if (Math.abs(y - downY) > 0 && Math.abs(y - downY) > Math.abs(x - downX)) {
                        type_HV = TYPE_V;
                        requestDisallowInterceptTouchEvent(true);
                    }
                    LogUtil.i("移动方向" + type_HV);
                }
                break;
        }

        return type_HV != -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        move_plus_x = 0;
        move_plus_y = 0;

        if ((mAnimator != null && mAnimator.isRunning())) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                LogUtil.i("获取点击点做标");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i("移动");
                if (type_HV == TYPE_HOR) {
                    //移动的距离
                    int move = (int) (event.getX() - downX + move_plus_x);
                    // 取整的小数部分
                    move_plus_x = event.getX() - downX - move;
                    slide_orientation = move > 0 ? DRAG_TYPE_RIGHT : DRAG_TYPE_LEFT;
                    if (slide_orientation == DRAG_TYPE_LEFT) {
                        move = move > 0 ? 0 : move;
                    } else if (slide_orientation == DRAG_TYPE_RIGHT) {
                        move = move < 0 ? 0 : move;
                    }
                    LogUtil.i("左右移动" + move);
                    //移动
                    scrollTo(-move, 0);
                } else {
                    int move = (int) (event.getY() - downY + move_plus_y);
                    // 取整的小数部分
                    move_plus_y = event.getY() - downY - move;
                    slide_orientation = move > 0 ? DRAG_TYPE_BOTTOM : DRAG_TYPE_TOP;
                    if (slide_orientation == DRAG_TYPE_TOP) {
                        move = move > 0 ? 0 : move;
                    } else if (slide_orientation == DRAG_TYPE_BOTTOM) {
                        move = move < 0 ? 0 : move;
                    }
                    LogUtil.i("上下移动" + move);
                    //移动
                    scrollTo(0, -move);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                LogUtil.i("抬起");
                if (type_HV == TYPE_HOR) {
                    float x1 = event.getX();
                    int movex = (int) (x1 - downX);
                    if (slide_orientation == DRAG_TYPE_LEFT && movex < 0 && Math.abs(movex) > getWidth() / 5) {
                        showRight();
                    } else if (slide_orientation == DRAG_TYPE_RIGHT && movex > getWidth() / 5) {
                        showLeft();
                    } else {
                        smoothscrollTo(true, 0, 300);
                    }
                } else {
                    float y1 = event.getY();
                    int movey = (int) (y1 - downY);
                    if (slide_orientation == DRAG_TYPE_TOP && movey < 0 && Math.abs(movey) > getHeight() / 5) {
                        showTop();
                    } else if (slide_orientation == DRAG_TYPE_BOTTOM && movey > getHeight() / 5) {
                        showBottom();
                    } else {
                        smoothscrollTo(false, 0, 300);
                    }
                }
                slide_orientation = -1;
                break;
        }
        return type_HV != -1;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private void smoothscrollTo(boolean isHor, int x, int duration) {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        if (isHor) {
            int startX = getScrollX();
            int detalX = x - startX;
            mScroller.startScroll(startX, 0, detalX, 0, duration);
        } else {
            int startY = getScrollY();
            int detalY = x - startY;
            mScroller.startScroll(0, startY, 0, detalY, duration);
        }
        invalidate();
    }


    public void showLeft() {

        final int x1 = getScrollX();
        mAnimator = ObjectAnimator.ofInt(this, "scrollX", x1, -getWidth());
        mAnimator.setDuration(ANIM_PAGE_DURATION);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                final View swapView = horizontalViews.remove(2);
                horizontalViews.add(0, swapView);
                layoutChild();
                scrollTo(0, getScrollY());
            }
        });
        mAnimator.start();
    }


    public void showRight() {
        final int x1 = getScrollX();
        mAnimator = ObjectAnimator.ofInt(this, "scrollX", x1, getWidth());
        mAnimator.setDuration(ANIM_PAGE_DURATION);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                final View swapView = horizontalViews.remove(0);
                horizontalViews.add(swapView);
                layoutChild();
                scrollTo(0, getScrollY());
            }
        });
        mAnimator.start();
    }

    public void showTop() {
        final int y1 = getScrollY();
        mAnimator = ObjectAnimator.ofInt(this, "scrollY", y1, getHeight());
        mAnimator.setDuration(ANIM_PAGE_DURATION);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                final View hor_swapView = horizontalViews.remove(1);
                final View ver_swapView = verticalViews.remove(1);
                verticalViews.add(0, hor_swapView);
                horizontalViews.add(1, ver_swapView);
                layoutChild();
                scrollTo(getScrollX(), 0);
            }
        });
        mAnimator.start();
    }

    public void showBottom() {
        final int y1 = getScrollY();
        mAnimator = ObjectAnimator.ofInt(this, "scrollY", y1, -getHeight());
        mAnimator.setDuration(ANIM_PAGE_DURATION);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                final View hor_swapView = horizontalViews.remove(1);
                final View ver_swapView = verticalViews.remove(0);
                verticalViews.add(hor_swapView);
                horizontalViews.add(1, ver_swapView);
                layoutChild();
                scrollTo(getScrollX(), 0);
            }
        });
        mAnimator.start();
    }


}
