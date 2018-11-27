package ca.ualberta.t04.medicaltracker.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    private Paint mPaintCircle;
    private float mCircleX, mCircleY;
    private float mCircleRadius = 20f;

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

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaintCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value  = super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();

            mCircleX = x;
            mCircleY = y;

            postInvalidate();

        }
        return value;

    }
}
