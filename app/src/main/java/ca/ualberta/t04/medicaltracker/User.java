package ca.ualberta.t04.medicaltracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
    private String password;
    private String address;

    public User(String userName, String password, Boolean isDoctor)
    {
        this.userName = userName;
        this.isDoctor = isDoctor;
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String newUserName)
    {
        this.userName = newUserName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyAllListeners();
    }

    public void addListener(String key, Listener listener)
    {
        if(listeners==null)
            listeners = new HashMap<>();
        if(!listeners.containsKey(key))
            listeners.put(key, listener);
    }

    public void notifyAllListeners()
    {
        if(listeners==null)
            listeners = new HashMap<>();
        for(Listener listener:listeners.values())
        {
            listener.update();
        }
    }

    public Boolean isDoctor() {
        return isDoctor;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getBirthdayString(){
        DateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
        if(birthday!=null)
            return format.format(birthday);
        return "";
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getMale() {
        return isMale;
    }

    public void setMale(Boolean isMale) {
        this.isMale = isMale;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
