package com.laluhilman.circleindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by laluhilman on 20/11/17.
 */


public class CircleBarIndicator extends View {

    ValueAnimator animation = null;
    private long animationDuration = 5000;

    protected RectF barRect = new RectF();
    protected RectF countourOutRect = new RectF();
    protected RectF teksRect = new RectF();
    protected RectF outerRect = new RectF();
    protected RectF innerRect = new RectF();
    protected RectF rimRect = new RectF();

    protected float top;
    protected float left;
    protected float right;
    protected float buttom;
    protected double height;
    protected double width;

    private int strokeSize;
    private int progressBarSize;
    private int teksSize;

    private float progresBarPosition;


    private int barColor;
    private int rimColor;
    private int fillColor;
    private int countourColor;
    private int teksColor;

    private Paint mBarPaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint fillPaint = new Paint();
    private Paint countourPaint = new Paint();
    private Paint teksPaint = new Paint();
    private Paint.Cap mBarStrokeCap = Paint.Cap.BUTT;

    public CircleBarIndicator(Context context) {
        super(context);
    }

    public CircleBarIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        generateDefaultColor(context);
        generateDefaultSize();
        loadAttributeValue(attrs, context);
        initDefaultValue();
    }


    public CircleBarIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void loadAttributeValue(AttributeSet attrs, Context context){

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleBarIndicator, 0, 0);
        barColor = ta.getColor(R.styleable.CircleBarIndicator_barColor, barColor  );
        rimColor = ta.getColor(R.styleable.CircleBarIndicator_rimColor, rimColor  );
        fillColor = ta.getColor(R.styleable.CircleBarIndicator_fillColor, fillColor  );
        countourColor = ta.getColor(R.styleable.CircleBarIndicator_countourColor, countourColor  );
        teksColor = ta.getColor(R.styleable.CircleBarIndicator_teksColor, teksColor  );
        progresBarPosition = ta.getFloat(R.styleable.CircleBarIndicator_barProgress, progresBarPosition);
        progressBarSize = ta.getInt(R.styleable.CircleBarIndicator_barSize, progressBarSize);
    }

    private void initDefaultValue(){
        setBarPaint();
        setRimPaint();
        setCountourPaint();
        setFillPaint();
        setUpTeksPaint();
    }

    private void generateDefaultColor(Context context){
        barColor = context.getResources().getColor(R.color.light_aqua);
        rimColor = context.getResources().getColor(R.color.gray);
        fillColor = context.getResources().getColor(R.color.smooth_red);
        countourColor = context.getResources().getColor(R.color.smooth_orange);
        teksColor = context.getResources().getColor(R.color.white);
    }


    private void generateDefaultSize(){
        strokeSize = 40;
        progresBarPosition= 25;
        progressBarSize = 100;
    }


    private void setBarPaint(){
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStrokeCap(mBarStrokeCap);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setColor(barColor);
        mBarPaint.setStrokeWidth(progressBarSize);
    }

    private void setRimPaint(){
        rimPaint.setStyle(Paint.Style.FILL);
        rimPaint.setColor(rimColor);
    }


    private void setCountourPaint(){

        countourPaint.setStrokeCap(mBarStrokeCap);
        countourPaint.setStyle(Paint.Style.STROKE);
        countourPaint.setStrokeWidth(5);
        countourPaint.setColor(countourColor);
    }

    private void setUpTeksPaint(){
        teksPaint.setColor(teksColor);
        teksPaint.setTextAlign(Paint.Align.CENTER);
        teksPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }


    private void setFillPaint(){
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(fillColor);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleBarIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        calculateSize();

        if(convertfloatToInt(progresBarPosition)<10){
            teksSize = (int)(( (getWidth()/2) -((int)(getLeftPoint()+progressBarSize)))  * 0.9);
        } else if(convertfloatToInt(progresBarPosition)<100) {
            teksSize = (int)(( (getWidth()/2) -((int)(getLeftPoint()+progressBarSize)))  * 0.8);

        } else {
            teksSize = (int)(( (getWidth()/2) -((int)(getLeftPoint()+progressBarSize)))  * 0.63);
        }
        drawOnCanvas(canvas);
    }


    private void drawOnCanvas(Canvas canvas){
        mBarPaint.setStrokeWidth( progressBarSize/(float)1.3);
        teksPaint.setTextSize(teksSize);

        canvas.drawArc(outerRect, 270, 360, false,rimPaint);
        canvas.drawArc(outerRect, 270, 360, false,countourPaint);
        canvas.drawArc(innerRect, 270, 360, false,fillPaint);
        canvas.drawArc(innerRect, 270, 360, false,countourPaint);
        canvas.drawArc(barRect, 270, progresBarPosition, false,mBarPaint);
        canvas.drawText(""+convertfloatToInt(progresBarPosition)+"%", teksRect.centerX(),teksRect.bottom, teksPaint );
//        canvas.drawRect(teksRect, teksPaint );

    }


    public void setProgress(int progress){
        animateProgress((float) (progress*360)/100);
    }

    private void animateProgress(float progres){
        float previousValue = progresBarPosition;
        progresBarPosition = progres;
        animation = ValueAnimator.ofFloat(previousValue, progresBarPosition);

        //animationDuration specifies how long it should take to animate the entire graph, so the
        //actual value to use depends on how much the value needs to change
        int changeInValue = Math.abs((int)progres - (int)previousValue);
        long durationToUse = (long) (animationDuration * ((float) changeInValue / (float) 360));
        animation.setDuration(durationToUse);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progresBarPosition = (float) valueAnimator.getAnimatedValue();
                CircleBarIndicator.this.invalidate();
            }
        });

        animation.start();

    }

    private void calculateSize(){
        top = getPaddingTop();
        buttom = getHeight()-getPaddingTop()-getPaddingBottom()-(2*strokeSize);
        left = getPaddingLeft();
        right = getWidth()-getPaddingRight()-(2*strokeSize);
        height = getHeight();
        width = getWidth();
        progressBarSize = (getWidth()/2)/2;
        teksRect = new RectF(getCenterWidth()-getHeight()/14,getCenterHeight()-getHeight()/14, getCenterWidth()+getHeight()/14, getCenterHeight()+getHeight()/14);
        outerRect = new RectF(getLeftPoint(), getTopPoint(), getRightPoint(), getBottomPoint());
        innerRect = new RectF(getLeftPoint()+progressBarSize, getTopPoint()+progressBarSize, getRightPoint()-progressBarSize, getBottomPoint()-progressBarSize);
        barRect = new RectF(getLeftPoint()+(progressBarSize/2), getTopPoint()+(progressBarSize/2), getRightPoint()-(progressBarSize/2), getBottomPoint()-(progressBarSize/2));
        countourOutRect = new RectF(left,top, right, buttom);
    }

    private float getCenterWidth(){
        return (getWidth())/2;
    }

    private float getLeftPoint(){
        return  getLeft() + getPaddingLeft();
    }

    private float getTopPoint(){
        return getTop()+getPaddingTop();
    }

    private float getRightPoint(){
        return getRight()-getPaddingRight();
    }

    private float getBottomPoint(){
        return getBottom()-getPaddingBottom();
    }

    private float getCenterHeight(){
        return (getHeight())/2;
    }

    private int measureHeight(int measureSpec) {
        int size = getPaddingTop() + getPaddingBottom();
        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int convertfloatToInt(float progresBarPosition){
        return (int)((progresBarPosition/360)*100);
    }


    private int measureWidth(int measureSpec) {

        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        size += bounds.width();
        bounds = new Rect();
        size += bounds.width();
        return resolveSizeAndState(size, measureSpec, 0);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

}

