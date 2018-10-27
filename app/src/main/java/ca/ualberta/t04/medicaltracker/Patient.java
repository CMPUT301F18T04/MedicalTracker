package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;

public class Patient extends User
{
    private ArrayList<Problem> problems = null;
    private ArrayList<Doctor> doctors = null;

    public Patient(String userName, String password) {
        super(userName, password, false);
        if(problems==null)
            problems = new ArrayList<>();
        if(doctors==null)
            doctors = new ArrayList<>();
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public void addProblem(Problem problem) {
        problems.add(problem);
    }

    public void removeProblem(Problem problem) {
        problems.remove(problem);
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
    }
}
