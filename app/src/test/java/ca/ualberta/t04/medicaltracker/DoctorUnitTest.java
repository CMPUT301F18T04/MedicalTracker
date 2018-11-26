package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DoctorUnitTest {
    //This method is used to test Doctor class
    @Test
    public void doctor_test(){
        //constructor
        Doctor doctor = new Doctor("Doctor1");
        assertTrue("UserName should be 'Test'", doctor.getUserName().equals("Doctor1"));
        assertTrue("The user should be a patient", doctor.isDoctor().equals(true));

        //addPatient, removePatient, getPatients tests
        Patient patient = new Patient("Patient");
        Patient patient1 = new Patient("Patient1");
        doctor.addPatient(patient);
        assertEquals(doctor.getPatientsUserNames().get(0),patient.getUserName());
        doctor.addPatient(patient1);
        doctor.removePatient(patient);
        assertNotEquals(doctor.getPatientsUserNames().get(0),patient.getUserName());
    }
}
