package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import java.util.Date;


import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.ProblemList;

import static junit.framework.TestCase.assertTrue;

public class ProblemListUnitTest
{
    @Test
    public void problemListTest(){
        ProblemList problemList = new ProblemList();
        problemList.addListener("1", new Listener() {
            @Override
            public void update() {
                System.out.println("The message is sent by listeners.");
            }
        });
        Problem problem = new Problem("Test", new Date(), "Description");
        problemList.addProblem(problem);
        assertTrue("The problem that gets from problemList should equal to the original problem", problemList.getProblem(0).equals(problem));
    }
}
