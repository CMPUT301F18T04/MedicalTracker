package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;

public class User
{
    private String userName;
    private String name;
    private ArrayList<Listener> listeners;
    private Boolean isDoctor;

    public User(String userName, Boolean isDoctor)
    {
        this.userName = userName;
        this.isDoctor = isDoctor;
        if(listeners == null)
        {
            listeners = new ArrayList<>();
        }
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

    public void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    public void notifyAllListeners()
    {
        for(Listener listener:listeners)
        {
            listener.update();
        }
    }

    public Boolean isDoctor() {
        return isDoctor;
    }
}
