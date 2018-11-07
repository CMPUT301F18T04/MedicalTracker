package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;

public class Doctor extends User
{
    private ArrayList<Patient> patients;

    public Doctor(String userName, String password) {
        super(userName, password, true);
        if(patients == null)
            patients = new ArrayList<>();
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        notifyAllListeners();
    }

    public void removePatient(Patient patient){
        patients.remove(patient);
        notifyAllListeners();
    }
}
