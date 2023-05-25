package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FitnessCalculator {

    public static int getFitnessScore(List<Assignment> assignments) {
        Map<String, Integer> contributorFinalWorkDayMap = getContributorFinalWorkDayMap(assignments);
        int totalScore = 0;

        for (Assignment assignment : assignments) {
            int maxFinalWorkDay = updateContributorLastDayToMaxFinalDayAndGetTheMaxFinalDay(assignment, contributorFinalWorkDayMap);
            totalScore += calculateProjectScore(assignment.getProject(), maxFinalWorkDay);
        }

        return totalScore;
    }

    private static int updateContributorLastDayToMaxFinalDayAndGetTheMaxFinalDay(Assignment assignment, Map<String, Integer> contributorFinalWorkDayMap) {
        for (Contributor contributor : assignment.getRoleWithContributorMap().values()) {
            String contributorName = contributor.getName();
            int newFinalWorkDay = contributorFinalWorkDayMap.get(contributorName) + assignment.getProject().getDaysToComplete();
            contributorFinalWorkDayMap.put(contributorName, newFinalWorkDay);
        }

        int latestFinalDate = 0;
        for (Contributor contributor : assignment.getRoleWithContributorMap().values()) {
            int contributorFinalWorkDay = contributorFinalWorkDayMap.get(contributor.getName());
            if (latestFinalDate < contributorFinalWorkDay) {
                latestFinalDate = contributorFinalWorkDay;
            }
        }

        for (Contributor contributor : assignment.getRoleWithContributorMap().values()) {
            contributorFinalWorkDayMap.put(contributor.getName(), latestFinalDate);
        }

        return latestFinalDate;
    }

    private static Map<String, Integer> getContributorFinalWorkDayMap(List<Assignment> assignments) {
        return assignments.stream()
                .map(assignment -> new ArrayList<>(assignment.getRoleWithContributorMap().values()))
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