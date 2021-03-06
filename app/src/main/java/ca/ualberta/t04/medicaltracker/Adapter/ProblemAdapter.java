package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

/**
 * This class represents a custom adapter for displaying problems
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This class represents a custom adapter for displaying problems
 */
public class ProblemAdapter extends ArrayAdapter {

    private int recourseId;

    public ProblemAdapter(@NonNull Context context, int resource, @NonNull List<Problem> problems) {
        super(context, resource, problems);
        recourseId = resource;
    }

    /**
     * returns the custom view
     * @param position int
     * @param convertView View
     * @param parent ViewGroup
     * @return view View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Problem problem = (Problem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);

        TextView title = view.findViewById(R.id.problem_list_title);
        TextView date = view.findViewById(R.id.problem_list_date);
        TextView description = view.findViewById(R.id.problem_list_description);
        //description.setMovementMethod(new ScrollingMovementMethod());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        title.setText(problem.getTitle() + " (" + problem.getRecordList().getRecordIds().size() + " " + getContext().getString(R.string.patient_inner_text_record) + ")");
        date.setText(format.format(problem.getTime()));

        String description_text = problem.getDescription();
        if(description_text.length()<40){
            description.setText(getContext().getText(R.string.patient_page_description) + description_text.substring(0));
        } else {
            description.setText(getContext().getText(R.string.patient_page_description)+ problem.getDescription().substring(0, 40) + "...");
        }

        return view;
    }
}
