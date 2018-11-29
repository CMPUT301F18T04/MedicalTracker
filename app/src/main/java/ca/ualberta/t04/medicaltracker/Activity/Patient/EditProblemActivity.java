package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.ProblemList;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

public class EditProblemActivity extends AppCompatActivity{

    private DatePickerDialog.OnDateSetListener problemDateSetListener;
    private TextView problemDate;
    private EditText problemTitle,problemDescription;
    private Button editButton;
    private ProblemList mProblemList;
    private Problem mProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_problem);

        //vars
        problemDate = findViewById(R.id.edit_problem_date_pick_view);
        problemTitle = findViewById(R.id.edit_problem_title_text);
        problemDescription = findViewById(R.id.edit_problem_description_text);
        editButton = findViewById(R.id.edit_problem_button_edit);
        Intent mIntent = getIntent();
        int problemIndex = mIntent.getIntExtra("problem_index", -1);
        mProblemList = DataController.getPatient().getProblemList();
        mProblem = mProblemList.getProblem(problemIndex);

        problemTitle.setText(mProblem.getTitle());
        problemDescription.setText(mProblem.getDescription());
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        String date = cal.get(Calendar.YEAR) +"-"+ month +"-" + cal.get(Calendar.DAY_OF_MONTH);
        problemDate.setText(date);
        init();
    }

    public void init(){
        problemDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditProblemActivity.this, android.R.style.ThemeOverlay_Material_Dialog,
                        problemDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        // problemDateSetListener gets the result of the DatePickerDialog
        // TextView problem_date will be set with the date the user picked
        // date format: year + "-" + month + "-" + day
        problemDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                problemDate.setText(date);
            }
        };

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProblem();
            }
        });
    }

    // Used to edit a problem
    public void editProblem(){

        // check if the title and description are both filled
        if(problemTitle.getText().toString().equals("") || problemDescription.getText().toString().equals("")){
            Toast.makeText(EditProblemActivity.this, "The title/description cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // init date is now
        Date dateStart = new Date();

        // if the date that the user inputs is not correct, then use the default date
        SimpleDateFormat format = new SimpleDateFormat(CommonUtil.DATE_FORMAT, Locale.getDefault());
        try {
            dateStart = format.parse(problemDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // use dataController to notify the change of problem
        mProblemList.setTitle(mProblem, problemTitle.getText().toString());
        mProblemList.setDescription(mProblem, problemDescription.getText().toString());
        mProblemList.setDateStart(mProblem, dateStart);

        // notification message
        Toast.makeText(EditProblemActivity.this, R.string.edit_problem_toast, Toast.LENGTH_SHORT).show();
        finish();
    }

}
