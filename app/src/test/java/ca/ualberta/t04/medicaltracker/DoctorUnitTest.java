package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DoctorUnitTest {
    //This method is used to test Doctor class
    @Test
    public void doctor_test(){
        //constructor
        Doctor doctor = new Doctor("Doctor1","12345");
        assertTrue("UserName should be 'Test'", doctor.getUserName().equals("Doctor1"));
        assertTrue("password should be '1234'", doctor.getPassword().equals("12345"));
        assertTrue("The user should be a patient", doctor.isDoctor().equals(true));

        //addPatient, removePatient, getPatients tests
        Patient patient = new Patient("Patient","1234");
        Patient patient1 = new Patient("Patient1","123");
        doctor.addPatient(patient);
        assertEquals(doctor.getPatients().get(0),patient);
        doctor.addPatient(patient1);
        doctor.removePatient(patient);
        assertNotEquals(doctor.getPatients().get(0),patient);
    }
}
