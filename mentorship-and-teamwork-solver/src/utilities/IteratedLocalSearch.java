package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final double PERTURB_PERCENTAGE = 0.3;


    public static List<Assignment> performSearch(List<Assignment> initialSolution, int maxMinutes, List<Project> projects, List<Contributor> contributors) {
        List<Assignment> currentSolution = new ArrayList<>(initialSolution);
        List<Assignment> currentHomeBase = new ArrayList<>(currentSolution);
        List<Assignment> bestSolution = new ArrayList<>(currentSolution);

        long startTime = System.currentTimeMillis();
        long maxMillis = TimeUnit.MINUTES.toMillis(maxMinutes);

        while (System.currentTimeMillis() - startTime < maxMillis) {
            int innerIteration = 0;
            while (System.currentTimeMillis() - startTime < maxMillis && innerIteration < maxMinutes * SECONDS_IN_MINUTE) {
                List<Assignment> tweakedSolution = tweak(copySolution(currentSolution), projects, contributors);
                if (deltaQuality(currentSolution, tweakedSolution) > 0) {
                    currentSolution = new ArrayList<>(tweakedSolution);
                }
                innerIteration++;
            }

            if (deltaQuality(bestSolution, currentSolution) > 0) {
                bestSolution = new ArrayList<>(currentSolution);
            }

            currentHomeBase = newHomeBase(currentHomeBase, currentSolution);
            currentSolution = perturb(currentHomeBase);
        }

        return bestSolution;
    }


    private static List<Assignment> tweak(List<Assignment> assignments, List<Project> projects, List<Contributor> contributors) {
        int operator = (int) (Math.random() * 3);

        switch (operator) {
            case 0:
                return insertProjects(assignments, projects, contributors);
            case 1:
                return operatorOne(assignments, projects, contributors);
            case 2:
                return operatorTwo(assignments, projects, contributors);
            default:
                return assignments;
        }
    }


    private static List<Assignment> insertProjects(List<Assignment> assignments, List<Project> projects, List<Contributor> contributors) {
        List<String> assignedProjectIds = assignments.stream()
                .filter(Objects::nonNull)
                .filter(assignment -> assignment.getProject() != null)
                .map(assignment -> assignment.getProject().getName())
                .collect(Collectors.toList());

        List<Project> unassignedProjects = projects.stream()
                .filter(project -> !assignedProjectIds.contains(project.getName()))
                .collect(Collectors.toList());

        if (unassignedProjects.size() > 0) {
            List<Assignment> additionalFullAssignments = InitialSolver.solveMentorshipAndTeamwork(unassignedProjects, contributors);
            assignments.addAll(additionalFullAssignments);
        }
        return assignments;
    }

    private static List<Assignment> operatorOne(List<Assignment> assignments, List<Project> projects, List<Contributor> contributors) {

        return assignments;
    }

    private static List<Assignment> operatorTwo(List<Assignment> assignments, List<Project> projects, List<Contributor> contributors) {

        return assignments;
    }

    private static List<Assignment> copySolution(List<Assignment> assignments) {
        return assignments.stream().map(Assignment::deepCopy).collect(Collectors.toList());
    }


    private static List<Assignment> newHomeBase(List<Assignment> currentHomeBase, List<Assignment> currentSolution) {
        if (quality(currentSolution) >= quality(currentHomeBase)) {
            return copySolution(currentSolution);
        } else {
            return copySolution(currentHomeBase);
        }
    }


    private static List<Assignment> perturb(List<Assignment> assignments) {

        // add a different perturb operation

        return assignments;
    }


    private static int deltaQuality(List<Assignment> oldSolution, List<Assignment> newSolution) {
        int divergenceIndex = 0;
        for (int i = 0; i < Math.min(oldSolution.size(), newSolution.size()); i++) {
            if (!oldSolution.get(i).equals(newSolution.get(i))) {
                divergenceIndex = i;
                break;
            }
        }

        List<Assignment> changedAssignmentsInNewSolution = new ArrayList<>(newSolution.subList(divergenceIndex, newSolution.size()));
        List<Assignment> changedAssignmentsInOldSolution = oldSolution.size() > divergenceIndex ? oldSolution.subList(divergenceIndex, oldSolution.size()) : new ArrayList<>();

        return quality(changedAssignmentsInNewSolution) - quality(changedAssignmentsInOldSolution);
    }


    private static int quality(List<Assignment> assignments) {
        return FitnessCalculator.getFitnessScore(assignments);
    }
}