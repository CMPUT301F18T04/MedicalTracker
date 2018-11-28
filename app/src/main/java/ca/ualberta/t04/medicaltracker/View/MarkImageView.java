package ca.ualberta.t04.medicaltracker.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.widget.ImageView;

import ca.ualberta.t04.medicaltracker.Util.ImageUtil;

public class MarkImageView extends AppCompatImageView {
    private Paint mPaintCircle;
    private float circleWidth = 10;

    private float mCircleX, mCircleY;
    private float mCircleRadius = 40f;
    private Bitmap bitmap;
    private Bitmap editBitmap;

    private float height = 0;
    private float width = 0;
    private float bitmapHeight = 0;
    private float bitmapWidth = 0;
    private float startTop;
    private float startLeft;

    private float originHeight = 0;
    private float originWeight = 0;

    public MarkImageView(Context context) {
        super(context);

        init(null);
    }

    public MarkImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public MarkImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.RED);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(circleWidth);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        width = xNew;
        height = yNew;
        float bitmapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();
        float scale = bitmapHeight/bitmapWidth;
        float newWidth = width;
        float newHeight = newWidth * scale;
        Log.d("test", String.valueOf(newHeight) + "," + String.valueOf(newWidth));
        if(newHeight>height){
            newHeight = height;
            newWidth = newHeight / scale;
        }
        this.bitmapHeight = newHeight;
        this.bitmapWidth = newWidth;
        originHeight = bitmap.getHeight();
        originWeight = bitmap.getWidth();
        Log.d("Succeed", "Before:" + String.valueOf(originHeight) + "," + String.valueOf(originWeight));
        Log.d("Succeed", "Before:" + String.valueOf(ImageUtil.convertBitmapToString(bitmap).length()));
        //this.bitmap = bitmap.copy(bitmap.getConfig(), true);
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);
        Log.d("Succeed", "Scaled:" + String.valueOf(newWidth) + "," + String.valueOf(newHeight));
        Log.d("Succeed", "Scaled:" + String.valueOf(ImageUtil.convertBitmapToString(bitmap).length()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        startTop = (height - bitmapHeight)/2;
        startLeft = (width - bitmapWidth)/2;

        editBitmap = bitmap.copy(bitmap.getConfig(), true);
        canvas.drawBitmap(editBitmap, startLeft, startTop, mPaintCircle);

        Canvas bitmapCanvas = new Canvas(editBitmap);

        bitmapCanvas.drawCircle(mCircleX - startLeft, mCircleY - startTop, mCircleRadius, mPaintCircle);

    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        Log.d("test", String.valueOf(bitmap.getByteCount()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value  = super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();

            if(x<startLeft || x>startLeft + bitmapWidth || y>bitmapHeight + startTop || y<startTop)
                return value;

            mCircleX = x;
            mCircleY = y;

            postInvalidate();
        }
        return value;
    }

    public Bitmap getBitmap() {
        this.editBitmap = Bitmap.createScaledBitmap(editBitmap, (int)originWeight, (int)originHeight, false);
        Log.d("Succeed", "Get:" + String.valueOf(ImageUtil.convertBitmapToString(bitmap).length()));
        return editBitmap;
    }

}
