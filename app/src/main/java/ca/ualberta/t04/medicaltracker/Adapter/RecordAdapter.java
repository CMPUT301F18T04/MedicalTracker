package ca.ualberta.t04.medicaltracker.Adapter;

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

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

/*
  This class represents a custom adapter for displaying records
 */

public class RecordAdapter extends ArrayAdapter {

    private int recourseId;

    public RecordAdapter(@NonNull Context context, int resource, @NonNull List<Record> objects) {
        super(context, resource, objects);
        recourseId = resource;
    }

    // returns the custom view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Record record = (Record) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);

        TextView title = view.findViewById(R.id.record_list_title);
        TextView date = view.findViewById(R.id.record_list_date);
        TextView description = view.findViewById(R.id.record_list_description);

        //SimpleDateFormat format = new SimpleDateFormat(CommonUtil.TIME_FORMAT, Locale.getDefault());
        SimpleDateFormat format = new SimpleDateFormat(CommonUtil.DATE_FORMAT, Locale.getDefault());

        title.setText(record.getTitle());
        date.setText(format.format(record.getDateStart()));

        String description_text = record.getDescription();
        if(description_text.length()<40){
            description.setText(getContext().getText(R.string.record_history_description) + description_text.substring(0));
        } else {
            description.setText(getContext().getText(R.string.record_history_description) + record.getDescription().substring(0, 40) + "...");
        }

        return view;
    }
}
