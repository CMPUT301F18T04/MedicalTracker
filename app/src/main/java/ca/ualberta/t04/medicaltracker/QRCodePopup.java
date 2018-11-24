package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import ca.ualberta.t04.medicaltracker.Util.CommonUtil;
import ca.ualberta.t04.medicaltracker.Util.QRCodeUtil;

/**
 * This class displays a pop up window for the doctor to add a comment
 */

public class QRCodePopup {

    // Initialize everything

    private Context context;
    private String userName;
    private static int QR_CODE_HEIGHT = CommonUtil.QR_CODE_HEIGHT;

    public QRCodePopup(Context context, String userName) {
        this.context = context;
        this.userName = userName;
    }

    // The method that shows the alert dialogue for commenting
    public void showQRCode() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.qr_code, null);

        final ImageView qr_code = promptView.findViewById(R.id.qr_code);

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setView(promptView)
                .create();

        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(userName, QR_CODE_HEIGHT, QR_CODE_HEIGHT);
        qr_code.setImageBitmap(bitmap);

        ad.show();
    }
}
