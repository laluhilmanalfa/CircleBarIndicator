package com.laluhilman.drawcircleandroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by laluhilman on 14/11/17.
 */

public class CircleIndicator extends View {

    protected RectF barRect = new RectF();
    protected RectF countourOutRect = new RectF();
    protected RectF teksRect = new RectF();
    protected float top;
    protected float left;
    protected float right;
    protected float buttom;
    protected double height;
    protected double width;

    private int strokeSize;
    private int teksSize;




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
    private final int mBarColorStandard = 0xff009688; //stylish blue




    public CircleIndicator(Context context) {
        super(context);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        generateDefaultColor(context);
        generateDefaultSize();
        loadAttributeValue(attrs, context);
        initDefaultValue();
    }


    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void loadAttributeValue(AttributeSet attrs, Context context){

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleIndicator, 0, 0);

        barColor = ta.getColor(R.styleable.CircleIndicator_barColor, barColor  );
        rimColor = ta.getColor(R.styleable.CircleIndicator_rimColor, rimColor  );
        fillColor = ta.getColor(R.styleable.CircleIndicator_fillColor, fillColor  );
        countourColor = ta.getColor(R.styleable.CircleIndicator_countourColor, countourColor  );
        teksColor = ta.getColor(R.styleable.CircleIndicator_teksColor, teksColor  );


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
        rimColor = context.getResources().getColor(R.color.dark_aqua);
        fillColor = context.getResources().getColor(R.color.smooth_red);
        countourColor = context.getResources().getColor(R.color.smooth_orange);
        teksColor = context.getResources().getColor(R.color.white);
    }


    private void generateDefaultSize(){
        strokeSize = 40;
    }


    private void setBarPaint(){
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStrokeCap(mBarStrokeCap);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setColor(barColor);
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
    }


    private void setFillPaint(){
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(fillColor);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        calculateSize();
        mBarPaint.setStrokeWidth( (float)(((getHeight()/5)- (getHeight()/8))/1.25));
        teksPaint.setTextSize(teksSize);

        canvas.drawCircle(getCenterWidth(), getCenterHeight(), getHeight()/5, rimPaint);
        canvas.drawCircle(getCenterWidth(), getCenterHeight(), getHeight()/8, fillPaint);
        canvas.drawCircle(getCenterWidth(), getCenterHeight(), getHeight()/8, countourPaint);
        canvas.drawCircle(getCenterWidth(), getCenterHeight(), getHeight()/5, countourPaint);
        canvas.drawText("100", teksRect.centerX(),teksRect.bottom, teksPaint );
        canvas.drawRect(teksRect, countourPaint );
        canvas.drawArc(barRect, 270, 100, false, mBarPaint);


    }



    private void calculateSize(){
        top = getPaddingTop();
        buttom = getHeight()-getPaddingTop()-getPaddingBottom()-(2*strokeSize);
        left = getPaddingLeft();
        right = getWidth()-getPaddingRight()-(2*strokeSize);
        height = getHeight();
        width = getWidth();
        teksSize =   ((int)getCenterWidth()+getHeight()/15)-((int)getCenterWidth()-getHeight()/15);



        barRect = new RectF(getCenterWidth()-(float)(getHeight()/6.25),getCenterHeight()-(float)(getHeight()/6.25), getCenterWidth()+(float)(getHeight()/6.25), getCenterHeight()+(float)(getHeight()/6.25));
        teksRect = new RectF(getCenterWidth()-getHeight()/14,getCenterHeight()-getHeight()/14, getCenterWidth()+getHeight()/14, getCenterHeight()+getHeight()/14);
        countourOutRect = new RectF(left,top, right, buttom);
    }

    private float getCenterWidth(){
        return (getWidth()-getPaddingLeft()-getPaddingRight())/2;
    }


    private float getCenterHeight(){
        return (getHeight()-getPaddingTop()-getPaddingBottom())/2;
    }



    private int measureHeight(int measureSpec) {

        int size = getPaddingTop() + getPaddingBottom();
        return resolveSizeAndState(size, measureSpec, 0);
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
