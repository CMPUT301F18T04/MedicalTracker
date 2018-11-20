package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.R;

/*
  This class represents a custom adapter for displaying the doctor list
 */

public class DoctorListAdapter extends ArrayAdapter{
    private int resourceId;
    private List<Doctor> doctors;

    public DoctorListAdapter(@NonNull Context context, int resource, @NonNull List<Doctor> doctors) {
        super(context, resource, doctors);
        resourceId = resource;
        this.doctors = doctors;
    }

    // returns the custom view
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Doctor doctor = (Doctor) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView userName = view.findViewById(R.id.doctor_username);

        userName.setText(doctor.getUserName());

        return view;
    }
}
