package ca.ualberta.t04.medicaltracker.Util;

import android.os.Build;

import java.util.UUID;

/**
 * Stores the utils used throughout the project
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class CommonUtil {
    public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static String INDEX_NAME = "cmput301f18t04";
    public static int QR_CODE_HEIGHT = 500;
    public static float LOCATION_DISTANCE = 2000;

    // Idea comes from https://blog.csdn.net/a360940265a/article/details/79907844

    /**
     * Gets the device id
     * @return String
     */
    public static String getIMEI() {
        String serial;

        String m_szDevIDShort = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +

                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +

                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +

                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +

                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +

                Build.TAGS.length()%10 + Build.TYPE.length()%10 +

                Build.USER.length()%10 ;

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            // API>=9 Use serial to be the unique ID
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // Init serial
            serial = "serial";
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
