import entities.*;
import utilities.*;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName();
        String inputFileName = fileNames.get(0);
        String outputFileName = fileNames.get(1);

        List<String> fileContents = InputReader.readFileContent(inputFileName);

        List<Contributor> contributors = InputReader.readContributors(fileContents);

        Collections.shuffle(contributors, new Random());
        List<Contributor> unchangedContributors = InputReader.readContributors(fileContents);

        List<Project> projects = InputReader.readProjects(fileContents);
        // Sort contributors in descending order based on number of skills
        Collections.sort(contributors, new Comparator<Contributor>() {
            @Override
            public int compare(Contributor c1, Contributor c2) {
                return Integer.compare(c2.getSkills().size(), c1.getSkills().size());
            }
        });

        // Sort projects in descending order based on number of skills
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(Project p1, Project p2) {
                return Integer.compare(p2.getSkills().size(), p1.getSkills().size());
            }
        });

        // Collections.shuffle(projects, new Random());


        List<Project> unchangedProjects = InputReader.readProjects(fileContents);

        // contributors in descending order based on number of skills
        Collections.sort(contributors, new Comparator<Contributor>() {
            @Override
            public int compare(Contributor c1, Contributor c2) {
                return Integer.compare(c2.getSkills().size(), c1.getSkills().size());
            }
        });
//        Collections.reverse(contributors);

//         Sort projects in descending order based on number of skills
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(Project p1, Project p2) {
                return Integer.compare(p2.getSkills().size(), p1.getSkills().size());
            }
        });
//        Collections.shuffle(projects, new Random());
//        Collections.reverse(projects);

//        projects.sort(new Project.ProjectComparator());

//        List<FullAssignment> fullAssignments = InitialSolver.solver(contributors, projects);

        List<FullAssignment> fullAssignments = InitialSolver.solve(contributors, projects);

        if (Validator.areAssignmentsValid(NameAssignment.from(fullAssignments), unchangedContributors, unchangedProjects)) {
            // System.out.println(assignments);
            System.out.println("The solution is valid!");

            int fitnessScoreOfInitialSolution = FitnessCalculator.getFitnessScore(fullAssignments);
            System.out.println("Fitness score: " + fitnessScoreOfInitialSolution);

            //***** This should be deleted when we leave the program to be executed as command-line
            Scanner input = new Scanner(System.in);
            System.out.print("Write a number for max iterations: ");
            String number_in = input.nextLine();

            int max_iterations = Integer.parseInt(number_in);

            List<FullAssignment> fullAssignmentAfterILS = new ArrayList<>(
                    IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(
                            fullAssignments, max_iterations, InputReader.readProjects(fileContents),
                            InputReader.readContributors(fileContents), outputFileName));

            System.out.println();
            // System.out.println(assignmentAfterILS);
            int fitnessScore = FitnessCalculator.getFitnessScore(fullAssignmentAfterILS);
            System.out.println("Fitness score: " + fitnessScore);

            OutputWriter.writeContent(fullAssignmentAfterILS, outputFileName);
            System.out.println("Wrote assignments\n");

            List<NameAssignment> rawAssignments = InputReader.readAssignments(outputFileName);
            if (Validator.areAssignmentsValid(rawAssignments, unchangedContributors, unchangedProjects)) {
                System.out.println("The solution is valid!");
            } else {
                System.out.println("Wrong solution!");
            }
        }

    }
}
