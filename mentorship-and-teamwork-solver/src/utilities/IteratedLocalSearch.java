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

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(List<Assignment> initialSolution, int maxMinutes, List<Project> projects, List<Contributor> contributors) {
        List<Contributor> contributorsBeforeILS = contributors.stream().map(Contributor::deepCopy).collect(Collectors.toList());
        List<Project> projectsBeforeILS = projects.stream().map(Project::deepCopy).collect(Collectors.toList());
        List<Assignment> initialSolutionBeforeILS = initialSolution.stream().map(Assignment::deepCopy).collect(Collectors.toList());

        List<Assignment> S = new ArrayList<>(initialSolution);
        List<Assignment> H = new ArrayList<>(S);
        List<Assignment> Best = new ArrayList<>(S);

        long startTime = System.currentTimeMillis();
        long maxMillis = TimeUnit.MINUTES.toMillis(maxMinutes);

        while (System.currentTimeMillis() - startTime < maxMillis) {
            int innerIteration = 0;
            while (System.currentTimeMillis() - startTime < maxMillis && innerIteration < maxMinutes * 60) {
                List<Assignment> R = Tweak(Copy(S), projects, contributors);
                if (Validator.areAssignmentsValid(R, contributors, projects)) {
                    int delta = deltaQuality(S, R);
                    if (delta > 0) {
                        S = new ArrayList<>(R);
                    }
                }
                innerIteration++;
            }

            int delta = deltaQuality(Best, S);
            if (delta > 0) {
                Best = new ArrayList<>(S);
            }

            H = NewHomeBase(H, S);
            List<Assignment> PerturbedS = Perturb(H);
            if (Validator.areAssignmentsValid(PerturbedS, contributors, projects)) {
                S = PerturbedS;
            }
        }

        if (Validator.areAssignmentsValid(Best, contributorsBeforeILS, projectsBeforeILS)) {
            return Best;
        } else {
            System.out.println("Changed assignments with ILS are not valid!");
            return initialSolutionBeforeILS;
        }
    }

    private static int deltaQuality(List<Assignment> oldSolution, List<Assignment> newSolution) {
        int x = 0;
        for (int i = 0; i < newSolution.size(); i++) {
            if (i < oldSolution.size()) {
                if (!oldSolution.get(i).equals(newSolution.get(i))) {
                    x = i;
                    break;
                }
            }

        }
        List<Assignment> changedAssignments = new ArrayList<>(newSolution.subList(x, newSolution.size()));

        return Quality(changedAssignments) - Quality(oldSolution.subList(x, oldSolution.size()));
    }

    private static List<Assignment> Copy(List<Assignment> S) {
        return new ArrayList<>(S);
    }

    private static int Quality(List<Assignment> R) {
        return FitnessCalculator.getFitnessScore(R);
    }

    private static List<Assignment> NewHomeBase(List<Assignment> H, List<Assignment> S) {
        if (Quality(S) >= Quality(H)) {
            return new ArrayList<>(S);
        } else {
            return new ArrayList<>(H);
        }
    }

    private static List<Assignment> Tweak(List<Assignment> CopyS, List<Project> projects, List<Contributor> contributors) {
        int operator = (int) (Math.random() * 3);

        switch (operator) {
            case 0:
                return InsertProjects(CopyS, projects, contributors);
            case 1:
                return RemoveProject(CopyS, contributors);
            case 2:
                return ReplaceContributors(CopyS, contributors);
            default:
                return CopyS;
        }
    }


    private static List<Assignment> InsertProjects(List<Assignment> fullAssignments, List<Project> projects, List<Contributor> contributors) {
        List<String> assignedProjectIds = fullAssignments.stream()
                .filter(Objects::nonNull)
                .filter(assignment -> assignment.getProject() != null)
                .map(assignment -> assignment.getProject().getName())
                .collect(Collectors.toList());

        List<Project> unassignedProjects = projects.stream()
                .filter(project -> !assignedProjectIds.contains(project.getName()))
                .collect(Collectors.toList());

        int twentyPercent = (int) (0.5 * unassignedProjects.size());
        List<Project> firstTwentyPercent = new ArrayList<>(unassignedProjects.subList(0, twentyPercent));
        unassignedProjects.addAll(firstTwentyPercent);
        unassignedProjects.subList(0, twentyPercent).clear();

        return fullAssignments;
    }


    private static List<Assignment> RemoveProject(List<Assignment> fullAssignments, List<Contributor> contributors) {
        List<Assignment> removedAssignments = removeLastTenPercent(fullAssignments);

        for (Assignment assignment : removedAssignments) {
            Project project = assignment.getProject();
            Map<Integer, entities.Contributor> contributorMap = assignment.getRoleWithContributorMap();
            for (Integer index : contributorMap.keySet()) {
                Map<String, Integer> contributorSkillLevel = contributorMap.get(index).getSkills().stream().collect(Collectors.toMap(Skill::getName, Skill::getLevel, (existingValue, newValue) -> existingValue));

                Skill skill = project.getSkills().get(index - 1);

                if (contributorSkillLevel.containsKey(skill.getName())) {
                    if (skill.getLevel() == contributorSkillLevel.get(skill.getName()) || skill.getLevel() == contributorSkillLevel.get(skill.getName()) - 1) {
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

                contributorSkillLevel = new HashMap<>();
            }
        }
        return removedAssignments;
    }

    private static List<Assignment> removeLastTenPercent(List<Assignment> CopyS) {
        int index1 = (int) (Math.random() * CopyS.size());
        int index2 = (int) (Math.random() * CopyS.size());

        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);

        for (int i = start, j = end; i < j; i++, j--) {
            Assignment temp = CopyS.get(i);
            CopyS.set(i, CopyS.get(j));
            CopyS.set(j, temp);
        }
        return CopyS;
    }

    private static List<Assignment> ReplaceContributors(List<Assignment> assignments, List<Contributor> contributors) {
        // Retrieve all contributors from the assignments
        Set<UUID> assignedContributorIds = assignments.stream()
                .flatMap(a -> a.getRoleWithContributorMap().values().stream())
                .map(Contributor::getId)
                .collect(Collectors.toSet());

        // Retrieve all contributors from the provided list
        Set<UUID> contributorIds = contributors.stream()
                .map(Contributor::getId)
                .collect(Collectors.toSet());

        // Find out the unassigned contributors
        List<Contributor> unassignedContributors = contributors.stream()
                .filter(c -> !assignedContributorIds.contains(c.getId()))
                .collect(Collectors.toList());

        // Determine the first 20% of the assignments
        int limit = (int) (assignments.size() * 0.5);

        // Create a copy of the assignments
        List<Assignment> newAssignments = new ArrayList<>(assignments);

        for (int i = 0; i < limit; i++) {
            Assignment assignment = newAssignments.get(i);
            boolean replaced = false;

            for (Map.Entry<Integer, Contributor> entry : new HashMap<>(assignment.getRoleWithContributorMap()).entrySet()) {
                Contributor contributor = entry.getValue();

                // Check if the contributor is in the provided list
                if (!contributorIds.contains(contributor.getId())) {
                    // Search for a replacement in the unassigned contributors
                    for (Contributor unassignedContributor : unassignedContributors) {
                        if (hasSameOrHigherSkills(contributor, unassignedContributor)) {
                            // Replace the contributor in the assignment
                            assignment.getRoleWithContributorMap().put(entry.getKey(), unassignedContributor);

                            // Remove the unassigned contributor as it's now assigned
                            unassignedContributors.remove(unassignedContributor);

                            // A replacement has been made
                            replaced = true;
                            break;
                        }
                    }
                }
                if (replaced) break; // Break out of the loop after replacing one contributor
            }
        }

        return newAssignments;
    }

    private static boolean hasSameOrHigherSkills(Contributor contributor, Contributor unassignedContributor) {
        // Create a map for easy lookup of skills by name for the contributor
        Map<String, Skill> contributorSkillsMap = contributor.getSkills().stream()
                .collect(Collectors.toMap(Skill::getName, Function.identity()));

        // Iterate through each skill of the unassigned contributor
        for (Skill skill : unassignedContributor.getSkills()) {
            // If the contributor doesn't have the skill, return false
            if (!contributorSkillsMap.containsKey(skill.getName())) {
                return false;
            }

            // If the contributor has the skill but at a lower level, return false
            if (contributorSkillsMap.get(skill.getName()).getLevel() > skill.getLevel()) {
                return false;
            }
        }

        // If we've made it through all skills without returning false, the unassigned contributor has same or higher skills
        return true;
    }


    private static List<Assignment> Perturb(List<Assignment> H) {
        int numSwaps = (int) (H.size() * 0.30);
        Random rand = new Random();
        for (int i = 0; i < numSwaps; i++) {
            int index = rand.nextInt(H.size() - 1);
            Assignment temp = H.get(index);
            H.set(index, H.get(index + 1));
            H.set(index + 1, temp);
        }
        return H;
    }
}