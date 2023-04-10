import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;
import utilities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Test {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName();
        String absoluteInputFilePath = fileNames.get(0);
        String absoluteOutputFilePath = fileNames.get(1);

        List<String> fileContents = InputReader.readFileContent(absoluteInputFilePath);

        List<Contributor> contributors = InputReader.readContributors(fileContents);
        List<Contributor> unchangedContributors = InputReader.readContributors(fileContents);
        Collections.shuffle(contributors, new Random());

        List<Project> projects = InputReader.readProjects(fileContents);
        List<Project> unchangedProjects = InputReader.readProjects(fileContents);
        Collections.shuffle(projects, new Random());

        List<Assignment> assignments = InitialSolver.solver(contributors, projects);
        List<Assignment> assignmentAfterILS = new ArrayList<>(
                IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(assignments, 10, projects, contributors, absoluteOutputFilePath)
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
