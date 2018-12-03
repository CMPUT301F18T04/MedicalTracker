package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class BitmapHolderUnitTest {
    @Test
    public void bitmapHolder_test(){
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        ArrayList<Boolean> frontBack = new ArrayList<>();

        BitmapHolder.setFrontBackArrayList(frontBack);
        BitmapHolder.setBitmaps(bitmaps);

        assertEquals(bitmaps, BitmapHolder.getBitmaps());
        assertEquals(frontBack, BitmapHolder.getFrontBackArrayList());

    }
}
