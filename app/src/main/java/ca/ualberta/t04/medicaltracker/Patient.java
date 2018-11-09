package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

public class Patient extends User
{
    private ProblemList problemList = null;
    // We only save doctors' username in database, because in this way, we can reduce the complex of the data
    private ArrayList<String> doctorsUserNames;
    private transient ArrayList<Doctor> doctors = null;
    private ArrayList<String> notifyDoctors;

    public Patient(String userName, String password) {
        super(userName, password, false);
        if(problemList==null)
            problemList = new ProblemList();
        if(doctors==null)
            doctors = new ArrayList<>();
        if(doctorsUserNames == null)
            doctorsUserNames = new ArrayList<>();
        if(notifyDoctors==null)
            notifyDoctors = new ArrayList<>();
    }

    // Used to keep all doctors' information newest
    private ArrayList<Doctor> updateDoctor(ArrayList<String> doctorsUserNames){
        if(doctorsUserNames==null)
            return new ArrayList<>();
        ArrayList<Doctor> updatedDoctors = new ArrayList<>();
        for(String userName:doctorsUserNames){
            updatedDoctors.add((Doctor) ElasticSearchController.searchUser(userName));
        }
        return updatedDoctors;
    }

    public ProblemList getProblemList() {
        return problemList;
    }

    public ArrayList<Doctor> getDoctors() {
        doctors = updateDoctor(doctorsUserNames);
        return doctors;
    }

    public ArrayList<String> getDoctorsUserNames() {
        return doctorsUserNames;
    }

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

    public void removeDoctor(Doctor doctor) {
        if(doctorsUserNames.contains(doctor.getUserName())){
            doctorsUserNames.remove(doctor.getUserName());
            notifyAllListeners();
            notifyDoctors.remove(doctor.getUserName());
        }
    }

    public ArrayList<String> getNotifyDoctors() {
        return notifyDoctors;
    }

    public void clearNotifyDoctors(){
        notifyDoctors.clear();
        notifyAllListeners();
    }
}
