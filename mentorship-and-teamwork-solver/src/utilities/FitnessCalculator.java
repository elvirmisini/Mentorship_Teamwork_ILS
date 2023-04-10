package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FitnessCalculator {

    public static int getFitnessScore(List<Assignment> assignments, List<Contributor> contributors, List<Project> projects) {
        List<Assignment> filteredAssignments = assignments.stream()
                .filter(assignment -> assignment.getProject() != null)
                .filter(assignment -> assignment.getContributors().size() != 0)
                .collect(Collectors.toList());
        Map<String, Integer> contributorsFinalWorkDay = contributors.stream().collect(Collectors.toMap(Contributor::getName, contributor -> 0));
        Map<String, Project> projectMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        int totalScore = 0;

        for(int i = 0; i < filteredAssignments.size(); i++) {
            String projectName = filteredAssignments.get(i).getProject().getName();
            if(!projectMap.containsKey(projectName)) {
                System.out.println("Error. The project " + projectName + " does not exist in input!");
                System.exit(0);
            }

            Project project = projectMap.get(projectName);
            int nrOfDaysToCompleteProject = project.getDaysToComplete();
            int bestBeforeDaysOfProject = project.getBestBefore();
            int projectScore = project.getScore();

            List<String> contributorNames = filteredAssignments.get(i).getContributorNames();

            for(int j = 0; j < contributorNames.size(); j++) {
                String contributorName = contributorNames.get(j);
                int newFinalWorkDay = contributorsFinalWorkDay.get(contributorName) + nrOfDaysToCompleteProject;
                contributorsFinalWorkDay.put(contributorName, newFinalWorkDay);
            }

            int endWorkDayOfProject = updateContributorLastDayToMaxFinalDayAndGetTheMaxFinalDay(contributorNames, contributorsFinalWorkDay);

            if(bestBeforeDaysOfProject > endWorkDayOfProject) {
                totalScore += projectScore;
            }
            else if ((projectScore - (endWorkDayOfProject - bestBeforeDaysOfProject)) >= 0) {
                totalScore += projectScore - (endWorkDayOfProject - bestBeforeDaysOfProject);
            }
            else {
                totalScore += 0;
            }

        }

        return totalScore;
    }

    private static int updateContributorLastDayToMaxFinalDayAndGetTheMaxFinalDay(List<String> contributorNames, Map<String, Integer> contributorsFinalWorkDay) {
        int latestFinalDate = 0;
        for(int i = 0; i < contributorNames.size(); i++) {
            if(latestFinalDate < contributorsFinalWorkDay.get(contributorNames.get(i))) {
                latestFinalDate = contributorsFinalWorkDay.get(contributorNames.get(i));
            }
        }
        for(int j = 0; j < contributorNames.size(); j++) {
            contributorsFinalWorkDay.put(contributorNames.get(j), latestFinalDate);
        }
        return latestFinalDate;
    }
}
