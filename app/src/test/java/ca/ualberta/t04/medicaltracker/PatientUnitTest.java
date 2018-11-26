package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import java.util.Date;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PatientUnitTest {
    @Test
    public void patient_test(){

        //Constructor test
        Patient patient = new Patient("Test");
        assertTrue("UserName should be 'Test'", patient.getUserName().equals("Test"));
        assertTrue("The user should be a patient", patient.isDoctor().equals(false));

        //addProblem, removeProblem, getProblems tests
        Problem problem = new Problem("Dermatophytosis",new Date(), "Skin rash, itch");
        Problem problem1 = new Problem("Urticaria",new Date(),"Extremely itch");
        patient.getProblemList().addProblem(problem);
        assertEquals(patient.getProblemList().getProblems().get(0),problem);
        patient.getProblemList().addProblem(problem1);
        patient.getProblemList().removeProblem(problem);
        assertNotEquals(patient.getProblemList().getProblems().get(0),problem);

        //addDoctor, removeDoctor, getDoctors
        Doctor doctor = new Doctor("Doctor");
        Doctor doctor1 = new Doctor("Doctor1");
        patient.addDoctor(doctor);
        assertEquals(patient.getDoctorsUserNames().get(0),doctor.getUserName());
        patient.addDoctor(doctor1);
        patient.removeDoctor(doctor);
        assertNotEquals(patient.getDoctorsUserNames().get(0),doctor.getUserName());
    }

}
