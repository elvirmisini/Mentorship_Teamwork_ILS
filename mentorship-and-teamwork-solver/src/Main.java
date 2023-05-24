import entities.Assignment;
import entities.Contributor;
import entities.Project;
import utilities.*;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName(args[0]);
        List<String> fileContents = InputReader.readFileContent(fileNames.get(0));
        List<Contributor> contributors = InputReader.readContributors(fileContents);
        List<Project> projects = InputReader.readProjects(fileContents);

        // Sort the contributors by their highest skill level, in descending order
        contributors.sort((c1, c2) -> Double.compare(c2.getCombinedScore(), c1.getCombinedScore()));
        // Sort the projects by combined score, in descending order
        projects.sort((p1, p2) -> {
            int result = Double.compare(p2.getPriorityScore(), p1.getPriorityScore());
            if (result == 0) { // if priority scores are equal, compare average skill levels
                result = Double.compare(p2.getAverageSkillLevel(), p1.getAverageSkillLevel());
            }
            return result;
        });

        List<Contributor> firstCopyOfContributors = contributors.stream().map(Contributor::deepCopy).collect(Collectors.toList());
        List<Project> firstCopyOfProjects = projects.stream().map(Project::deepCopy).collect(Collectors.toList());

        List<Assignment> assignments = InitialSolver.solveMentorshipAndTeamwork(projects, contributors);

        if (!Validator.areAssignmentsValid(assignments, firstCopyOfContributors, firstCopyOfProjects)) {
            System.out.println("Wrong initial solution");
            System.exit(0);
        }

        int initialSolutionFitnessScore = FitnessCalculator.getFitnessScore(assignments);
        System.out.println("Initial solution fitness score: " + initialSolutionFitnessScore);
        System.out.println("Initial assignments: " + assignments.size());

        List<Assignment> changed_assignments = IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(assignments, 1, projects, contributors);

        if (!Validator.areAssignmentsValid(changed_assignments, firstCopyOfContributors, firstCopyOfProjects)) {
            System.out.println("Wrong initial solution");
            System.exit(0);
        }

        initialSolutionFitnessScore = FitnessCalculator.getFitnessScore(changed_assignments);
        System.out.println("Final solution fitness score: " + initialSolutionFitnessScore);
        System.out.println("Final assignments: " + changed_assignments.size());

        OutputWriter.writeContent(changed_assignments, fileNames.get(1));
    }


}
