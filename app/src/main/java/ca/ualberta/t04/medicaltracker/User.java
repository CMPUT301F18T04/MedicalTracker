package ca.ualberta.t04.medicaltracker;

public class User
{
    private int id = 0;
    private String name;

    public User(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int newID)
    {
        this.id = newID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
