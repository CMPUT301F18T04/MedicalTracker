package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.SearchType;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

/*
 This class represents a custom adapter for search results
 */

public class SearchResultAdapter extends ArrayAdapter {

    private int recourseId;
    private SearchType searchType;

    public SearchResultAdapter(@NonNull Context context, int resource, @NonNull List<Object[]> objects, SearchType searchType) {
        super(context, resource, objects);
        recourseId = resource;
        this.searchType = searchType;
    }

    // returns the custom view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);

        TextView username = view.findViewById(R.id.search_result_list_username);
        TextView title = view.findViewById(R.id.search_result_list_title);
        TextView date = view.findViewById(R.id.search_result_list_date);
        TextView description = view.findViewById(R.id.search_result_list_description);
        ImageView imageView = view.findViewById(R.id.search_result_imageView);

        Object[] object = (Object[]) getItem(position);

        fillData(username, title, date, description, imageView, object);

        return view;
    }

    private void fillData(TextView username, TextView title, TextView date, TextView description, ImageView imageView, Object[] object){
        SimpleDateFormat format = new SimpleDateFormat(CommonUtil.DATE_FORMAT, Locale.getDefault());
        String userName = (String) object[0];
        username.setText(userName);
        if(searchType.equals(SearchType.Problem)){
            Problem problem = (Problem) object[1];
            imageView.setImageResource(R.mipmap.problem);

            title.setText(problem.getTitle() + " (" + problem.getRecordList().getRecords().size() + " " + getContext().getString(R.string.patient_inner_text_record) + ")");
            date.setText(format.format(problem.getTime()));

            String description_text = problem.getDescription();
            if(description_text.length()<20){
                description.setText(getContext().getText(R.string.patient_page_description) + description_text.substring(0));
            } else {
                description.setText(getContext().getText(R.string.patient_page_description)+ problem.getDescription().substring(0, 20) + "...");
            }
        }
        else if(searchType.equals(SearchType.Record)){
            Record record = (Record) object[1];
            imageView.setImageResource(R.mipmap.record);

            title.setText(record.getTitle());
            date.setText(format.format(record.getDateStart()));

            String description_text = record.getDescription();
            if(description_text.length()<20){
                description.setText(getContext().getText(R.string.record_history_description) + description_text.substring(0));
            } else {
                description.setText(getContext().getText(R.string.record_history_description)+ record.getDescription().substring(0, 20) + "...");
            }
        }
    }
}
