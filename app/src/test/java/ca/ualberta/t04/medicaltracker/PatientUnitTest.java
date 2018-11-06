package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PatientUnitTest {
    @Test
    public void patient_test(){

        //Constructor test
        Patient patient = new Patient("Test","123");
        assertTrue("UserName should be 'Test'", patient.getUserName().equals("Test"));
        assertTrue("password should be '123'", patient.getPassword().equals("123"));
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
        Doctor doctor = new Doctor("Doctor","12345");
        Doctor doctor1 = new Doctor("Doctor1","1234");
        patient.addDoctor(doctor);
        assertEquals(patient.getDoctors().get(0),doctor);
        patient.addDoctor(doctor1);
        patient.removeDoctor(doctor);
        assertNotEquals(patient.getDoctors().get(0),doctor);
    }

}
