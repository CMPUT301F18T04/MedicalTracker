package ca.ualberta.t04.medicaltracker.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import ca.ualberta.t04.medicaltracker.Adapter.ImageAdapter;
import ca.ualberta.t04.medicaltracker.R;

public class SlideShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        // Hide the action bar
        getSupportActionBar().hide();
        // Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set adapter to ViewPager
        ViewPager viewPager = findViewById(R.id.viewPager);
        int[] resourceId = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};
        final String[] descriptions = {"background", "foreground"};

        ImageAdapter imageAdapter = new ImageAdapter(this, resourceId);
        viewPager.setAdapter(imageAdapter);

        final TextView textView = findViewById(R.id.image_description);
        textView.setText(descriptions[0]);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateImageDescription(textView, descriptions[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateImageDescription(TextView textView, String content)
    {
        textView.setText(content);
    }

}
