package utilities;

import entities.*;

import java.util.*;
import java.util.stream.Collectors;

public class SimplifiedInitialSolver {

    /*
    The time complexity of the SimplifiedInitialSolver is O(n*m), where 'n' is the number of contributors
    and 'm' is the number of projects.

    It provides a simpler approach to assigning contributors to projects based on their skills, but it does
    not take into account the more complex constraints like checking if a contributor has a mentor. It simply
    assigns contributors to projects if they have the required skill level or higher.
    */

    public static List<FullAssignment> solve(List<Contributor> contributors, List<Project> projects) {
        Map<UUID, Map<UUID, Skill>> contributorSkillsMap = createContributorSkillsMap(contributors);
        Map<String, List<Contributor>> skillToContributorsMap = createSkillToContributorsMap(contributors);

        List<FullAssignment> fullAssignments = new ArrayList<>();

        for (Project project : projects) {
            FullAssignment fullAssignment = assignContributorsToProject(project, skillToContributorsMap, contributorSkillsMap);
            if (fullAssignment.getProject() != null) {
                fullAssignments.add(fullAssignment);
            }
        }

        return fullAssignments;
    }

    private static Map<UUID, Map<UUID, Skill>> createContributorSkillsMap(List<Contributor> contributors) {
        return contributors.stream()
                .collect(Collectors.toMap(Contributor::getId,
                        contributor -> contributor.getSkills().stream()
                                .collect(Collectors.toMap(Skill::getId, skill -> skill))));
    }

    private static Map<String, List<Contributor>> createSkillToContributorsMap(List<Contributor> contributors) {
        Map<String, List<Contributor>> skillToContributorsMap = new HashMap<>();

        for (Contributor contributor : contributors) {
            for (Skill skill : contributor.getSkills()) {
                skillToContributorsMap.putIfAbsent(skill.getName(), new ArrayList<>());
                skillToContributorsMap.get(skill.getName()).add(contributor);
            }
        }

        return skillToContributorsMap;
    }

    private static FullAssignment assignContributorsToProject(Project project, Map<String, List<Contributor>> skillToContributorsMap,
                                                              Map<UUID, Map<UUID, Skill>> contributorSkillsMap) {
        FullAssignment fullAssignment = new FullAssignment();
        fullAssignment.setId(UUID.randomUUID());

        List<Skill> projectSkills = project.getSkills();
        List<ContributorWithAssignedSkill> assignedContributors = new ArrayList<>();
        Set<UUID> assignedContributorIds = new HashSet<>();
        Set<UUID> assignedSkillIds = new HashSet<>();
        List<ContributorWithAssignedSkill> contributorsToIncreaseScore = new ArrayList<>();

        for (Skill projectSkill : projectSkills) {
            Contributor assignedContributor = findContributorForSkill(projectSkill, skillToContributorsMap, assignedContributorIds);

            if (assignedContributor != null) {
                addToAssignedContributors(assignedContributor, projectSkill, assignedContributors, assignedContributorIds, assignedSkillIds);
                if (projectSkill.getLevel() == assignedContributor.getSkillLevel(projectSkill.getName())) {
                    contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(assignedContributor, projectSkill));
                }
            }
        }

        if (isValidAssignment(project, assignedContributors)) {
            increaseLevelOfAssignedContributorSkills(contributorsToIncreaseScore, contributorSkillsMap);
            fullAssignment.setProject(project);
            fullAssignment.setContributors(getContributors(assignedContributors));
        }

        return fullAssignment;
    }

    private static Contributor findContributorForSkill(Skill projectSkill, Map<String, List<Contributor>> skillToContributorsMap,
                                                       Set<UUID> assignedContributorIds) {
        List<Contributor> contributorsWithSkill = skillToContributorsMap.get(projectSkill.getName());
        if (contributorsWithSkill != null) {
            for (Contributor contributor : contributorsWithSkill) {
                if (!assignedContributorIds.contains(contributor.getId()) && contributor.getSkillLevel(projectSkill.getName()) >= projectSkill.getLevel()) {
                    return contributor;
                }
            }
        }
        return null;
    }

    private static void addToAssignedContributors(Contributor contributor, Skill projectSkill,
                                                  List<ContributorWithAssignedSkill> assignedContributors, Set<UUID> assignedContributorIds,
                                                  Set<UUID> assignedSkillIds) {
        assignedContributorIds.add(contributor.getId());
        assignedSkillIds.add(projectSkill.getId());
        assignedContributors.add(new ContributorWithAssignedSkill(contributor, projectSkill));
    }

    private static boolean isValidAssignment(Project project, List<ContributorWithAssignedSkill> assignedContributors) {
        return assignedContributors.size() == project.getSkills().size();
    }

    private static void increaseLevelOfAssignedContributorSkills(List<ContributorWithAssignedSkill> contributorsToIncreaseScore,
                                                                 Map<UUID, Map<UUID, Skill>> contributorSkillsMap) {
        for (ContributorWithAssignedSkill contributorWithSkill : contributorsToIncreaseScore) {
            Skill skill = contributorSkillsMap.get(contributorWithSkill.getContributor().getId())
                    .get(contributorWithSkill.getAssignedSkill().getId());
            if(skill != null) {
                skill.setLevel(skill.getLevel() + 1);
            }
        }
    }

    private static List<Contributor> getContributors(List<ContributorWithAssignedSkill> assignedContributors) {
        return assignedContributors.stream().map(ContributorWithAssignedSkill::getContributor).collect(Collectors.toList());
    }

}
