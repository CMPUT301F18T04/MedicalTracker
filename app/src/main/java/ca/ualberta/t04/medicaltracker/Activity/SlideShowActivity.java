package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.ImageAdapter;
import ca.ualberta.t04.medicaltracker.BitmapHolder;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.R;

/**
 * This activity is for displaying all the photos in a slideshow format
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for displaying all the photos in a slideshow format
 */

public class SlideShowActivity extends AppCompatActivity {

    private int currentIndex;
    private TextView titleView;
    private ArrayList<String> titles;
    private String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        // Determine to hide the "Delete" button or display the record title
        //try{
        //    Intent intent = getIntent();
        //    activity= intent.getStringExtra("activity");
        //    Button delete = findViewById(R.id.slide_show_button_delete);
        //    delete.setVisibility(View.GONE);
        //    titleView = findViewById(R.id.albumText);
        //    titles = intent.getStringArrayListExtra("Titles");
        //}
        //catch (Exception e){
        //    titleView.setVisibility(View.GONE);
        //}

        // Hide the action bar
        getSupportActionBar().hide();
        // Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set adapter to ViewPager
        final ViewPager viewPager = findViewById(R.id.viewPager);

        final ArrayList<Bitmap> bitmaps = BitmapHolder.getBitmaps();

        final ImageAdapter imageAdapter = new ImageAdapter(this, bitmaps);
        viewPager.setAdapter(imageAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //if(activity.equals("album")) {
                //    titleView.setText(titles.get(position));
                //}
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Button delete = findViewById(R.id.slide_show_button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmaps.remove(currentIndex);
                returnResult();

                if(bitmaps.isEmpty()){
                    finish();
                }

                imageAdapter.notifyDataSetChanged();
            }
        });
    }

    public void returnResult() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
    }
}