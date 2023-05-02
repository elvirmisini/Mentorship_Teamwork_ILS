package utilities;

import entities.Contributor;
import entities.FullAssignment;
import entities.Project;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FitnessCalculator {

    /*
    The fitness score has the time complexity O(n*m),
    where n is the number of assignments
    and m is the number of contributors in each assignment.
    */

    public static int getFitnessScore(List<FullAssignment> assignments) {
        Map<String, Integer> contributorFinalWorkDayMap = getContributorFinalWorkDayMap(assignments);
        int totalScore = 0;

        for (FullAssignment assignment : assignments) {
            int maxFinalWorkDay = updateContributorLastDayToMaxFinalDayAndGetTheMaxFinalDay(assignment, contributorFinalWorkDayMap);
            totalScore += calculateProjectScore(assignment.getProject(), maxFinalWorkDay);
        }

        return totalScore;
    }

    private static int updateContributorLastDayToMaxFinalDayAndGetTheMaxFinalDay(FullAssignment assignment, Map<String, Integer> contributorFinalWorkDayMap) {
        for (Contributor contributor : assignment.getContributors()) {
            String contributorName = contributor.getName();
            int newFinalWorkDay = contributorFinalWorkDayMap.get(contributorName) + assignment.getProject().getDaysToComplete();
            contributorFinalWorkDayMap.put(contributorName, newFinalWorkDay);
        }

        int latestFinalDate = 0;
        for (Contributor contributor : assignment.getContributors()) {
            int contributorFinalWorkDay = contributorFinalWorkDayMap.get(contributor.getName());
            if (latestFinalDate < contributorFinalWorkDay) {
                latestFinalDate = contributorFinalWorkDay;
            }
        }

        for (Contributor contributor : assignment.getContributors()) {
            contributorFinalWorkDayMap.put(contributor.getName(), latestFinalDate);
        }

        return latestFinalDate;
    }

    private static Map<String, Integer> getContributorFinalWorkDayMap(List<FullAssignment> assignments) {
        return assignments.stream()
                .map(FullAssignment::getContributors)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toMap(
                        Contributor::getName,
                        contributor -> 0,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private static int calculateProjectScore(Project project, int maxFinalWorkDay) {
        int projectScore = project.getScore();
        int bestBefore = project.getBestBefore();

        if (bestBefore > maxFinalWorkDay) {
            return projectScore;
        } else {
            return Math.max(projectScore - (maxFinalWorkDay - bestBefore), 0);
        }
    }
}