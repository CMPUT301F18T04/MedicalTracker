package ca.ualberta.t04.medicaltracker.Model;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

/**
 * This class contains all attributes and functionality for a patient user of the application
 * Extended from User class
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04
 * @see User
 * @since 1.0
 */

public class Patient extends User
{
    private ProblemList problemList = null;
    // We only save doctors' username in database, because in this way, we can reduce the complex of the data
    private ArrayList<String> doctorsUserNames;
    private transient ArrayList<Doctor> doctors = null;
    private ArrayList<String> notifyDoctors;


    public Patient(String userName) {
        super(userName, false);
        if(problemList==null)
            problemList = new ProblemList();
        if(doctors==null)
            doctors = new ArrayList<>();
        if(doctorsUserNames == null)
            doctorsUserNames = new ArrayList<>();
        if(notifyDoctors==null)
            notifyDoctors = new ArrayList<>();
    }

    /**
     * Used to keep all doctors' information the newest
     * @param doctorsUserNames ArrayList<String>
     * @return ArrayList<Doctor> updatedDoctors
     */
    private ArrayList<Doctor> updateDoctor(ArrayList<String> doctorsUserNames){
        if(doctorsUserNames==null)
            return new ArrayList<>();
        ArrayList<Doctor> updatedDoctors = new ArrayList<>();
        for(String userName:doctorsUserNames){
            updatedDoctors.add((Doctor) ElasticSearchController.searchUser(userName));
        }
        return updatedDoctors;
    }

    /**
     * Gets the problem list of the patient
     * @return ProblemList problemList
     */
    public ProblemList getProblemList() {
        return problemList;
    }

    /**
     * Gets the Doctor list of the patient
     * @return ArrayList<Doctor> doctors
     */
    public ArrayList<Doctor> getDoctors() {
        doctors = updateDoctor(doctorsUserNames);
        return doctors;
    }

    /**
     * Gets the doctors' username list
     * @return ArrayList<String> doctorsUserNames
     */
    public ArrayList<String> getDoctorsUserNames() {
        return doctorsUserNames;
    }

    /**
     * Adds a doctor for the patient
     * @param doctor Doctor
     */
    public void addDoctor(Doctor doctor) {
        if(doctorsUserNames==null)
            doctorsUserNames = new ArrayList<>();
        if(!doctorsUserNames.contains(doctor.getUserName())){
            doctorsUserNames.add(doctor.getUserName());
            notifyAllListeners();
            if(notifyDoctors==null)
                notifyDoctors = new ArrayList<>();
            notifyDoctors.add(doctor.getUserName());
        }
    }

    /**
     * Removes a doctor from the patient
     * @param doctor Doctor
     */
    public void removeDoctor(Doctor doctor) {
        if(doctorsUserNames.contains(doctor.getUserName())){
            doctorsUserNames.remove(doctor.getUserName());
            notifyAllListeners();
            notifyDoctors.remove(doctor.getUserName());
        }
    }

    /**
     * Gets the notify doctor list
     * @return ArrayList<String> notifyDoctors
     */
    public ArrayList<String> getNotifyDoctors() {
        return notifyDoctors;
    }

    /**
     * Clears the notify doctor list
     */
    public void clearNotifyDoctors(){
        notifyDoctors.clear();
        notifyAllListeners();
    }
}
