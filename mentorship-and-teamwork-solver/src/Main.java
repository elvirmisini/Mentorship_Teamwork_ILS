import entities.Assignment;
import entities.Contributor;
import entities.Project;
import utilities.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<String> fileNames = InputReader.readFileName(args[0]);
        List<String> fileContents = InputReader.readFileContent(fileNames.get(0));
        List<Contributor> contributors = InputReader.readContributors(fileContents);
        List<Project> projects = InputReader.readProjects(fileContents);

        /** This sort is used for c, e, f **/
        // Sort the contributors by their highest skill level, in descending order
        contributors.sort((c1, c2) -> Double.compare(c2.getCombinedScore(), c1.getCombinedScore()));

        /** This sort is used for the c instance (collaborations) - it gives the score: 116985
         // Sort the projects by score (desc) and bestBefore (asc)
         projects.sort((p1, p2) -> {
         // Compare by score
         int scoreComparison = Integer.compare(p2.getScore(), p1.getScore());
         if (scoreComparison != 0) {
         return scoreComparison;
         }
         // Scores are equal, compare by bestBefore
         return Integer.compare(p1.getBestBefore(), p2.getBestBefore());
         });
         **/

        /** This sort is used for the e instance (exceptional skills) - it gives the score: 1551509  **/
//         projects.sort((p1, p2) -> Double.compare(p2.getPriorityScore(), p1.getPriorityScore()));


        /** This sort is used for the f instance (great mentors) - it gives the score: 176150 **/
        projects.sort(Comparator.comparing(Project::getSkillLevels));


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

        List<Assignment> changed_assignments = IteratedLocalSearch.iteratedLocalSearchWithRandomRestarts(assignments, Integer.parseInt(args[1]), projects, contributors);

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
