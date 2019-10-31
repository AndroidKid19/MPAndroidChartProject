package com.wheel.chart.dashboardview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wheel.chart.R;
import com.wheel.chart.dashboardview.SpringInterpolator;
import com.wheel.chart.dashboardview.utils.PxUtils;
import com.wheel.chart.dashboardview.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * Description: MPAndroidChart v3.1.0
 * <p>
 * Created by MPAndroidChartProject on 10/10/2019 14:12
 * <a href="mailto:yuanw9@sina.com">Contact me</a>
 * ================================================
 */
public class DashboardView extends View {

    private DashboardViewAttr dashboardViewattr;
    private int progressStrokeWidth;//进度弧的宽度
    private String speed = "";//速度显示
    private String unit = "";//显示单位
    private int backgroundColor = 0;//背景颜色
    private String mText = ""; //文字内容
    private float startNum = 0;//开始值
    private float maxNum = 100;//最大值
    private int mTextSize;//文字的大小
    private int mTextColor;//设置文字颜色
    private int mTikeCount;//刻度的个数
    private int tikeGroup = 3;
    //刻度的文字描述
    private String[] tikeStrArray = null;

    private int startColor;
    private int endColor;
    private int progressColor;
    private int circleColor;

    //画笔
    private Paint paintBackground;
    private Paint paintOutCircle;
    private Paint paintProgressBackground;
    private Paint paintProgress;
    private Paint paintCenterCirclePointer;//内圆画笔
    private Paint paintCenterRingPointer;//内环画笔
    private Paint paintPointerRight;
    private Paint paintPointerLeft;
    private Paint paintPinterCircle;
    private Paint paintText;
    private Paint paintNum;
    private Paint paintTikeStr;
    private RectF rectF1, rectF2;

    private int OFFSET = 30;
    private int START_ARC = 150;
    private int DURING_ARC = 240;
    private int mMinCircleRadius; //中心圆点的半径
    private int mMinRingRadius; //中心圆环的半径

    private Context mContext;
    private int mWidth, mHight;
    float percent;
    float oldPercent = 0f;
    private ValueAnimator valueAnimator;
    private long animatorDuration;
    TimeInterpolator interpolator = new SpringInterpolator();

    public DashboardView(Context context) {
        this(context, null);
        init(context);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dashboardViewattr = new DashboardViewAttr(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        initAttr();
        initPaint();
    }

    private void initPaint() {
        //初始化画笔
        paintOutCircle = new Paint();
        paintOutCircle.setAntiAlias(true);
        paintOutCircle.setStyle(Paint.Style.STROKE);
        paintOutCircle.setStrokeWidth(2);
        paintOutCircle.setColor(getResources().getColor(R.color.shadow));
        paintOutCircle.setDither(true);
        paintBackground = new Paint();
        paintBackground.setAntiAlias(true);
        paintBackground.setStyle(Paint.Style.FILL);
        paintBackground.setStrokeWidth(2);
        paintBackground.setDither(true);
        paintProgressBackground = new Paint();
        paintProgressBackground.setAntiAlias(true);
        paintProgressBackground.setStrokeWidth(progressStrokeWidth);
        paintProgressBackground.setStyle(Paint.Style.STROKE);
        paintProgressBackground.setStrokeCap(Paint.Cap.ROUND);
        paintProgressBackground.setColor(getResources().getColor(R.color.shadow));
        paintProgressBackground.setDither(true);
        paintProgress = new Paint();
        paintProgress.setAntiAlias(true);
        paintProgress.setStrokeWidth(progressStrokeWidth);
        paintProgress.setStyle(Paint.Style.STROKE);
        paintProgress.setStrokeCap(Paint.Cap.ROUND);
        paintProgress.setColor(progressColor);
        paintProgress.setDither(true);
        paintCenterCirclePointer = new Paint();
        paintCenterCirclePointer.setAntiAlias(true);
        paintCenterCirclePointer.setStyle(Paint.Style.FILL);//实心画笔
        paintCenterCirclePointer.setDither(true);
        paintCenterRingPointer = new Paint();
        paintCenterRingPointer.setAntiAlias(true);
        paintCenterRingPointer.setColor(circleColor);
        paintCenterRingPointer.setStrokeWidth(mMinCircleRadius);
        paintCenterRingPointer.setStyle(Paint.Style.STROKE);//空心画笔
        paintCenterRingPointer.setDither(true);
        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setColor(mTextColor);
        paintText.setStrokeWidth(1);
        paintText.setStyle(Paint.Style.FILL);//实心画笔
        paintText.setDither(true);
        paintNum = new Paint();
        paintNum.setAntiAlias(true);
        paintNum.setColor(getResources().getColor(R.color.scale));
        paintNum.setStrokeWidth(2);
        paintNum.setStyle(Paint.Style.FILL);
        paintNum.setDither(true);
        paintPointerRight = new Paint();
        paintPointerRight.setAntiAlias(true);
        paintPointerRight.setColor(getResources().getColor(R.color.rightRight));
        paintPointerRight.setStyle(Paint.Style.FILL_AND_STROKE);
        paintPointerRight.setDither(true);
        paintPointerLeft = new Paint();
        paintPointerLeft.setAntiAlias(true);
        paintPointerLeft.setColor(getResources().getColor(R.color.leftRight));
        paintPointerLeft.setStyle(Paint.Style.FILL_AND_STROKE);
        paintPointerLeft.setDither(true);
        paintPinterCircle = new Paint();
        paintPinterCircle.setAntiAlias(true);
        paintPinterCircle.setColor(getResources().getColor(R.color.insideCircle));
        paintPinterCircle.setStyle(Paint.Style.FILL);
        paintPinterCircle.setDither(true);
        paintTikeStr = new Paint();
        paintTikeStr.setAntiAlias(true);
        paintTikeStr.setStyle(Paint.Style.FILL);
        paintTikeStr.setTextAlign(Paint.Align.LEFT);
        paintTikeStr.setColor(dashboardViewattr.getTikeStrColor());
        paintTikeStr.setTextSize(dashboardViewattr.getTikeStrSize());

    }


    private void initShader() {
        updateOval();
        if (startColor != 0 && endColor != 0) {
//            LinearGradient shader = new LinearGradient(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom,
//                    endColor, startColor, Shader.TileMode.CLAMP);
            SweepGradient shader = new SweepGradient(0, 0, new int[]{startColor, endColor}, null);
            float rotate = 90f;
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(rotate, 0, 0);
            shader.setLocalMatrix(gradientMatrix);
            paintProgress.setShader(shader);
        }
    }

    private void initAttr() {
        mMinCircleRadius = mWidth / 15;
        //刻度值 默认为最大值/10
        tikeStrArray = new String[0];

        tikeGroup = 5; // 默认1个长刻度间隔4个短刻度，加起来一组5
        if (tikeStrArray != null && tikeStrArray.length != 0) {
            //根据需要绘制的刻度数组大小计算刻度总数
            mTikeCount = (tikeStrArray.length - 1) * tikeGroup + 1;
        } else {
            tikeStrArray = new String[0];
            mTikeCount = 101;
        }
        mTextSize = dashboardViewattr.getmTextSize();
        mTextColor = dashboardViewattr.getTextColor();
        mText = dashboardViewattr.getmText();
        progressStrokeWidth = dashboardViewattr.getProgressStrokeWidth();
        unit = dashboardViewattr.getUnit();
        backgroundColor = dashboardViewattr.getBackground();
        startColor = dashboardViewattr.getStartColor();
        endColor = dashboardViewattr.getEndColor();
        startNum = dashboardViewattr.getStartNumber();
        maxNum = dashboardViewattr.getMaxNumber();
        progressColor = dashboardViewattr.getProgressColor();
        circleColor = dashboardViewattr.getCircleColor();
        if (dashboardViewattr.getPadding() == 0) {
            OFFSET = progressStrokeWidth + 10;
        } else {
            OFFSET = dashboardViewattr.getPadding();
        }

        // 开启硬件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHight = getWidth();
        initShader();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int realWidth = startMeasure(widthMeasureSpec);
        int realHeight = startMeasure(heightMeasureSpec);

        setMeasuredDimension(realWidth, realHeight);
    }


    private int startMeasure(int msSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(msSpec);
        int size = MeasureSpec.getSize(msSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = PxUtils.dpToPx(200, mContext);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.percent = percent / 100f;
        canvas.translate(mWidth / 2, mHight / 2);//移动坐标原点到中心
        //背景
        drawBackground(canvas);
        //表盘
        drawPanel(canvas);
        //绘制指针和进度弧
        drawPointerAndProgress(canvas, percent);
        //绘制矩形和文字
        drawText(canvas, percent);

    }

    private void drawPointerAndProgress(Canvas canvas, float percent) {
        //绘制粗圆弧
        drawProgress(canvas, percent);

        //绘制指针
        drawerPointer(canvas, percent);
    }

    private void drawPanel(Canvas canvas) {
        //绘制刻度
        drawerNum(canvas);
        //绘制中间小圆和圆环
        drawInPoint(canvas);
    }

    private void drawText(Canvas canvas, float percent) {
//        if (TextUtils.isEmpty(unit)) return;
        if (mText == null) {
            mText = "";
        }
        float length;
        paintText.setTextSize(mTextSize);
        length = paintText.measureText(mText);
        canvas.drawText(mText, -length / 2, mMinRingRadius * 2.0F, paintText);
        paintText.setTextSize(mTextSize * 1.2f);
        //当前值
        float current = startNum + (maxNum - startNum) * percent;
        if (current > maxNum) {
            current = maxNum;
        } else if (current < startNum) {
            current = startNum;
        }
        speed = StringUtil.floatFormat((int) current) + unit;

        length = paintText.measureText(speed);
        canvas.drawText(speed, -length / 2, mMinRingRadius * 2.5F, paintText);

    }

    private void drawerPointer(Canvas canvas, float percent) {
        mMinCircleRadius = mWidth / 15;
        rectF1 = new RectF(-mMinCircleRadius / 2, -mMinCircleRadius / 2, mMinCircleRadius / 2, mMinCircleRadius / 2);
        canvas.save();
        float angel = DURING_ARC * (percent - 0.5f) - 180;
        canvas.rotate(angel, 0, 0);//指针与外弧边缘持平
        Path pathPointerRight = new Path();
        pathPointerRight.moveTo(0, mMinCircleRadius / 2);
        pathPointerRight.arcTo(rectF1, 270, -90);
        pathPointerRight.lineTo(0, mHight / 2 - OFFSET - progressStrokeWidth);
        pathPointerRight.lineTo(0, mMinCircleRadius / 2);
        pathPointerRight.close();
        Path pathPointerLeft = new Path();
        pathPointerLeft.moveTo(0, mMinCircleRadius / 2);
        pathPointerLeft.arcTo(rectF1, 270, 90);
        pathPointerLeft.lineTo(0, mHight / 2 - OFFSET - progressStrokeWidth);
        pathPointerLeft.lineTo(0, mMinCircleRadius / 2);
        pathPointerLeft.close();
        Path pathCircle = new Path();
        pathCircle.addCircle(0, 0, mMinCircleRadius / 4, Path.Direction.CW);

        canvas.drawPath(pathPointerLeft, paintPointerLeft);
        canvas.drawPath(pathPointerRight, paintPointerRight);
        canvas.drawPath(pathCircle, paintPinterCircle);

        canvas.restore();

    }

    private void drawInPoint(Canvas canvas) {
        mMinCircleRadius = mWidth / 15;
        mMinRingRadius = mMinCircleRadius * 2 + mMinCircleRadius / 20;
        paintCenterRingPointer.setStrokeWidth(mMinCircleRadius);

        paintCenterCirclePointer.setColor(circleColor);
        canvas.drawCircle(0, 0, mMinCircleRadius, paintCenterCirclePointer);
        paintCenterCirclePointer.setColor(getResources().getColor(R.color.scale));
        canvas.drawCircle(0, 0, mMinCircleRadius, paintCenterCirclePointer); //中心圆点

        canvas.drawCircle(0, 0, mMinRingRadius, paintCenterRingPointer);//中心小圆环

    }

    private void drawerNum(Canvas canvas) {
        canvas.save(); //记录画布状态
        canvas.rotate(-(180 - START_ARC + 90), 0, 0);
        int numY = -mHight / 2 + OFFSET + progressStrokeWidth;
        float rAngle = DURING_ARC / ((mTikeCount - 1) * 1.0f); //n根线，只需要n-1个区间
        for (int i = 0; i < mTikeCount; i++) {
            canvas.save(); //记录画布状态
            canvas.rotate(rAngle * i, 0, 0);
            if (i == 0 || i % tikeGroup == 0) {
                canvas.drawLine(0, numY + 5, 0, numY + 25, paintNum);//画长刻度线
                if (tikeStrArray.length > (i % tikeGroup)) {
                    String text = tikeStrArray[i / tikeGroup].toString();
                    Paint.FontMetricsInt fontMetrics = paintTikeStr.getFontMetricsInt();
                    int baseline = ((numY + 40) + (fontMetrics.bottom - fontMetrics.top) / 2);
                    canvas.drawText(text, -getTextViewLength(paintTikeStr, text) / 2, baseline, paintTikeStr);
                }
            } else {
                canvas.drawLine(0, numY + 5, 0, numY + 15, paintNum);//画短刻度线
            }

            canvas.restore();
        }
        canvas.restore();

    }

    private float getTextViewLength(Paint paint, String text) {
        if (TextUtils.isEmpty(text)) return 0;
        float textLength = paint.measureText(text);
        return textLength;
    }

    private void drawProgress(Canvas canvas, float percent) {
        canvas.drawArc(rectF2, START_ARC, DURING_ARC, false, paintProgressBackground);
        if (percent > 1.0f) {
            percent = 1.0f; //限制进度条在弹性的作用下不会超出
        }
        if (!(percent <= 0.0f)) {
            canvas.drawArc(rectF2, START_ARC, percent * DURING_ARC, false, paintProgress);
        }
    }

    private void updateOval() {
        rectF2 = new RectF((-mWidth / 2) + OFFSET + getPaddingLeft(), getPaddingTop() - (mHight / 2) + OFFSET,
                (mWidth / 2) - getPaddingRight() - OFFSET,
                (mWidth / 2) - getPaddingBottom() - OFFSET);
    }

    private void drawBackground(Canvas canvas) {

        //最外阴影
        canvas.drawCircle(0, 0, mWidth / 2 - 2, paintOutCircle);
        canvas.save();
        //背景
        if (backgroundColor != 0) {
            paintBackground.setColor(backgroundColor);
            canvas.drawCircle(0, 0, mWidth / 2 - 4, paintBackground);
        }
    }

    //每次view被隐藏在显示出来之后percent都会变成一个0.000000x的值，原因未知，暂时采取暴力方式处理。
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        percent = oldPercent;
    }

    /**
     * 设置值
     *
     * @param percent
     */
    public void setPercent(float percent) {
        if (percent > maxNum) {
            percent = maxNum;
        } else if (percent < startNum) {
            percent = startNum;
        }
        percent = percent - startNum;
        setAnimator(percent * 100 / (maxNum - startNum));
    }

    private void setAnimator(final float percent) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }

        animatorDuration = (long) Math.abs(percent - oldPercent) * 20;

        valueAnimator = ValueAnimator.ofFloat(oldPercent, percent).setDuration(animatorDuration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DashboardView.this.percent = (float) animation.getAnimatedValue();
                invalidate();

            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                oldPercent = percent;

                //防止因为插值器误差产生越界
                if (DashboardView.this.percent < 0.0) {
                    DashboardView.this.percent = 0.0f;
                    invalidate();
                }
                if (DashboardView.this.percent > 100.0) {
                    DashboardView.this.percent = 100.0f;
                    invalidate();
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        mText = text;
        invalidate();
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        mTextSize = size;
        invalidate();
    }

    /**
     * 设置字体颜色
     *
     * @param mTextColor
     */
    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    /**
     * 设置弧的高度
     *
     * @param dp
     */
    public void setProgressStroke(int dp) {

        progressStrokeWidth = PxUtils.dpToPx(dp, mContext);
        paintProgress.setStrokeWidth(progressStrokeWidth);
        paintProgressBackground.setStrokeWidth(progressStrokeWidth);
        invalidate();
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
        invalidate();
    }

    /**
     * 设置插值器
     *
     * @param interpolator
     */
    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    /**
     * 设置起始颜色
     *
     * @param startColor
     */
    public void setStartColor(int startColor) {
        this.startColor = startColor;
        initShader();
    }

    /**
     * 设置结束颜色
     *
     * @param endColor
     */
    public void setEndColor(int endColor) {
        this.endColor = endColor;
        initShader();
    }

    /**
     * 设置起始值
     *
     * @param startNum
     */
    public void setStartNum(float startNum) {
        this.startNum = startNum;
    }

    /**
     * 设置最大值
     *
     * @param maxNum
     */
    public void setMaxNum(float maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * 设置刻度文字显示
     *
     * @param maxNum
     */
    public void setTikeStrArray(float maxNum) {
        this.maxNum = maxNum;
        List<String> list = new ArrayList<>();
        //向上取整
        int number = (int) Math.ceil((maxNum - startNum) / 10);
        Log.e("TAG", "--------ww-----" + (maxNum - startNum));
        Log.e("TAG", "-------w---" + number);
        for (int i = 0; i <= 10; i++) {
            list.add(String.valueOf((int) (number * i + startNum)));
        }
        this.tikeStrArray = list.toArray(new String[list.size()]);
        mTikeCount = (tikeStrArray.length - 1) * tikeGroup + 1;
        initShader();
    }

    /**
     * 设置刻度文字显示
     *
     * @param tikeStrArray
     */
    public void setTikeStrArray(String[] tikeStrArray) {
        this.tikeStrArray = tikeStrArray;
        mTikeCount = (tikeStrArray.length - 1) * tikeGroup + 1;
        initShader();
    }
}