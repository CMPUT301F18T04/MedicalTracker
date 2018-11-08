package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

public class Doctor extends User
{
    private ArrayList<String> patientsUserNames;
    private transient ArrayList<Patient> patients;

    public Doctor(String userName, String password) {
        super(userName, password, true);
        if(patients == null)
            patients = new ArrayList<>();
        if(patientsUserNames == null)
            patientsUserNames = new ArrayList<>();
    }

    public ArrayList<Patient> getPatients() {
        patients = updatePatient(patientsUserNames);
        return patients;
    }

    private ArrayList<Patient> updatePatient(ArrayList<String> patientsUserNames){
        if(patientsUserNames==null)
            return new ArrayList<>();
        ArrayList<Patient> updatedPatients = new ArrayList<>();
        for(String userName:patientsUserNames){
            updatedPatients.add((Patient) ElasticSearchController.searchUser(userName));
        }
        return updatedPatients;
    }

    public ArrayList<String> getPatientsUserNames() {
        return patientsUserNames;
    }

    public void addPatient(Patient patient) {
        if(!patientsUserNames.contains(patient.getUserName())){
            patientsUserNames.add(patient.getUserName());
            notifyAllListeners();
        }
    }

    public void removePatient(Patient patient){
        if(patientsUserNames.contains(patient.getUserName())){
            patientsUserNames.remove(patient.getUserName());
            notifyAllListeners();
        }
    }
}
