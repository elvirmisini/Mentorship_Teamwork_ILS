import entities.Assignment;
import entities.Contributor;
import entities.NameAssignment;
import entities.Project;
import utilities.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            List<String> inputAndOutputName = InputReader.readFileName(args[0]);
            List<String> fileContents = InputReader.readFileContent(inputAndOutputName.get(0));

            List<Contributor> contributors = InputReader.readContributors(fileContents);
            List<Project> projects = InputReader.readProjects(fileContents);

            System.out.println("\nInitial solution");
            List<Assignment> initialAssignments = InitialSolver.solveMentorshipAndTeamwork(projects, contributors);
            processAndValidateAssignments(initialAssignments, contributors, projects);

            System.out.println("\nOptimized solution with Iterated Local Search algorithm (" + args[1] + (Integer.parseInt(args[1]) == 1 ? " minute)" : " minutes)"));
            List<Assignment> assignmentsAfterILS = IteratedLocalSearch.performSearch(initialAssignments, Integer.parseInt(args[1]), projects, contributors);
            processAndValidateAssignments(assignmentsAfterILS, contributors, projects);

            OutputWriter.writeContent(assignmentsAfterILS, inputAndOutputName.get(1));

            System.out.println("\nSubmitted solution information");
            List<NameAssignment> nameAssignments = InputReader.readAssignments(inputAndOutputName.get(1));
            List<Assignment> submittedAssignments = Assignment.from(nameAssignments, projects, contributors);
            processAndValidateAssignments(submittedAssignments, contributors, projects);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void checkIfAssignmentsAreValid(List<Assignment> assignments, List<Contributor> contributors, List<Project> projects) throws Exception {
        if (!Validator.areAssignmentsValid(assignments, contributors, projects)) {
            throw new Exception("Wrong solution");
        }
    }

    private static void printAssignmentsInformation(int fitnessScore, List<Assignment> assignments) {
        System.out.println("Fitness score: " + fitnessScore);
        System.out.println("Number of assignments: " + assignments.size());
    }

    private static void processAndValidateAssignments(List<Assignment> assignments, List<Contributor> contributors, List<Project> projects) throws Exception {
        checkIfAssignmentsAreValid(assignments, contributors, projects);
        printAssignmentsInformation(FitnessCalculator.getFitnessScore(assignments), assignments);
    }
}