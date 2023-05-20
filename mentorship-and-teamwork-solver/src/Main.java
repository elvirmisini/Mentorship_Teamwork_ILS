import entities.Contributor;
import entities.Project;
import utilities.InputReader;

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

        List<Contributor> firstCopyOfContributors = contributors.stream().map(Contributor::deepCopy)
                .collect(Collectors.toList());
        List<Contributor> secondCopyOfContributors = contributors.stream().map(Contributor::deepCopy)
                .collect(Collectors.toList());
        List<Project> firstCopyOfProjects = projects.stream().map(Project::deepCopy).collect(Collectors.toList());
        List<Project> secondCopyOfProjects = projects.stream().map(Project::deepCopy).collect(Collectors.toList());

//        List<FullAssignment> fullAssignments = InitialSolver.solve(contributors, projects);

//        System.out.println("The solution is valid!");
//        System.out.println("Fitness score: " + FitnessCalculator.getFitnessScore(fullAssignments));
    }
}