package ca.ualberta.t04.medicaltracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.R;

/**
 * This class represents a custom adapter for search patient results
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */


/*
  This class represents a custom adapter for search patient results
 */

public class SearchPatientAdapter extends ArrayAdapter {

    private int recourseId;
    private List<Patient> patients;

    public SearchPatientAdapter(@NonNull Context context, int resource, @NonNull List<Patient> patients) {
        super(context, resource, patients);
        recourseId = resource;
        this.patients = patients;
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
        final Patient patient = (Patient) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(recourseId, null);

        TextView userName = view.findViewById(R.id.search_patient_username);
        TextView detail = view.findViewById(R.id.search_patient_detail);

        userName.setText(patient.getUserName());
        String detail_text = getContext().getString(R.string.add_patient_detail);
        detail_text = detail_text.replace("%d", String.valueOf(patient.getProblemList().getProblems().size()));
        detail.setText(detail_text);

        // Allow a doctor to add a patient
        Button button = view.findViewById(R.id.search_patient_button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataController.getDoctor().addPatient(patient);
                patients.remove(patient);
                notifyDataSetChanged();

                patient.addDoctor(DataController.getDoctor());
                ElasticSearchController.updateUser(patient);

                Toast.makeText(getContext(), "Added a patient", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
