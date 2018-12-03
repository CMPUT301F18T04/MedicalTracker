package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import ca.ualberta.t04.medicaltracker.Model.Language;

import static junit.framework.Assert.assertEquals;

public class LanguageUnitTest {
    @Test
    public void language_test(){
        Language language = new Language(1, "ca");

        assertEquals(1,language.resId);
        assertEquals("ca", language.countryName);
    }
}
