package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.ImageAdapter;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.Util;
import id.zelory.compressor.Compressor;

/*
  This activity is for displaying all the photos in a slideshow format
 */

public class SlideShowActivity extends AppCompatActivity {

    private int currentIndex;
    private ArrayList<String> paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        // Hide the action bar
        getSupportActionBar().hide();
        // Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set adapter to ViewPager
        final ViewPager viewPager = findViewById(R.id.viewPager);

        /*
        Bundle bundle = getIntent().getExtras();

        ArrayList byteBitmaps = bundle.getParcelableArrayList("image");

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for(Object object:byteBitmaps){
            byte[] bytes = (byte[]) object;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            bitmaps.add(bitmap);
        }
        */
        final ArrayList<Bitmap> bitmaps = new ArrayList<>();
        paths = getIntent().getStringArrayListExtra("image");

        for(String path:paths){
            try {
                Bitmap bitmap = Util.compressImageFile(this, path);
                bitmaps.add(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final ImageAdapter imageAdapter = new ImageAdapter(this, bitmaps);
        viewPager.setAdapter(imageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
                paths.remove(currentIndex);
                returnResult();
                if(bitmaps.isEmpty()){
                    finish();
                }

                viewPager.removeViewAt(currentIndex);
                imageAdapter.notifyDataSetChanged();
            }
        });
    }

    public void returnResult() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", paths);
        setResult(RESULT_OK, intent);
    }
}
