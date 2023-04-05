package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.*;
import java.util.stream.Collectors;

public class FitnessCalculator {

    public static int getFitnessScore(List<Assignment> assignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Integer> contributorsFinalWorkDay = new HashMap<>();
        for(int i = 0; i < contributors.size(); i++) {
            contributorsFinalWorkDay.put(contributors.get(i).getName(), 0);
        }

        Map<String, Project> projectMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        int totalScore = 0;

        for(int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i) == null) {
                continue;
            }
            if (assignments.get(i).getProject() == null) {
                continue;
            }
            String projectName = assignments.get(i).getProject().getName();
            if(!projectMap.containsKey(projectName)) {
                System.out.println("Error. The project " + projectName + " does not exist in input!");
                System.exit(0);
            }

            Project project = projectMap.get(projectName);
            int nrOfDaysToCompleteProject = project.getDaysToComplete();
            int bestBeforeDaysOfProject = project.getBestBefore();
            int projectScore = project.getScore();

            List<String> contributorNames = assignments.get(i).getContributorNames();

            for(int j = 0; j < contributorNames.size(); j++) {
                String contributorName = contributorNames.get(j);
                int oldFinalWorkDay = contributorsFinalWorkDay.get(contributorName);
                contributorsFinalWorkDay.put(contributorName, oldFinalWorkDay + nrOfDaysToCompleteProject);
            }

            setTheSameFinalDayForAllContributorsInProject(contributorNames, contributorsFinalWorkDay);
            int endWorkDayOfProject = getTheLatestDayOfAllAssignedContributors(contributorNames, contributorsFinalWorkDay);


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

    private static void setTheSameFinalDayForAllContributorsInProject(List<String> contributorNames, Map<String, Integer> contributorsFinalWorkDay) {
        int latestFinalDate = Collections.max(contributorsFinalWorkDay.values());
        for(int i = 0; i < contributorNames.size(); i++) {
            contributorsFinalWorkDay.put(contributorNames.get(i), latestFinalDate);
        }
    }

    private static int getTheLatestDayOfAllAssignedContributors(List<String> contributorNames, Map<String, Integer> contributorsFinalWorkDay) {
        List<Integer> latestDays = new ArrayList<>();
        for (int i = 0; i < contributorNames.size(); i++) {
            latestDays.add(contributorsFinalWorkDay.get(contributorNames.get(i)));
        }

        return Collections.max(latestDays);
    }

}
