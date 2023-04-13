import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;
import utilities.*;

import java.util.*;

public class Test {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName();
        String absoluteInputFilePath = fileNames.get(0);
        String absoluteOutputFilePath = fileNames.get(1);

        List<String> fileContents = InputReader.readFileContent(absoluteInputFilePath);

        List<Contributor> contributors = InputReader.readContributors(fileContents);
        Collections.shuffle(contributors, new Random());
        List<Contributor> unchangedContributors = InputReader.readContributors(fileContents);

        List<Project> projects = InputReader.readProjects(fileContents);
        Collections.shuffle(projects, new Random());
        List<Project> unchangedProjects = InputReader.readProjects(fileContents);

        List<Assignment> assignments = InitialSolver.solver(contributors, projects);
        int fitnessScoreOfInitialSolution = FitnessCalculator.getFitnessScore(assignments, contributors, projects);
        System.out.println("Fitness score: " + fitnessScoreOfInitialSolution);

        List<Assignment> assignmentAfterILS = new ArrayList<>(
                IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(
                        assignments, 10, InputReader.readProjects(fileContents), InputReader.readContributors(fileContents), absoluteOutputFilePath)
        );

        int fitnessScore = FitnessCalculator.getFitnessScore(assignmentAfterILS, contributors, projects);
        System.out.println("Fitness score: " + fitnessScore);

        OutputWriter.writeContent(assignmentAfterILS, absoluteOutputFilePath);
        System.out.println("Wrote assignments\n");

        List<RawAssignments> rawAssignments = InputReader.readRawAssignments(absoluteOutputFilePath);
        if (Validator.areAssignmentsValid(rawAssignments, unchangedContributors, unchangedProjects, absoluteOutputFilePath)) {
            System.out.println("The solution is valid!");
        } else {
            System.out.println("Wrong solution!");
        }

    }
}
