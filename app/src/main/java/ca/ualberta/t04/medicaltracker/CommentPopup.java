package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class displays a pop up window for the doctor to add a comment
 */

public class CommentPopup {
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

    public void addComment() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.activity_doctor_add_comment, null);

        final EditText editComment = (EditText) promptView.findViewById(R.id.addCommentEditText);

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Add a new comment")
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
                        Toast.makeText(context,"Successfully added a new comment",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ad.show();
    }
}
