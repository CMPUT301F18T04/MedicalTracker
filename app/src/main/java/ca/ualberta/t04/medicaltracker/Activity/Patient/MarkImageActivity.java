package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.R;

public class MarkImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_image);



        Bitmap bitmap = getIntent().getParcelableExtra("image");

        ImageView imageView = findViewById(R.id.mark_image_view);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);



        Intent intent = new Intent();
        intent.putExtra("data", bitmap);
        setResult(RESULT_OK, intent);

        getCoordinate();
    }

    private void getCoordinate(){

        ImageView imageView = findViewById(R.id.mark_image_view);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                float x = motionEvent.getX();
                float y = motionEvent.getY();

                String message = String.format("Coordinate: (%.2%, %.2%)", x, y);

//                Toast.makeText(MarkImageActivity.this, message, Toast.LENGTH_SHORT).show();
//                Log.d("Coordinate", message);

                return false;
            }
        });
    }

}
