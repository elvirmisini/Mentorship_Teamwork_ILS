package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.Skill;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final double PERTURB_PERCENTAGE = 0.3;
    private static final double PERCENTAGE_OF_ASSIGNMENTS_TO_REPLACE_CONTRIBUTORS = 0.3;
    private static final double PERCENTAGE_OF_PROJECTS_TO_BE_REMOVED = 0.3;


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
                return removeProject(assignments, contributors);
            case 2:
                return replaceContributors(assignments, contributors);
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


    private static List<Assignment> removeProject(List<Assignment> assignments, List<Contributor> contributors) {
        int removeCount = (int) Math.ceil(assignments.size() * PERCENTAGE_OF_PROJECTS_TO_BE_REMOVED);
        List<Assignment> removedAssignments = new ArrayList<>();
        for (int i = 0; i < removeCount; i++) {
            removedAssignments.add(assignments.remove(assignments.size() - 1));
        }

        for (Assignment assignment : removedAssignments) {
            Project project = assignment.getProject();
            Map<Integer, entities.Contributor> contributorMap = assignment.getRoleWithContributorMap();
            for (Integer index : contributorMap.keySet()) {
                Map<String, Integer> contributorSkillLevel = contributorMap.get(index).getSkills().stream().collect(
                        Collectors.toMap(Skill::getName, Skill::getLevel, (existingValue, newValue) -> existingValue));

                Skill skill = project.getSkills().get(index - 1);

                if (contributorSkillLevel.containsKey(skill.getName())) {
                    if (skill.getLevel() == contributorSkillLevel.get(skill.getName())
                            || skill.getLevel() == contributorSkillLevel.get(skill.getName()) - 1) {
                        for (Contributor contributor : contributors) {
                            if (Objects.equals(contributor.getName(), contributorMap.get(index).getName())) {
                                for (Skill contributorSkill : contributor.getSkills()) {
                                    if (Objects.equals(skill.getName(), contributorSkill.getName())) {
                                        skill.setLevel(skill.getLevel() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return assignments;
    }


    private static List<Assignment> replaceContributors(List<Assignment> assignments, List<Contributor> contributors) {
        Set<UUID> assignedContributorIds = assignments.stream()
                .flatMap(a -> a.getRoleWithContributorMap().values().stream())
                .map(Contributor::getId)
                .collect(Collectors.toSet());

        Set<UUID> contributorIds = contributors.stream().map(Contributor::getId).collect(Collectors.toSet());

        List<Contributor> unassignedContributors = contributors.stream()
                .filter(c -> !assignedContributorIds.contains(c.getId()))
                .collect(Collectors.toList());

        int limit = (int) (assignments.size() * PERCENTAGE_OF_ASSIGNMENTS_TO_REPLACE_CONTRIBUTORS);

        List<Assignment> newAssignments = new ArrayList<>(assignments);
        for (int i = 0; i < limit; i++) {
            Assignment assignment = newAssignments.get(i);
            boolean replaced = false;

            for (Map.Entry<Integer, Contributor> entry : new HashMap<>(assignment.getRoleWithContributorMap()).entrySet()) {
                Contributor contributor = entry.getValue();

                if (!contributorIds.contains(contributor.getId())) {
                    for (Contributor unassignedContributor : unassignedContributors) {
                        if (hasSameOrHigherSkills(contributor, unassignedContributor)) {
                            assignment.getRoleWithContributorMap().put(entry.getKey(), unassignedContributor);
                            unassignedContributors.remove(unassignedContributor);
                            replaced = true;
                            break;
                        }
                    }
                }
                if (replaced)
                    break;
            }
        }

        return newAssignments;
    }


    private static boolean hasSameOrHigherSkills(Contributor contributor, Contributor unassignedContributor) {
        Map<String, Skill> contributorSkillsMap = contributor.getSkills().stream().collect(Collectors.toMap(Skill::getName, Function.identity()));
        for (Skill skill : unassignedContributor.getSkills()) {
            if (!contributorSkillsMap.containsKey(skill.getName())) {
                return false;
            }
            if (contributorSkillsMap.get(skill.getName()).getLevel() > skill.getLevel()) {
                return false;
            }
        }
        return true;
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
        int numSwaps = (int) (assignments.size() * PERTURB_PERCENTAGE);

        List<Assignment> perturbedList = new ArrayList<>(assignments);
        for (int i = 0; i < numSwaps; i++) {
            int index = new Random().nextInt(perturbedList.size() - 1);
            Collections.swap(perturbedList, index, index + 1);
        }
        return perturbedList;
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