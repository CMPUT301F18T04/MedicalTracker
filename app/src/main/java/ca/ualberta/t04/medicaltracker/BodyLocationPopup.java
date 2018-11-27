package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.R;

/**
 * This class displays a pop up window for the doctor to add a comment
 */

public class BodyLocationPopup {

    // Initialize everything

    private Context context;
    private BodyLocation bodyLocation;
    private TextView textView;

    public BodyLocationPopup(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    // The method that shows the alert dialogue for commenting
    public BodyLocation chooseBodyLocation() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.body_location_popup, null);

        Button head = promptView.findViewById(R.id.body_location_head);
        Button leftArm = promptView.findViewById(R.id.body_location_left_arm);
        Button rightArm = promptView.findViewById(R.id.body_location_right_arm);
        Button torso = promptView.findViewById(R.id.body_location_torso);
        Button leftLeg = promptView.findViewById(R.id.body_location_left_leg);
        Button rightLeg = promptView.findViewById(R.id.body_location_right_leg);

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setView(promptView)
                .create();

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.Head, ad);
            }
        });

        leftArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.LeftArm, ad);
            }
        });

        rightArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.RightArm, ad);
            }
        });

        torso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.Torso, ad);
            }
        });

        leftLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.LeftLeg, ad);
            }
        });

        rightLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.RightLeg, ad);
            }
        });
        ad.show();
        return this.bodyLocation;
    }

    private void assignBodyLocation(BodyLocation bodyLocation, AlertDialog ad){
        this.bodyLocation = bodyLocation;
        textView.setText(bodyLocation.name());
        ad.dismiss();
    }

    public BodyLocation getBodyLocation() {
        return bodyLocation;
    }
}
