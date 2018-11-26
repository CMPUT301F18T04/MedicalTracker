package ca.ualberta.t04.medicaltracker.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;

/**
 * This class contains all attributes and functionality for problem list
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class ProblemList
{
    private int currentId = 1;
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

    /**
     * Adds a problem to the problem list
     * @param problem Problem
     */
    public void addProblem(Problem problem){
        String problemId = "problem" + this.currentId;
        problems.add(problem);
        problem.setProblemId(problemId);
        notifyAllListener();
        this.currentId += 1;
    }

    /**
     * removes a problem form the problem list
     * @param problem Problem
     */
    public void removeProblem(Problem problem){
        problems.remove(problem);
        ElasticSearchController.deleteRecordList(problem.getRecordList().getRecordIds());
        notifyAllListener();
    }

    /**
     * Sets the title of a problem in the list
     * @param problem Problem
     * @param newTitle String
     */
    public void setTitle(Problem problem, String newTitle){
        if(problems.contains(problem)){
            problem.setTitle(newTitle);
        }
        notifyAllListener();
    }

    /**
     * Sets the description of a problem in the list
     * @param problem Problem
     * @param newDescription String
     */
    public void setDescription(Problem problem, String newDescription){
        if(problems.contains(problem)){
            problem.setDescription(newDescription);
        }
        notifyAllListener();
    }


    /**
     * Sets the date of a problem in the list
     * @param problem Problem
     * @param newDate Date
     */
    public void setDateStart(Problem problem, Date newDate){
        if(problems.contains(problem)){
            problem.setTime(newDate);
        }
        notifyAllListener();
    }

    /**
     * Adds a record to a problem in the list
     * @param problem Problem
     * @param record Record
     */
    public void addRecord(Problem problem, Record record){
        if(problems.contains(problem)){
            problem.getRecordList().addRecord(record);
        }
    }

    /**
     * Adds listeners
     * @param key String,
     * @param listener Listener
     */
    public void addListener(String key, Listener listener){
        if(listeners==null)
            listeners = new HashMap<>();
        if(!listeners.containsKey(key))
            listeners.put(key, listener);
    }

    /**
     * notifies all the listeners
     */
    public void notifyAllListener() {
        if(listeners==null)
            listeners = new HashMap<>();
        for (Listener listener:listeners.values()){
            listener.update();
        }
    }
}
