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

        List<Assignment> assignments = InitialSolver.solver(contributors, projects);
        if (Validator.areAssignmentsValid(RawAssignments.from(assignments), unchangedContributors, unchangedProjects,
                absoluteOutputFilePath)) {
            // System.out.println(assignments);
            System.out.println("The solution is valid!");

            int fitnessScoreOfInitialSolution = FitnessCalculator.getFitnessScore(assignments, contributors, projects);
            System.out.println("Fitness score: " + fitnessScoreOfInitialSolution);

            Properties prop = new Properties();

            // try {
            // FileInputStream input = new FileInputStream("config.properties");
            // prop.load(input);
            // } catch (IOException ex) {
            // ex.printStackTrace();
            // }

            // String maxIterationsStr = prop.getProperty("max_iterations");
            // int maxIterations = Integer.parseInt(maxIterationsStr);

            // System.out.println("MaxIterations " + maxIterations);

            Scanner input = new Scanner(System.in);
            System.out.print("Write a number for max iterations: ");
            String number_in = input.nextLine();

            int max_iterations = Integer.parseInt(number_in);

            List<Assignment> assignmentAfterILS = new ArrayList<>(
                    IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(
                            assignments, max_iterations, InputReader.readProjects(fileContents),
                            InputReader.readContributors(fileContents), absoluteOutputFilePath));

            System.out.println();
            // System.out.println(assignmentAfterILS);
            int fitnessScore = FitnessCalculator.getFitnessScore(assignmentAfterILS, contributors, projects);
            System.out.println("Fitness score: " + fitnessScore);

            OutputWriter.writeContent(assignmentAfterILS, absoluteOutputFilePath);
            System.out.println("Wrote assignments\n");

            List<RawAssignments> rawAssignments = InputReader.readRawAssignments(absoluteOutputFilePath);
            if (Validator.areAssignmentsValid(rawAssignments, unchangedContributors, unchangedProjects,
                    absoluteOutputFilePath)) {
                System.out.println("The solution is valid!");
            } else {
                System.out.println("Wrong solution!");
            }
        }

    }
}
