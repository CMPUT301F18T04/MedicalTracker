package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;

public class Patient extends User
{
    private ProblemList problemList = null;
    private ArrayList<Doctor> doctors = null;

    public Patient(String userName, String password) {
        super(userName, password, false);
        if(problemList==null)
            problemList = new ProblemList();
        if(doctors==null)
            doctors = new ArrayList<>();
    }

    public ProblemList getProblemList() {
        return problemList;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        notifyAllListeners();
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
        notifyAllListeners();
    }
}
