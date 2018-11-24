package ca.ualberta.t04.medicaltracker.Model;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

/**
 * This class contains all attributes and functionality for a doctor user of the application
 * Extended from User class
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04
 * @see User
 * @since 1.0
 */

public class Doctor extends User
{
    // We only save patients' username in database, because in this way, we can reduce the complex of the data
    private ArrayList<String> patientsUserNames;
    private transient ArrayList<Patient> patients;

    public Doctor(String userName, String password) {
        super(userName, password, true);
        if(patients == null)
            patients = new ArrayList<>();
        if(patientsUserNames == null)
            patientsUserNames = new ArrayList<>();
    }

    /**
     * Gets the patient list of the doctor
     * @return ArrayList<Patient> patients
     */
    public ArrayList<Patient> getPatients() {
        patients = updatePatient(patientsUserNames);
        return patients;
    }

    /**
     * Used to keep all patients' information up-to-date
     * @param patientsUserNames ArrayList<String>
     * @return ArrayList<Patient> updatePatients
     */
    private ArrayList<Patient> updatePatient(ArrayList<String> patientsUserNames){
        if(patientsUserNames==null)
            return new ArrayList<>();
        ArrayList<Patient> updatedPatients = new ArrayList<>();
        for(String userName:patientsUserNames){
            updatedPatients.add((Patient) ElasticSearchController.searchUser(userName));
        }
        return updatedPatients;
    }

    /**
     * Gets the patient's username list
     * @return ArrayList<String> patientUserNames
     */
    public ArrayList<String> getPatientsUserNames() {
        return patientsUserNames;
    }

    /**
     * Adds a patient for the doctor
     * @param patient Patient
     */
    public void addPatient(Patient patient) {
        if(!patientsUserNames.contains(patient.getUserName())){
            patientsUserNames.add(patient.getUserName());
            notifyAllListeners();
        }
    }

    /**
     * Removes a patient of the doctor
     * @param patient Patient
     */
    public void removePatient(Patient patient){
        if(patientsUserNames.contains(patient.getUserName())){
            patientsUserNames.remove(patient.getUserName());
            notifyAllListeners();
        }
    }
}
