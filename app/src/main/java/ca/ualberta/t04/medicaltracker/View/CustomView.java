package ca.ualberta.t04.medicaltracker.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {
    private Paint mPaintCircle;
    private float mCircleX, mCircleY;
    private float mCircleRadius = 20f;
    private Bitmap bitmap;
    private float height = 0;
    private float width = 0;
    private float bitmapHeight = 0;
    private float bitmapWidth = 0;
    private float startTop;
    private float startLeft;

    private boolean isTouch = false;

    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.RED);

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
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        startTop = (height - bitmapHeight)/2;
        startLeft = (width - bitmapWidth)/2;
        if(!isTouch)
            canvas.drawBitmap(bitmap, startLeft, startTop, mPaintCircle);
        else{
            canvas.drawBitmap(bitmap, startLeft, startTop, mPaintCircle);
            canvas = new Canvas(bitmap);
            canvas.drawBitmap(bitmap, startLeft*(bitmapWidth/width)+bitmapWidth , startTop*(bitmapHeight/height)+bitmapHeight, mPaintCircle);
            canvas.drawCircle(mCircleX - startLeft, mCircleY - startTop, mCircleRadius, mPaintCircle);
        }
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;

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

            isTouch = true;

            postInvalidate();

        }
        return value;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
