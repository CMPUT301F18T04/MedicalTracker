package ca.ualberta.t04.medicaltracker.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

//    private static final int SQUARE_SIZE = 100;
//    private Rect mRectSquare;
//    private Paint mPaintSquare;

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
//        mRectSquare = new Rect();
//        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaintSquare.setColor(Color.GREEN);



//        if (set == null)
//            return;
//
//        TypedArray ta = getContext().obtainStyledAttributes(set, android.support.v4.R.styleable.Cus)
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        mRectSquare.left = 50;
//        mRectSquare.top = 50;
//        mRectSquare.right = mRectSquare.left + SQUARE_SIZE;
//        mRectSquare.bottom = mRectSquare.top + SQUARE_SIZE;
//
//
//        canvas.drawRect(mRectSquare, mPaintSquare);
//
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.RED);

        // need to fill in
//        if (mCircleX == 0f || mCircleY == 0f){
//            mCircleX = getWidth() / 2;
//            mCircleX = getHeight() / 2;
//        }

        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaintCircle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value  = super.onTouchEvent(event);

        switch (event.getAction()){
            case(MotionEvent.ACTION_DOWN):{
                float x = event.getX();
                float y = event.getY();

                mCircleX = x;
                mCircleY = y;

                postInvalidate();

                return true;

            }

            case(MotionEvent.ACTION_MOVE):{
                float x = event.getX();
                float y = event.getY();

                double dx = Math.pow(x - mCircleX, 2);
                double dy = Math.pow(y - mCircleY, 2);

                if (dx + dy < Math.pow(mCircleRadius, 2)) {
                    mCircleX = x;
                    mCircleY = y;

                    postInvalidate();
                    return true;
                }

                return true;
            }
        }

        return value;

    }
}
