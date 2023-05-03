import entities.Contributor;
import entities.FullAssignment;
import entities.NameAssignment;
import entities.Project;
import utilities.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName();
        List<String> fileContents = InputReader.readFileContent(fileNames.get(0));
        List<Contributor> contributors = InputReader.readContributors(fileContents);
        List<Project> projects = InputReader.readProjects(fileContents);

        Collections.shuffle(contributors, new Random());
        Collections.shuffle(projects, new Random());

        List<Contributor> firstCopyOfContributors = contributors.stream().map(Contributor::deepCopy).collect(Collectors.toList());
        List<Contributor> secondCopyOfContributors = contributors.stream().map(Contributor::deepCopy).collect(Collectors.toList());
        List<Project> firstCopyOfProjects = projects.stream().map(Project::deepCopy).collect(Collectors.toList());
        List<Project> secondCopyOfProjects = projects.stream().map(Project::deepCopy).collect(Collectors.toList());

        List<FullAssignment> fullAssignments = InitialSolver.solve(contributors, projects);

        if (Validator.areAssignmentsValid(fullAssignments, firstCopyOfContributors, firstCopyOfProjects)) {
            System.out.println("The solution is valid!");
            System.out.println("Fitness score: " + FitnessCalculator.getFitnessScore(fullAssignments));

            //**********************************************************************************************************
            // This should be deleted when we leave the program to be executed as command-line
            Scanner input = new Scanner(System.in);
            System.out.print("Write a number for max iterations: ");
            String number_in = input.nextLine();
            int max_iterations = Integer.parseInt(number_in);
            //**********************************************************************************************************

            List<FullAssignment> fullAssignmentAfterILS = IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(fullAssignments, max_iterations, projects, contributors);
            System.out.println("Fitness score: " + FitnessCalculator.getFitnessScore(fullAssignmentAfterILS));

            List<NameAssignment> nameAssignments = InputReader.readAssignments(fileNames.get(1));
            if (Validator.areTheFinalAssignmentsValid(nameAssignments, secondCopyOfContributors, secondCopyOfProjects)) {
                System.out.println("The solution is valid!");
                OutputWriter.writeContent(fullAssignmentAfterILS, fileNames.get(1));
            } else {
                System.out.println("Wrong solution!");
            }
        }
    }
}