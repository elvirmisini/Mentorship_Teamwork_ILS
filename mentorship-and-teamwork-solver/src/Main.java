import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;
import utilities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
            fitnessScore = FitnessCalculator.getFitnessScore(assignments, contributors, projects);
            scores.add(fitnessScore);
            i++;
        }

        List<Assignment> finalInitialSolutionAssignment = assignmentList.get(findMaxValueIndex(scores));
        List<Assignment> finalAssignment = IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(finalInitialSolutionAssignment, projects, contributors, 5, 5);

        OutputWriter.writeContent(finalInitialSolutionAssignment, absoluteOutputFilePath);
        System.out.println("Wrote assignments\n");

        System.out.println("Fitness score: " + Collections.max(scores));

        List<RawAssignments> rawAssignments = InputReader.readRawAssignments(absoluteOutputFilePath);
        if(Validator.areAssignmentsValid(rawAssignments, contributors, projects, absoluteOutputFilePath)) {
            System.out.println("The solution is valid!");
        }
        else {
            System.out.println("Wrong solution!");
        }

    }

    public static int findMaxValueIndex(List<Integer> list) {
        int maxIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > list.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

}
