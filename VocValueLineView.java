/**
 * 
 */

package com.tencent.obd.view;

import com.tencent.navsns.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author robbinjia
 */
public class VocValueLineView extends RelativeLayout {

    private static final String TAG = "VocValueLineView";

    private int viewWidth;

    private int viewHeight;

    private Point point = new Point();// 用来保存点的位置

    private static final int Range = 1000;// ppd数值图中显示范围0~1000

    private int mPositionX = 0;// ppd数值

    private double scale;// 比例

    private Bitmap mPointBitmap;// 圆点图片

    // private Bitmap mBgBitmap;// 背景图片

    // private PaintFlagsDrawFilter mPaintFlatDrawFilter;
    private Paint mPaint;

    private Paint mLinePaint;

    private int pointOffsetX;

    private int pointOffsetY;

    // private double a;// 斜率

    // private TextView instruction2_tv;// 第二条说明textview

    private LinearLayout brand_layout;// 随着圆点动的牌子

    // private int instruction2_bottom;

    private int brand_height;

    private int brand_width;

    /**
     * @param context
     */
    public VocValueLineView(Context context) {
        super(context);
        init(context);
    }

    public VocValueLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public VocValueLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mPaint = new Paint();
        mPaint.setAntiAlias(false);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(false);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(0xfff0f0f0);

        // mPaintFlatDrawFilter = new PaintFlagsDrawFilter(0,
        // Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mPointBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_voc_mark);
        // mBgBitmap = BitmapFactory.decodeResource(context.getResources(),
        // R.drawable.bg_voc_diagram);

        pointOffsetX = mPointBitmap.getWidth() / 2;
        pointOffsetY = mPointBitmap.getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // canvas.setDrawFilter(mPaintFlatDrawFilter);
        int left = point.x - pointOffsetX;
        int top = viewHeight - point.y - pointOffsetY;

        // Log.d("jiabin", "left:" + left + " top:" + top);

        // canvas.drawBitmap(mBgBitmap, 0, 0, null);

        for (int i = 0; i <= viewWidth; i++) {
            canvas.drawLine(i, 0, i, viewHeight - relationXY(i), mLinePaint);
        }

        canvas.drawBitmap(mPointBitmap, left, top, mPaint);
        setBrandPosition(left, top);

    }

    private int brandMargin = 5;// 圆点和牌子之间的间隔

    private void setBrandPosition(int left, int top) {

        int l = 0;
        int t = 0;
        int r = 0;
        int b = 0;

        // if (top - instruction2_bottom - brandMargin >= brand_height)
        if (top - brandMargin >= brand_height) {
            t = top - brandMargin - brand_height;
        } else {
            t = top + pointOffsetY * 2 + brandMargin;
        }

        if ((viewWidth - left - pointOffsetX * 2 >= brand_width / 2) && (left >= brand_width / 2)) {
            l = left + pointOffsetX - brand_width / 2;
        } else if (viewWidth - left - pointOffsetX * 2 < brand_width / 2) {
            l = viewWidth - pointOffsetX - brand_width;
        } else {
            l = pointOffsetX;
        }

        b = t + brand_height;
        r = l + brand_width;

        brand_layout.layout(l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        // a = (double) viewHeight * 3 / 4 / (double) viewWidth;

        scale = (double)viewWidth / (double)Range;// 用于ppd数值转换为真实宽度

        // Log.d("jiabin", "viewWidth:" + viewWidth + " viewHeight:" +
        // viewHeight);
        // Log.d("jiabin", "a:" + a + " scale:" + scale);

        //point.set(viewWidth, relationXY(viewWidth));// 点的初始位置

        brand_height = brand_layout.getMeasuredHeight();
        brand_width = brand_layout.getMeasuredWidth();

        Log.d(TAG, "brand_height:" + brand_height + ",brand_width:" + brand_width);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // instruction2_bottom = instruction2_tv.getBottom();
        // Log.d(TAG, "instruction2_bottom:" + instruction2_bottom);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initViews();
    }

    private void initViews() {

        // instruction2_tv = (TextView)getChildAt(1);
        // brand_layout = (LinearLayout)getChildAt(2);
        brand_layout = (LinearLayout)getChildAt(0);

    }

    /**
     * 曲线关系
     * 
     * @param x
     * @return y
     */
    private int relationXY(int x) {
        return (int)((viewHeight) * Math.atan((double)x / viewWidth));
    }

    public double getScale() {
        return scale;
    }

    public synchronized void setPositionX(int positionX) {
        if (positionX < 0) {
            mPositionX = 0;
        }
        if (positionX > viewWidth) {
            positionX = viewWidth;
        }
        if (positionX != mPositionX) {
            mPositionX = positionX;
        }

        // Log.d("jiabin", "setPositionX");
        // int positionY = relationXY(positionX);// 函数关系
        int realX = viewWidth - (int)(positionX);
        int realY = relationXY(realX);
        // Log.d("jiabin", "realX:" + realX + " ; realY:" + realY);
        point.set(realX, realY);

        // postInvalidate();
        invalidate();

        // brand_layout.layout(0, 0, brand_width, brand_height);
    }

    public int getPositionX() {
        return mPositionX;
    }

}
