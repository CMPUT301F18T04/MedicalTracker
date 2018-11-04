package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProblemAdapter extends ArrayAdapter {

    private int recourseId;

    public ProblemAdapter(@NonNull Context context, int resource, @NonNull List<Problem> problems) {
        super(context, resource, problems);
        recourseId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Problem problem = (Problem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);

        TextView title = view.findViewById(R.id.problem_list_title);
        TextView date = view.findViewById(R.id.problem_list_date);
        TextView description = view.findViewById(R.id.problem_list_description);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        title.setText(problem.getTitle() + "(" + problem.getRecords().size() + " " + R.string.patient_inner_text_record + ")");
        date.setText(format.format(problem.getTime()));

        String description_text = problem.getDescription();
        if(description.length()<30){
            description.setText(description_text);
        } else {
            description.setText(problem.getDescription().substring(0, 30) + "...");
        }

        return view;
    }
}
