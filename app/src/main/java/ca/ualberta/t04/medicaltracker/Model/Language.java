package ca.ualberta.t04.medicaltracker.Model;


/**
 * This class contains all language related attributes and functions
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

public class Language {
    public int resId;
    public String countryName;

    public Language(int resId, String countryName) {
        this.resId = resId;
        this.countryName = countryName;
    }
}
