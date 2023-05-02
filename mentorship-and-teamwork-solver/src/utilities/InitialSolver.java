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

    public static List<FullAssignment> solve(List<Contributor> contributors, List<Project> projects) {
        Map<UUID, Map<UUID, Skill>> contributorSkillsMap = contributors.stream().collect(Collectors.toMap(Contributor::getId, contributor ->
                contributor.getSkills().stream().collect(Collectors.toMap(Skill::getId, skill -> skill))
        ));

        List<FullAssignment> fullAssignments = new ArrayList<>();

        for (int i = 0; i < projects.size(); i++) {
            FullAssignment fullAssignment = new FullAssignment();
            fullAssignment.setId(UUID.randomUUID());

            Project currentProject = projects.get(i);
            List<Skill> currentProjectSkills = currentProject.getSkills();

            List<ContributorWithAssignedSkill> assignedContributors = new ArrayList<>();
            List<String> assignedContributorIds = new ArrayList<>();
            List<ContributorWithAssignedSkill> contributorsToIncreaseScore = new ArrayList<>();
            List<String> assignedSkillIds = new ArrayList<>();

            endSearchForThisProject:
            for (int j = 0; j < contributors.size(); j++) {
                Contributor currentContributor = contributors.get(j);
                List<Skill> currentContributorSkills = currentContributor.getSkills();

                for (int k = 0; k < currentProjectSkills.size(); k++) {
                    Skill currentProjectSkill = currentProjectSkills.get(k);

                    for (int t = 0; t < currentContributorSkills.size(); t++) {
                        Skill currentContributorSkill = currentContributorSkills.get(t);

                        if (isValidAssignment(currentProject, assignedContributors)) {
                            increaseLevelOfAssignedContributorSkills(contributorsToIncreaseScore, contributorSkillsMap);
                            fullAssignment.setProject(currentProject);
                            fullAssignment.setContributors(getContributors(assignedContributors));
                            break endSearchForThisProject;
                        } else {

                            if (Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() <= currentContributorSkill.getLevel() &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                                if (currentProjectSkill.getLevel() == currentContributorSkill.getLevel()) {
                                    contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(currentContributor, currentContributorSkill));
                                }
                            }

                            if (Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == currentContributorSkill.getLevel() + 1 &&
                                    hasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                                contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(currentContributor, currentContributorSkill));
                            }
                            if (!Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == 1 &&
                                    hasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                contributors.get(j).getSkills().add(new Skill(UUID.randomUUID(), currentProjectSkill.getName(), 1));
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                            }
                        }
                    }
                    if (isValidAssignment(currentProject, assignedContributors)) {
                        increaseLevelOfAssignedContributorSkills(contributorsToIncreaseScore, contributorSkillsMap);
                        fullAssignment.setProject(currentProject);
                        fullAssignment.setContributors(getContributors(assignedContributors));
                        break endSearchForThisProject;
                    }
                }
            }
            if (fullAssignment.getProject() != null) {
                fullAssignments.add(fullAssignment);
            }
        }

        return fullAssignments;
    }

    private static boolean isValidAssignment(Project project, List<ContributorWithAssignedSkill> contributorWithAssignedSkills) {
        return contributorWithAssignedSkills.size() == project.getSkills().size();
    }

    private static void increaseLevelOfAssignedContributorSkills(List<ContributorWithAssignedSkill> contributors, Map<UUID, Map<UUID, Skill>> contributorSkillsMap) {
        for (ContributorWithAssignedSkill contributorWithSkill : contributors) {
            Skill skill = contributorSkillsMap.get(contributorWithSkill.getContributor().getId())
                    .get(contributorWithSkill.getAssignedSkill().getId());

            if (skill != null) {
                skill.setLevel(contributorWithSkill.getAssignedSkill().getLevel() + 1);
            }
        }
    }

    private static boolean hasMentor(String skillName, int skillLevel, List<ContributorWithAssignedSkill> contributorWithAssignedSkillList) {
        for (ContributorWithAssignedSkill contributorWithAssignedSkill : contributorWithAssignedSkillList) {
            Skill mentorSkill = contributorWithAssignedSkill.getAssignedSkill();
            if (Objects.equals(mentorSkill.getName(), skillName) && mentorSkill.getLevel() >= skillLevel) {
                return true;
            }
        }
        return false;
    }

    private static List<Contributor> getContributors(List<ContributorWithAssignedSkill> contributorWithAssignedSkillList) {
        return contributorWithAssignedSkillList.stream().map(ContributorWithAssignedSkill::getContributor).collect(Collectors.toList());
    }
}