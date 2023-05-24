package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.Skill;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(
            List<Assignment> initialSolution,
            int maxMinutes, List<Project> projects,
            List<Contributor> contributors
    ) {
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
        return Quality(newSolution) - Quality(oldSolution);
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
                return SwapAssignments(CopyS);
//            case 3:
//                return ReplaceContributors(CopyS, contributors);
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

        int twentyPercent = (int) (0.1 * unassignedProjects.size());

        // Get first 20% of the list
        List<Project> firstTwentyPercent = new ArrayList<>(unassignedProjects.subList(0, twentyPercent));

        // Add them to the end of the list
        unassignedProjects.addAll(firstTwentyPercent);

        // Remove them from the beginning of the list
        unassignedProjects.subList(0, twentyPercent).clear();

        if (unassignedProjects.size() > 0) {
            List<Assignment> additionalFullAssignments = InitialSolver.solveMentorshipAndTeamwork(unassignedProjects, contributors);
            fullAssignments.addAll(additionalFullAssignments);
        }

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

        return fullAssignments;
    }

    private static <T> List<T> removeLastTenPercent(List<T> list) {
        int removeCount = (int) Math.ceil(list.size() * 0.1);
        List<T> removedItems = new ArrayList<>();

        for (int i = 0; i < removeCount; i++) {
            removedItems.add(list.remove(list.size() - 1));
        }

        return removedItems;
    }

    private static List<Assignment> ReplaceContributors(List<Assignment> assignments, List<Contributor> contributors) {
        List<Contributor> assignedContributorIds = assignments.stream()
                .map(assignment -> assignment.getRoleWithContributorMap().values())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Contributor> unassignedContributors = contributors.stream()
                .filter(contributor -> !assignedContributorIds.stream().map(Contributor::getId).collect(Collectors.toList()).contains(contributor.getId()))
                .collect(Collectors.toList());

        if(unassignedContributors.size() > 0) {
            unassignedContributors.sort((c1, c2) -> Double.compare(c2.getCombinedScore(), c1.getCombinedScore()));

        }

        return assignments;
    }

    private static List<Assignment> SwapAssignments(List<Assignment> H) {
        // Calculate 10% of list size
        int numSwaps = (int)(H.size() * 0.10);  // This rounds down

        // Create a random number generator
        Random rand = new Random();

        // Perform the swaps
        for (int i = 0; i < numSwaps; i++) {
            // Generate a random index between 0 and list size - 2
            // We subtract 2 to avoid ArrayIndexOutOfBoundsException as we swap element with its next one
            int index = rand.nextInt(H.size() - 1);

            // Swap the element with its neighbor
            Assignment temp = H.get(index);
            H.set(index, H.get(index + 1));
            H.set(index + 1, temp);
        }
        return H;
    }

    private static List<Assignment> Perturb(List<Assignment> H) {
        return H;
    }
}