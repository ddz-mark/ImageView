package com.markable.photoimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.text.DisplayContext;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Markable on 2016/11/18.
 */

public class LineChart extends View {
    private static final String TAG = LineChart.class.getName();

    private int coordinatesLineWidth;
    private int coordinatesTextSize;
    private int coordinatesTextColor;
    private int lineColor;
    private int lineWidth;
    private int averageCircleradius;
    private String tableType;
    private int maxcircleColor;
    private int mincircleColor;
    private int bgColor;

    private Paint xyPaint;

    private Rect textBound;

    private String[] weeks;

    private Paint textPaint;

    private int[] values;

    private Paint linePaint;

    private Paint maxCirclePaint;

    private Paint minCirclePaint;

    private int XScale;

    private int width, height;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineChart, defStyleAttr, 0);
        int index = array.getIndexCount();
        for (int i = 0; i < index; i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.LineChart_coordinatesLineWidth) {
                coordinatesLineWidth = array.getDimensionPixelOffset(attr,
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.LineChart_coordinatesTextSize) {
                coordinatesTextSize = array.getDimensionPixelSize(attr,
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 11,
                                getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.LineChart_coordinatesTextColor) {
                coordinatesTextColor = array.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LineChart_lineColor) {
                lineColor = array.getColor(attr, Color.BLUE);
            } else if (attr == R.styleable.LineChart_lineWidth) {
                lineWidth = array.getDimensionPixelOffset(attr,
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 11,
                                getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.LineChart_averageCircleradius) {
                averageCircleradius = array.getDimensionPixelSize(attr,
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 10,
                                getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.LineChart_tableType) {
                tableType = array.getString(attr);
            } else if (attr == R.styleable.LineChart_maxcircleColor) {
                maxcircleColor = array.getColor(attr, Color.GREEN);
            } else if (attr == R.styleable.LineChart_mincircleColor) {
                mincircleColor = array.getColor(attr, Color.WHITE);
            } else if (attr == R.styleable.LineChart_bgColor)
                bgColor = array.getColor(attr, Color.WHITE);
        }
        array.recycle();
        init();
    }

    private void init() {

        xyPaint = new Paint();
        xyPaint.setAntiAlias(true);
        xyPaint.setColor(coordinatesTextColor);
        xyPaint.setStrokeWidth(coordinatesLineWidth);
        xyPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(coordinatesTextColor);
        textPaint.setTextSize(coordinatesTextSize);
        textPaint.setStyle(Paint.Style.STROKE);
        textBound = new Rect();

        minCirclePaint = new Paint();
        minCirclePaint.setStyle(Paint.Style.FILL);
        minCirclePaint.setColor(Color.WHITE);
        minCirclePaint.setAntiAlias(true);

        maxCirclePaint = new Paint();
        maxCirclePaint.setStyle(Paint.Style.FILL);
        maxCirclePaint.setColor(maxcircleColor);
        maxCirclePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * 自定义控件的宽高必须由调用者自己指定具体的数值
         */
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            width = widthSpecSize;
        } else {
            width = 300;

        }

        if (heightSpecMode == MeasureSpec.EXACTLY) {
            // 高是宽的3/5,这样好吗?
            height = (width / 5) * 3;
        } else {
            height = 230;
        }
        Log.i(TAG, "width=" + width + "...height=" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        XScale = (width - getPaddingRight() - getPaddingLeft() - 40) / 6;
        canvas.drawColor(bgColor);
        //画坐标系
        drawCoordinates(canvas);
        //画坐标轴上的数值
        drawCoordinatesXvalues(canvas);
        drawTypeValues(canvas);
    }

    private void drawTypeValues(Canvas canvas)
    {

        switch (tableType)
        {
            case "activity":
                values = new int[]
                        { 6666, 38888, 56956, 43345, 42765, 66666, 37892 };
                break;
            case "breath":
                values = new int[]
                        { 15, 26, 18, 22, 27, 18, 20 };
                break;
            case "blood_oxygen":
                values = new int[]
                        { 96, 86, 96, 95, 99, 88, 81 };
                break;
            case "sinketemp":
                values = new int[]
                        { 33, 34, 32, 30, 36, 28, 31 };
                break;
            case "heartrate":
                values = new int[]
                        { 67, 66, 68, 74, 77, 128, 98 };
                break;
        }

        int[] YValues = cacluterYValues(getArrayMax(), getArrayMin());
        if (tableType.equals("sleep"))
        {
            values = new int[]
                    { 7, 5, 9, 5, 6, 6, 8 };
            YValues = new int[]
                    { 0, 3, 6, 9, 12 };
            linePaint.setColor(Color.parseColor("#6acfed"));
            drawLine(canvas, YValues[4] - YValues[0], YValues[0]);
            values = new int[]
                    { 3, 4, 5, 2, 2, 5, 2 };
            linePaint.setColor(Color.parseColor("#008fff"));
            drawLine(canvas, YValues[4] - YValues[0], YValues[0]);

        }

        for (int i = 0; i < YValues.length; i++)
        {
            Log.i(TAG, tableType + YValues[i]);
        }
        // YValues[4]-YValues[0] 差值就是全部高度
        drawYValues(canvas, YValues[4] - YValues[0], YValues);
        drawLine(canvas, YValues[4] - YValues[0], YValues[0]);

    }

    private void drawLine(Canvas canvas, float arraymax, float yMin)
    {

        // 这里是整个Y轴可用高度除以最大值,就是每个值占有刻度上的几等分;
        float YScale = ((height - getPaddingBottom() - getPaddingTop() - 40))
                / arraymax;
        for (int i = 0; i < values.length; i++)
        {
            // 为什么是values[i] - arraymin(数据值-Y坐标最小值)?
            // 因为圆点是以数据值来画得,数据值和Y轴坐标最小值的差就是整个数据的区间;
            int scale = (int) (values[i] - yMin);

            int j;
            /**
             * 画折线
             */
            if (i < 6)
            {
                int textScale = (int) (values[i + 1] - yMin);
                j = i + 1;
                canvas.drawLine(getPaddingLeft() + (XScale * i),
                        height - getPaddingBottom() - (YScale * scale),
                        getPaddingLeft() + (XScale * j),
                        height - getPaddingBottom() - (YScale * textScale),
                        linePaint);
            }

            String text = String.valueOf(values[i]);
            textPaint.getTextBounds(text, 0, text.length(), textBound);
            canvas.drawText(text,
                    getPaddingLeft() + (XScale * i) - textBound.width() / 2,
                    height - getPaddingBottom() - (YScale * scale) - 15,
                    textPaint);

            /**
             * 两个小圆点
             */
            canvas.drawCircle(getPaddingLeft() + (XScale * i),
                    height - getPaddingBottom() - (YScale * scale), 10,
                    maxCirclePaint);
            canvas.drawCircle(getPaddingLeft() + (XScale * i),
                    height - getPaddingBottom() - (YScale * scale), 10 - 2,
                    minCirclePaint);

        }

    }

    /**
     * 画Y轴上的数值
     *
     * @param canvas
     */
    private void drawYValues(Canvas canvas, float max, int[] value)
    {
        // 这里除以max这个最大值是为了有多大的去见就分成多少等分,是的后面折线的点更精准,否者就会对不齐刻度,
        float YScale = ((float) height - getPaddingBottom() - getPaddingTop()
                - 40) / max;
        for (int i = 0; i < value.length; i++)
        {
            Log.i(TAG, "activity=" + value[i] / 1000f);

            String text = tableType.equals("activity") ? String.format("%.1f",value[i] / 1000f) + "k"
                    : value[i] + "";
            int scale = value[i] - value[0];
            textPaint.getTextBounds(text, 0, text.length(), textBound);
            // +textBound.height()/2 主要是为了让字体和间断线居中
            canvas.drawText(text,
                    getPaddingLeft() - 100, height - getPaddingBottom()
                            - (YScale * scale) + textBound.height() / 2,
                    textPaint);
            // 和X轴重合的线不画
            if (i != 0)
            {
                canvas.drawLine(getPaddingLeft(),
                        height - getPaddingBottom() - (YScale * scale),
                        width - getPaddingRight(),
                        height - getPaddingBottom() - (YScale * scale),
                        xyPaint);
            }
        }

    }

    /**
     * 绘制X轴上的数值
     *
     * @param canvas
     */
    private void drawCoordinatesXvalues(Canvas canvas)
    {

        // -40 为X轴留点边界。 /6分成7等分

        for (int i = 0; i < weeks.length; i++)
        {
            textPaint.getTextBounds(weeks[i], 0, weeks[i].length(), textBound);
            // 画建断线
            canvas.drawLine(getPaddingLeft() + (i * XScale),
                    height - getPaddingBottom() - 10,
                    getPaddingLeft() + (i * XScale),
                    height - getPaddingBottom(), xyPaint);
            // -textBound.width()/2 是为了让字体居中与间断线
            canvas.drawText(weeks[i],
                    getPaddingLeft() + (i * XScale) - textBound.width() / 2,
                    height - getPaddingBottom() + 30, textPaint);
        }

    }

    /**
     * 画坐标系
     *
     * @param canvas
     */
    private void drawCoordinates(Canvas canvas)
    {

        // X轴
        Log.i(TAG, "drawCoordinates");
        canvas.drawLine(getPaddingLeft(), height - getPaddingBottom(),
                width - getPaddingRight(), height - getPaddingBottom(),
                xyPaint);
        // X轴上的箭头
        canvas.drawLine(width - getPaddingRight() - 20,
                height - getPaddingBottom() - 10, width - getPaddingRight(),
                height - getPaddingBottom(), xyPaint);
        canvas.drawLine(width - getPaddingRight() - 20,
                height - getPaddingBottom() + 10, width - getPaddingRight(),
                height - getPaddingBottom(), xyPaint);

        // 绘制Y轴
        canvas.drawLine(getPaddingLeft(), getPaddingTop(), getPaddingLeft(),
                height - getPaddingBottom(), xyPaint);

        // Y轴上的箭头
        canvas.drawLine(getPaddingLeft() - 10, getPaddingTop() + 20,
                getPaddingLeft(), getPaddingTop(), xyPaint);
        canvas.drawLine(getPaddingLeft() + 10, getPaddingTop() + 20,
                getPaddingLeft(), getPaddingTop(), xyPaint);
    }

    /**
     * 获取数值中的最大值
     *
     * @return
     */
    private float getArrayMax()
    {

        float max = 0;
        for (int i = 0; i < values.length; i++)
        {
            float pre = Float.parseFloat(values[i] + "");
            max = Math.max(max, pre);
        }
        return max;
    }

    /**
     * 获取最小值
     *
     * @return
     */
    private float getArrayMin()
    {
        float min = 999999;
        for (int i = 0; i < values.length; i++)
        {
            float pre = Float.parseFloat(values[i] + "");
            min = Math.min(min, pre);
        }

        return min;
    }

    /**
     * 传入数组中的最大值和最小值,计算出在Y轴上数值的区间
     *
     * @param max
     * @param min
     * @return
     */
    private int[] cacluterYValues(float max, float min)
    {
        int[] values;
        int min1;
        int max1;
        int resultNum = getResultNum(min); // 计算出的最小值
        max1 = getResultNum(max); // 计算出最大值
        if (resultNum <= 20) // 如果小于等于20 就不要减20,否则Y最小值是0了
        {
            min1 = resultNum - 10;
        }
        else
        {

            min1 = resultNum - 20;
        }

        //步行特殊处理
        if (resultNum >= 1000)
        {
            min1 = resultNum - 1000;
        }

        if (resultNum <= 10 || resultNum == 0) // 如果小于10 就不用再减了,否则就是负数了
        {
            min1 = 0;
        }

        // 将计算出的数值均分为5等分
        double ceil = Math.ceil((max1 - min1) / 4);
        values = new int[]
                { min1, (int) (min1 + ceil), (int) (min1 + ceil * 2),
                        (int) (min1 + ceil * 3), (int) (min1 + ceil * 4) };
        return values;

    }

    /**
     * 最高位 为什么要取出最高值,这里主要是通过计算动态的算出Y轴上的数值区间,
     * 比如心率是60-100,不计算给死就是0-180,这样折线的所有点就全部落在中间一点的地带,上下都有较大的空白,影响美观(心率一般在60-
     * 100之间) 比如计步的幅度很大,如果不通过动态计算就不知道Y轴画的数值给多少合适,比如Y轴数值写死为0-20000,
     * 那么如果运动量偏少,比如都是1000步左右,折线就显得几乎和X=0平齐了
     *
     * @param num
     * @return
     */
    private int getResultNum(float num)
    {
        int resultNum;
        int gw = 0; // 个位
        int sw = 0; // 十位
        int bw = 0; // 百位
        int qw = 0; // 千位
        int ww = 0; // 万位

        if (num > 0)
        {
            gw = (int) (num % 10 / 1);
        }
        if (num > 10)
        {
            sw = (int) (num % 100 / 10);
        }

        if (num > 100)
        {
            bw = (int) (num % 1000 / 100);
        }

        if (num > 1000)
        {
            qw = (int) (num % 10000 / 1000);
        }

        if (num > 10000)
        {
            ww = (int) (num % 100000 / 10000);
        }
        /*********************************/
        if (ww >= 1)
        {
            resultNum = qw > 5 ? ww * 10000 + 10000 : ww * 10000 + 5000;
        }
        else if (qw >= 1)
        {
            resultNum = bw > 5 ? qw * 1000 + 1000 : qw * 1000 + 500;
        }
        else if (bw >= 1)
        {
            resultNum = bw * 100 + sw * 10 + 10;

        }
        else if (sw >= 1)
        {

            resultNum = gw > 5 ? sw * 10 + 20 : sw * 10 + 10;
        }
        else
        {
            resultNum = 0;
        }

        return resultNum;
    }
}
