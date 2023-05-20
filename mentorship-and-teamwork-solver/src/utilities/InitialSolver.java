package utilities;

import entities.*;

import java.util.*;
import java.util.stream.Collectors;

public class InitialSolver {

    /*
    The time complexity of the InitialSolver code is O(p * c * ps * cs), where:
        p = number of projects
        c = number of contributors
        ps = maximum number of skills per project
        cs = maximum number of skills per contributor

    This initial solution solver takes in consideration all the constraints of the problem.
    */

    public static List<FullAssignment> solve(List<Project> projects, List<Contributor> contributors) {
        Map<UUID, Contributor> contributorMap = contributors.stream().collect(Collectors.toMap(Contributor::getId, contributor -> contributor));

        Map<UUID, List<String>> contributorIdWithSkillNames = contributors.stream()
                .collect(Collectors.toMap(Contributor::getId,
                        contributor -> contributor.getSkills().stream().map(Skill::getName).collect(Collectors.toList())));

        Map<UUID, Map<String, Integer>> contributorIdWithSkillNameWithLevel = contributors.stream()
                .collect(Collectors.toMap(Contributor::getId, contributor ->
                        contributor.getSkills().stream().collect(Collectors.toMap(Skill::getName, Skill::getLevel))));


        List<FullAssignment> assignments = new ArrayList<>();

        for (Project project : projects) {
            FullAssignment assignment = new FullAssignment();
            assignment.setId(UUID.randomUUID());
            assignment.setProject(project);
            List<Skill> projectSkillList = project.getSkills();

            List<ContributorWithAssignedSkill> assignedContributorForSkillList = new ArrayList<>();
            List<UUID> usedContributor = new ArrayList<>();
            List<UUID> usedSkill = new ArrayList<>();

            for (Skill projectSkill : projectSkillList) {
                if (projectSkillList.size() > assignedContributorForSkillList.size()) {
                    for (Contributor contributor : contributors) {
                        if (!usedContributor.contains(contributor.getId()) && !usedSkill.contains(projectSkill.getId())) {

                            if (projectSkillList.size() > assignedContributorForSkillList.size()) {
                                List<String> contributorSkillNames = contributorIdWithSkillNames.get(contributor.getId());
                                if (contributorSkillNames.contains(projectSkill.getName())) {
                                    if (projectSkill.getLevel() <= contributorIdWithSkillNameWithLevel.get(contributor.getId()).get(projectSkill.getName())) {
                                        usedContributor.add(contributor.getId());
                                        usedSkill.add(projectSkill.getId());
                                        assignedContributorForSkillList.add(new ContributorWithAssignedSkill(contributor, projectSkill));
                                    }
                                    if (projectSkill.getLevel() - 1 == contributorIdWithSkillNameWithLevel.get(contributor.getId()).get(projectSkill.getName()) &&
                                            hasMentor(projectSkill.getName(), projectSkill.getLevel(), assignedContributorForSkillList)) {
                                        usedContributor.add(contributor.getId());
                                        usedSkill.add(projectSkill.getId());
                                        assignedContributorForSkillList.add(new ContributorWithAssignedSkill(contributor, projectSkill));
                                    }
                                } else {
                                    if (projectSkill.getLevel() == 1) {
                                        usedContributor.add(contributor.getId());
                                        usedSkill.add(projectSkill.getId());
                                        assignedContributorForSkillList.add(new ContributorWithAssignedSkill(contributor, projectSkill));
                                    }
                                }
                            }
                        }

                    }
                }
            }

            if (projectSkillList.size() == assignedContributorForSkillList.size()) {
                List<Contributor> assignedContributors = assignedContributorForSkillList.stream()
                        .map(contributorWithAssignedSkill ->
                                contributorMap.get(contributorWithAssignedSkill.getContributor().getId())).collect(Collectors.toList());

                assignment.setContributors(assignedContributors);
                assignments.add(assignment);
            }
        }


        return assignments;
    }

    private static boolean hasMentor(String skillName, int skillLevel, List<ContributorWithAssignedSkill> assignedContributorForSkillList) {
        for (ContributorWithAssignedSkill contributorWithAssignedSkill : assignedContributorForSkillList) {
            Skill mentorSkill = contributorWithAssignedSkill.getAssignedSkill();
            if (Objects.equals(mentorSkill.getName(), skillName) && mentorSkill.getLevel() >= skillLevel) {
                return true;
            }
        }
        return false;
    }

}