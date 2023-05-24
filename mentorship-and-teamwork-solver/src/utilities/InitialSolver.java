package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.Skill;

import java.util.*;
import java.util.stream.Collectors;

public class InitialSolver {

    static Map<UUID, String> contributorIdAndSkillNameToIncrease = new HashMap<>();
    static Map<UUID, String> contributorIdAndSkillNameToAdd = new HashMap<>();

    public static List<Assignment> solveMentorshipAndTeamwork(List<Project> projects, List<Contributor> contributors) {
        Map<UUID, List<String>> contributorIdWithSkillNamesMap = getContributorSkillsMap(contributors);
        Map<UUID, Map<String, Integer>> contributorIdAndSkillNameWithLevel = getContributorSkillLevelMap(contributors);

        List<Assignment> assignments = new ArrayList<>();

        for (Project project : projects) {
            Assignment assignment = new Assignment();
            assignment.setId(UUID.randomUUID());
            Map<Integer, Contributor> assignedContributorsToProject = new HashMap<>();
            List<UUID> assignedContributorIdsToProject = new ArrayList<>();
            List<UUID> addedSkill = new ArrayList<>();

            Collections.shuffle(contributors);

            for (Skill projectSkill : project.getSkills()) {
                for (Contributor contributor : contributors) {
                    if (!addedSkill.contains(projectSkill.getId())) {
                        if (!assignedContributorIdsToProject.contains(contributor.getId())) {
                            if (contributorIdWithSkillNamesMap.get(contributor.getId())
                                    .contains(projectSkill.getName())) {
                                int contributorSkillLevel = contributorIdAndSkillNameWithLevel.get(contributor.getId())
                                        .get(projectSkill.getName());
                                if (contributorSkillLevel >= projectSkill.getLevel()) {
                                    addedSkill.add(projectSkill.getId());
                                    assignedContributorsToProject.put(assignedContributorsToProject.size() + 1,
                                            contributor);
                                    assignedContributorIdsToProject.add(contributor.getId());

                                    if (contributorSkillLevel == projectSkill.getLevel()) {
                                        contributorIdAndSkillNameToIncrease.put(contributor.getId(),
                                                projectSkill.getName());
                                    }
                                } else if (contributorSkillLevel == projectSkill.getLevel() - 1
                                        && hasMentor(projectSkill.getLevel(), projectSkill.getName(),
                                                assignedContributorsToProject)) {
                                    addedSkill.add(projectSkill.getId());
                                    assignedContributorsToProject.put(assignedContributorsToProject.size() + 1,
                                            contributor);
                                    assignedContributorIdsToProject.add(contributor.getId());
                                    contributorIdAndSkillNameToIncrease.put(contributor.getId(),
                                            projectSkill.getName());
                                }
                            } else if (projectSkill.getLevel() == 1 && hasMentor(projectSkill.getLevel(),
                                    projectSkill.getName(), assignedContributorsToProject)) {
                                addedSkill.add(projectSkill.getId());
                                assignedContributorsToProject.put(assignedContributorsToProject.size() + 1,
                                        contributor);
                                assignedContributorIdsToProject.add(contributor.getId());
                                contributorIdAndSkillNameToAdd.put(contributor.getId(), projectSkill.getName());
                            }
                        }
                    }
                }

                if (isProjectFullyAssigned(project, addedSkill)) {
                    assignments.add(assignment);
                    assignment.setProject(project);
                    assignment.setRoleWithContributorMap(assignedContributorsToProject);
                    updateAssignedContributorsSkillLevel(contributors);

                    Random rand = new Random();

                    // Remove all assigned contributors from the contributors list
                    contributors.removeAll(assignedContributorsToProject.values());

                    // Add all assigned contributors to random positions in the contributors list
                    for (Contributor c : assignedContributorsToProject.values()) {
                        int randomIndex = rand.nextInt(contributors.size() + 1);
                        contributors.add(randomIndex, c);
                    }
                    break;
                }

                clearContributorSkillMaps();
            }
        }
        return assignments;
    }

    private static boolean hasMentor(int projectSkillLevel, String projectSkillName,
            Map<Integer, Contributor> assignedContributorsToProjectMap) {
        for (Contributor contributor : assignedContributorsToProjectMap.values()) {
            for (Skill skill : contributor.getSkills()) {
                if (Objects.equals(skill.getName(), projectSkillName) && skill.getLevel() >= projectSkillLevel) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void updateAssignedContributorsSkillLevel(List<Contributor> contributors) {
        for (Contributor contributor : contributors) {
            if (contributorIdAndSkillNameToIncrease.containsKey(contributor.getId())) {
                for (Skill skill : contributor.getSkills()) {
                    if (Objects.equals(skill.getName(), contributorIdAndSkillNameToIncrease.get(contributor.getId()))) {
                        skill.setLevel(skill.getLevel() + 1);
                    }
                }
            }
            if (contributorIdAndSkillNameToAdd.containsKey(contributor.getId())) {
                contributor.getSkills()
                        .add(new Skill(UUID.randomUUID(), contributorIdAndSkillNameToAdd.get(contributor.getId()), 1));
            }
        }
    }

    private static Map<UUID, List<String>> getContributorSkillsMap(List<Contributor> contributors) {
        return contributors.stream()
                .collect(Collectors.toMap(Contributor::getId,
                        contributor -> contributor.getSkills().stream().map(Skill::getName)
                                .collect(Collectors.toList())));
    }

    private static Map<UUID, Map<String, Integer>> getContributorSkillLevelMap(List<Contributor> contributors) {
        return contributors.stream()
                .collect(Collectors.toMap(Contributor::getId, contributor -> contributor.getSkills().stream()
                        .collect(Collectors.toMap(Skill::getName, Skill::getLevel,
                                (existingValue, newValue) -> existingValue))));
    }

    private static boolean isProjectFullyAssigned(Project project, List<UUID> addedSkill) {
        return project.getSkills().size() == addedSkill.size();
    }

    private static void clearContributorSkillMaps() {
        contributorIdAndSkillNameToIncrease = new HashMap<>();
        contributorIdAndSkillNameToAdd = new HashMap<>();
    }

}