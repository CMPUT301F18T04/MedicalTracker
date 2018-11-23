package ca.ualberta.t04.medicaltracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * This class contains all attributes and functionality for a general user of the application
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class User
{
    private String userName;
    private String name;
    private transient HashMap<String, Listener> listeners = new HashMap<>();
    private Boolean isDoctor;
    private Date birthday;
    private String email;
    private Boolean isMale;
    private String phoneNumber;
    private String address;

    public User(String userName, Boolean isDoctor)
    {
        this.userName = userName;
        this.isDoctor = isDoctor;
    }

    /**
     * Gets the user's username
     * @return String username
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Set the user's username
     * @param newUserName String
     */
    public void setUserName(String newUserName)
    {
        this.userName = newUserName;
    }

    /**
     * Gets the name of a user
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of a user
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
        notifyAllListeners();
    }

    /**
     * Adds listeners
     * @param key String,
     * @param listener Listener
     */
    public void addListener(String key, Listener listener)
    {
        if(listeners==null)
            listeners = new HashMap<>();
        if(!listeners.containsKey(key))
            listeners.put(key, listener);
    }

    /**
     * Removes a listener
     * @param key String
     */
    public void removeListener(String key){
        if(listeners==null)
            listeners = new HashMap<>();
        if(listeners.containsKey(key))
            listeners.remove(key);
    }

    /**
     * notifies all the listeners
     */
    public void notifyAllListeners()
    {
        if(listeners==null)
            listeners = new HashMap<>();
        for(Listener listener:listeners.values())
        {
            listener.update();
        }
    }

    /**
     * Checks if the user is a doctor
     * @return Boolean
     */
    public Boolean isDoctor() {
        return isDoctor;
    }

    /**
     * Gets the birthday of a user
     * @return Date birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Gets the string version of the birthday
     * @return String
     */
    public String getBirthdayString(){
        DateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
        if(birthday!=null)
            return format.format(birthday);
        return "";
    }

    /**
     * Sets the birthday of a user
     * @param birthday Date
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Gets the user's email
     * @return String email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of a user
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks if the user is a male or female
     * @return Boolean isMale
     */
    public Boolean getMale() {
        return isMale;
    }

    /**
     * Sets the variable isMale
     * @param isMale Boolean
     */
    public void setMale(Boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * Gets the phone number of the user
     * @return String phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of a user
     * @param phoneNumber String
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the address of a user
     * @return String address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user
     * @param address String
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
