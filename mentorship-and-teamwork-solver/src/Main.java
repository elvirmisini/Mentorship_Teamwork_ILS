import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;
import utilities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {

        List<String> fileNames = InputReader.readFileName();
        String absoluteInputFilePath = fileNames.get(0);
        String absoluteOutputFilePath = fileNames.get(1);

        List<String> fileContents = InputReader.readFileContent(absoluteInputFilePath);
        List<Contributor> contributors = InputReader.readContributors(fileContents);
        List<Project> projects = InputReader.readProjects(fileContents);

        List<List<Assignment>> assignmentList = new ArrayList<>();
        int fitnessScore;
        List<Integer> scores = new ArrayList<>();

        int i = 0;
        while (i < 5) {
            Collections.shuffle(projects);
            List<Assignment> assignments = InitialSolver.solver(contributors, projects);
            assignmentList.add(assignments);
            /////////////////////////////////// Operator
            Random rand = new Random();
            // Choose two random days
            int day1 = rand.nextInt(assignmentList.size());
            int day2 = rand.nextInt(assignmentList.size());

            // Choose two random assignments from each of the chosen days
            List<Assignment> assignmentsDay1 = assignmentList.get(day1);
            List<Assignment> assignmentsDay2 = assignmentList.get(day2);
            int assignment1Index = rand.nextInt(assignmentsDay1.size());
            int assignment2Index = rand.nextInt(assignmentsDay2.size());
            Assignment assignment1 = assignmentsDay1.get(assignment1Index);
            Assignment assignment2 = assignmentsDay2.get(assignment2Index);

            // Swap the assignments
            assignmentsDay1.set(assignment1Index, assignment2);
            assignmentsDay2.set(assignment2Index, assignment1);

            /////////////////// System.out.println(assignmentsDay2);

            fitnessScore = FitnessCalculator.getFitnessScore(assignmentsDay2, contributors, projects);
            scores.add(fitnessScore);
            i++;
        }

        List<Assignment> finalInitialSolutionAssignment = assignmentList.get(findMaxValueIndex(scores));
        // List<Assignment> finalAssignment =
        // IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(finalInitialSolutionAssignment,
        // projects, contributors, 5, 5);

        ///////////////////////
        // In this implementation, we randomly select two assignments from the current
        // solution and swap them.
        // This is done by storing the first assignment in a temporary variable,
        // replacing it with the second assignment, and then replacing the second
        // assignment with the temporary variable.
        // The modified solution is then returned.
        /////////////////////// One kind of operator

        // int index1 = (int) (Math.random() * finalInitialSolutionAssignment.size());
        // int index2 = (int) (Math.random() * finalInitialSolutionAssignment.size());

        // // Swap the assignments
        // Assignment temp = finalInitialSolutionAssignment.get(index1);
        // finalInitialSolutionAssignment.set(index1,
        // finalInitialSolutionAssignment.get(index2));
        // finalInitialSolutionAssignment.set(index2, temp);
        ////////////////////////////////////////////
        ////////////////////////////////////////////

        OutputWriter.writeContent(finalInitialSolutionAssignment, absoluteOutputFilePath);
        System.out.println("Wrote assignments\n");

        System.out.println("Fitness score: " + Collections.max(scores));

        List<RawAssignments> rawAssignments = InputReader.readRawAssignments(absoluteOutputFilePath);
        if (Validator.areAssignmentsValid(rawAssignments, contributors, projects, absoluteOutputFilePath)) {
            System.out.println("The solution is valid!");
        } else {
            System.out.println("Wrong solution!");
        }

    }

    private static int findMaxValueIndex(List<Integer> list) {
        int maxIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > list.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

}
