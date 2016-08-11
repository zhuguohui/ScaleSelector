package com.zgh.scaleselector.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;


import com.zgh.scaleselector.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhuguohui on 2016/8/9.
 */
public class ScaleSelectorView extends View {

    Paint mLinePaint, mCirclePaint, mTextPaint;
    List<String> mData = new ArrayList<>();
    int offset = 100, width, height;
    int lineHeight = 30;
    int cX = -1, cY, cR = 50;
    private List<Point> linesList;
    private int textOffSet = 140;
    //默认的index
    private int defaultIndex = 0;
    //文字和线段的颜色
    private int mStyleColor;
    //圆的颜色
    private int mCColor;
    //文字大小
    private int mTextSize;
    private TimeInterpolator mInterpolator=null;
    public ScaleSelectorView(Context context) {
        this(context, null);
    }

    public ScaleSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initPaints();


    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScaleSelectorView);
        mCColor = array.getColor(R.styleable.ScaleSelectorView_ScaleSelectorCColor, Color.RED);
        mStyleColor = array.getColor(R.styleable.ScaleSelectorView_ScaleSelectorStyleColor, Color.parseColor("#AAAAAA"));
        mTextSize = array.getDimensionPixelSize(R.styleable.ScaleSelectorView_ScaleSelecorTextSize, 50);
        int dataID = array.getResourceId(R.styleable.ScaleSelectorView_ScaleSelectorData, 0);
        if (dataID != 0) {
            String[] arrayData = context.getResources().getStringArray(dataID);
            mData.clear();
            Collections.addAll(mData, arrayData);
        }
        defaultIndex = array.getInt(R.styleable.ScaleSelectorView_ScaleSelectorDefaultIndex, 0);
        cR=array.getDimensionPixelOffset(R.styleable.ScaleSelectorView_ScaleSelectorCRadius,50);
        int interpolatorID=array.getResourceId(R.styleable.ScaleSelectorView_ScaleSelectorInterpolator,0);
        if(interpolatorID!=0) {
            mInterpolator = AnimationUtils.loadInterpolator(context, interpolatorID);
        }
        array.recycle();
    }

    private void initPaints() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mStyleColor);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mCColor);
        mCirclePaint.setAntiAlias(true);
        //设置阴影
        if (!isInEditMode()) {
            //关闭硬件加速，圆形的阴影才会出现
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            int color = mCirclePaint.getColor();
            int newColor = Color.argb(50, Color.red(color), Color.green(color), Color.blue(color));
            mCirclePaint.setShadowLayer(5, 0, 10, newColor);

        }
        mTextPaint = new Paint();
        mTextPaint.setColor(mStyleColor);
        mTextPaint.setTextSize(mTextSize);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //计算文字位置
        width = getWidth() - getPaddingLeft() - getPaddingRight() - cR;
        height = getHeight() - getPaddingTop() - getPaddingBottom();
        //计算两个刻度之间的间隔
        offset = width / (mData.size() - 1);
        linesList = new ArrayList<>();
        int l = getPaddingLeft() + cR / 2;
        for (int i = 0; i < mData.size(); i++) {
            Point point = new Point(l + i * offset, getHeight() / 2 + 5);
            linesList.add(point);
        }
        if (cX == -1) {
            if (linesList.size() > 0) {
                cX = linesList.get(defaultIndex).x;
            } else {
                cX = getWidth() / 2;
            }
        }
        cY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLines(canvas);
        drawTexts(canvas);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(cX, cY, cR, mCirclePaint);
    }

    private void drawTexts(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            int x;
            Point point = linesList.get(i);
            Rect mBound = new Rect();
            mTextPaint.getTextBounds(mData.get(i), 0, mData.get(i).length(), mBound);
            int width = mBound.width();

            if (i == 0) {
                x = point.x;
            } else if (i == (mData.size() - 1)) {
                x = point.x - width;
            } else {
                x = point.x - width / 2;
            }
            canvas.drawText(mData.get(i), x, point.y - textOffSet, mTextPaint);
        }
    }

    public void setData(List<String> data, int defaultIndex) {
        if (data == null) {
            throw new RuntimeException("Data 不能为空");
        }
        if (defaultIndex < 0 || defaultIndex >= data.size()) {
            throw new RuntimeException("defaultIndex 无效");
        }
        this.mData = data;
        this.defaultIndex = defaultIndex;
        requestLayout();
    }

    private void drawLines(Canvas canvas) {
        //画横线
        canvas.drawLine(getPaddingLeft() + cR / 2, getHeight() / 2 + 5, getWidth() - getPaddingRight() - cR / 2, getHeight() / 2 + 5, mLinePaint);
        for (int i = 0; i < linesList.size(); i++) {
            Point point = linesList.get(i);
            int height = lineHeight;
            if (i == 0 || i == linesList.size() - 1) {
                height += 20;
            }
            canvas.drawLine(point.x, point.y, point.x, point.y - height, mLinePaint);
        }
    }

    int dx, dy;
    boolean isPressOnCenter = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = (int) event.getX();
                dy = (int) event.getY();
                isPressOnCenter = calculateIsInCenter(dx, dy);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPressOnCenter) {
                    int mx = (int) (event.getX() - dx);
                    dx = (int) event.getX();
                    cX += mx;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isPressOnCenter) {
                    animatToCloserPoint(cX);
                } else {
                    //如果是点击则计算最近的点
                    boolean isClick = Math.abs(event.getX() - dx) < 10 && Math.abs(event.getY() - dy) < 10;
                    if (isClick) {
                        animatToCloserPoint((int) event.getX());
                    }
                }
                break;
        }
        return true;
    }


    private void animatToCloserPoint(int x) {
        //计算最近的点
        final int index = (int) ((x - getPaddingLeft() + 0.5 * offset) / offset);
        if (index >= linesList.size()) {
            return;
        }
        int moveX = linesList.get(index).x;
        ValueAnimator animator = ValueAnimator.ofInt(cX, moveX);
        if(mInterpolator!=null) {
            animator.setInterpolator(mInterpolator);
        }
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int mx = (int) animation.getAnimatedValue();
                cX = mx;
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnSelectChangeListener != null) {
                    mOnSelectChangeListener.onSelect(index,mData.get(index));
                }
            }
        });
        animator.start();
    }

    private boolean calculateIsInCenter(int dx, int dy) {
        return (dx - cX) * (dx - cX) + (dy - cY) * (dy - cY) <= (cR * cR) / 1.5;
    }

    public static interface OnSelectChangeListener {
        void onSelect(int index,String value);
    }

    private OnSelectChangeListener mOnSelectChangeListener;

    public void setOnSelectChangeListener(OnSelectChangeListener listener) {
        this.mOnSelectChangeListener = listener;
    }
}
