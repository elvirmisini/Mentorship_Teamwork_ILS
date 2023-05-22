import entities.Assignment;
import entities.Contributor;
import entities.Project;
import utilities.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName(args[0]);
        List<String> fileContents = InputReader.readFileContent(fileNames.get(0));
        List<Contributor> contributors = InputReader.readContributors(fileContents);
        List<Project> projects = InputReader.readProjects(fileContents);

        Collections.shuffle(contributors, new Random());
        Collections.shuffle(projects, new Random());

        List<Contributor> firstCopyOfContributors = contributors.stream().map(Contributor::deepCopy).collect(Collectors.toList());
        List<Project> firstCopyOfProjects = projects.stream().map(Project::deepCopy).collect(Collectors.toList());

        List<Assignment> assignments = InitialSolver.solveMentorshipAndTeamwork(projects, contributors);

        if (!Validator.areAssignmentsValid(assignments, firstCopyOfContributors, firstCopyOfProjects)) {
            System.out.println("Wrong initial solution");
            System.exit(0);
        }

        int initialSolutionFitnessScore = FitnessCalculator.getFitnessScore(assignments);
        System.out.println("Initial solution fitness score: " + initialSolutionFitnessScore);

        List<Assignment> changed_assignments = IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(assignments, 1, projects, contributors);

        if (!Validator.areAssignmentsValid(changed_assignments, firstCopyOfContributors, firstCopyOfProjects)) {
            System.out.println("Wrong initial solution");
            System.exit(0);
        }

        initialSolutionFitnessScore = FitnessCalculator.getFitnessScore(changed_assignments);
        System.out.println("Initial solution fitness score: " + initialSolutionFitnessScore);


        OutputWriter.writeContent(changed_assignments, fileNames.get(1));
    }
}