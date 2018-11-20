package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
   This class represents a custom image adapter
 */

public class ImageAdapter extends android.support.v4.view.PagerAdapter {

    private Context mContext;
    private int[] imageID;
    public ImageAdapter(Context context, int[] imageID) {
        this.mContext = context;
        this.imageID = imageID;
    }

    // returns the number of images
    @Override
    public int getCount() {
        return imageID.length;
    }

    // returns a boolean
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imageID[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
