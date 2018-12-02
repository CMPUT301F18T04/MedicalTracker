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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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
    private TextView frontBack;
    private ArrayList<String> titles;
    private String activity;
    private Button delete;

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        // Determine to hide the "Delete" button or display the record title
        titleView = findViewById(R.id.albumText);
        delete = findViewById(R.id.slide_show_button_delete);
        frontBack = findViewById(R.id.slide_show_front_back);

        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        if(activity != null){
            titles = intent.getStringArrayListExtra("Titles");
            titleView.setText(titles.get(0));
            delete.setVisibility(View.GONE);
        }
        else{
            titleView.setVisibility(View.GONE);
        }

        // Hide the action bar
        getSupportActionBar().hide();
        // Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set adapter to ViewPager
        final ViewPager viewPager = findViewById(R.id.viewPager);

        final ArrayList<Bitmap> bitmaps = BitmapHolder.getBitmaps();
        final ArrayList<Boolean> frontBackArrayList = BitmapHolder.getFrontBackArrayList();

        frontBack.setText(getFrontBackMessage(frontBackArrayList.get(0)));

        final ImageAdapter imageAdapter = new ImageAdapter(this, bitmaps);
        viewPager.setAdapter(imageAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                if(activity != null) {
                    titleView.setText(titles.get(position));
                }
                frontBack.setText(getFrontBackMessage(frontBackArrayList.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Button delete = findViewById(R.id.slide_show_button_delete);
        if(DataController.getUser().isDoctor()){
            delete.setVisibility(View.GONE);
        } else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bitmaps.remove(currentIndex);
                    frontBackArrayList.remove(currentIndex);
                    returnResult();

                    if(bitmaps.isEmpty()){
                        finish();
                    }

                    imageAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * Returns the results
     */
    public void returnResult() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
    }

    // Get the message of front/back body-location
    private String getFrontBackMessage(Boolean isBack){
        if(isBack)
            return getString(R.string.slide_show_back);
        else
            return getString(R.string.slide_show_front);
    }
}