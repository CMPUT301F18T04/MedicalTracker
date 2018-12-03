package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class PhotoUnitTest {
    @Test
    public void photo_test(){
        String path = "test/test";
        Photo photo = new Photo("aaaaa", path);

        assertEquals("aaaaa", photo.getBase64Bitmap());

        assertEquals(path, photo.getPath());

        photo.setPath("ccccc");
        assertEquals("ccccc", photo.getPath());

        photo.setBase64Bitmap("bbbbb");
        assertEquals("bbbbb", photo.getBase64Bitmap());

        assertFalse(photo.isBack());
        photo.setBackStatus(true);
        assertTrue(photo.isBack());
    }
}
