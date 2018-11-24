package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;

/**
 * This class displays a pop up window for the doctor to add a comment
 */

public class CommentPopup {

    // Initialize everything

    private Context context;
    private RecordList recordList;
    private Record record;
    private Doctor doctor;

    public CommentPopup(Context context, RecordList recordList, Record record, Doctor doctor) {
        this.context = context;
        this.recordList = recordList;
        this.record = record;
        this.doctor = doctor;
    }

    // The method that shows the alert dialogue for commenting
    public void addComment() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.activity_doctor_add_comment, null);

        final EditText editComment = promptView.findViewById(R.id.addCommentEditText);

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle(R.string.doctor_comment_title)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okButton = ad.getButton(AlertDialog.BUTTON_POSITIVE);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recordList.addComment(record, doctor, editComment.getText().toString());
                        ad.dismiss();
                        Toast.makeText(context,R.string.comment_toast1,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ad.show();
    }
}
