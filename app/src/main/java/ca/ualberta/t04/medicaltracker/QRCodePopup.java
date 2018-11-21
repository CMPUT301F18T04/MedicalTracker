package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * This class displays a pop up window for the doctor to add a comment
 */

public class QRCodePopup {

    // Initialize everything

    private Context context;
    private String userName;
    private int QR_CODE_HIGHT = Util.QR_CODE_HEIGHT;

    public QRCodePopup(Context context, String userName) {
        this.context = context;
        this.userName = userName;
    }

    // The method that shows the alert dialogue for commenting
    public void showQRCode() throws WriterException {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.qr_code, null);

        final ImageView qr_code = promptView.findViewById(R.id.qr_code);

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setView(promptView)
                .create();
        Bitmap bitmap = TextToImageEncode(context, userName);
        qr_code.setImageBitmap(bitmap);
        //Bitmap qrCode = CodeCreator.createQRCode(userName, 400, 400, null);
        //qr_code.setImageBitmap(qrCode);
        ad.show();
    }

    // Reference from https://demonuts.com/generate-qr-code/
    private Bitmap TextToImageEncode(Context context, String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QR_CODE_HIGHT, QR_CODE_HIGHT, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        context.getResources().getColor(R.color.black):context.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, QR_CODE_HIGHT, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
