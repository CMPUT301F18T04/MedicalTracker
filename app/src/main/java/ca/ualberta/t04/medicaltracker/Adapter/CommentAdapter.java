package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.R;

/*
  This class represents a custom adapter for displaying the doctor list
 */

public class CommentAdapter extends ArrayAdapter{
    private int resourceId;
    private ArrayList<String> comments;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> comments) {
        super(context, resource, comments);
        resourceId = resource;
        this.comments = comments;
    }

    // returns the custom view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String comment = (String) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView userName = view.findViewById(R.id.doctor_comment_text_view);

        userName.setText(comment);

        return view;
    }

    @Override
    public int getCount() {
        return comments.size();
    }
}
