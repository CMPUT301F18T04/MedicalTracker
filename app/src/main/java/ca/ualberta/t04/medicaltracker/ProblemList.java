package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProblemList
{
    private ArrayList<Problem> problems;
    private transient HashMap<String, Listener> listeners = new HashMap<>();

    public ProblemList(){
        problems = new ArrayList<>();
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public Problem getProblem(int index){
        return problems.get(index);
    }

    public void addProblem(Problem problem){
        problems.add(problem);
        notifyAllListener();
    }

    public void removeProblem(Problem problem){
        problems.remove(problem);
        notifyAllListener();
    }

    public void setTitle(Problem problem, String newTitle){
        if(problems.contains(problem)){
            problem.setTitle(newTitle);
        }
        notifyAllListener();
    }

    public void setDescription(Problem problem, String newDescription){
        if(problems.contains(problem)){
            problem.setDescription(newDescription);
        }
        notifyAllListener();
    }

    public void setDateStart(Problem problem, Date newDate){
        if(problems.contains(problem)){
            problem.setTime(newDate);
        }
        notifyAllListener();
    }

    public void addRecord(Problem problem, Record record){
        if(problems.contains(problem)){
            problem.getRecordList().addRecord(record);
        }
    }

    public void addListener(String key, Listener listener){
        if(listeners==null)
            listeners = new HashMap<>();
        if(!listeners.containsKey(key))
            listeners.put(key, listener);
    }

    public void notifyAllListener() {
        if(listeners==null)
            listeners = new HashMap<>();
        for (Listener listener:listeners.values()){
            listener.update();
        }
    }
}
