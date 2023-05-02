package utilities;

import entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InitialSolver {

    public static List<Assignment> solver(List<Contributor> contributors, List<Project> projects) {
        List<Assignment> assignments = new ArrayList<>();

        for(int i = 0; i < projects.size(); i++) {
//            System.out.println("p=" + i);
            Assignment assignment = new Assignment();
            assignment.setId(UUID.randomUUID());

            Project currentProject = projects.get(i);
            List<Skill> currentProjectSkills = currentProject.getSkills();

            List<ContributorAndAssignedSkill> assignedContributors = new ArrayList<>();
            List<String> assignedContributorIds = new ArrayList<>();
            List<ContributorAndAssignedSkill> contributorsToIncreaseScore = new ArrayList<>();
            List<String> assignedSkillIds = new ArrayList<>();

            endSearchForThisProject:
            for(int j = 0; j < contributors.size(); j++) {
                Contributor currentContributor = contributors.get(j);
                List<Skill> currentContributorSkills = currentContributor.getSkills();

                for(int k = 0; k < currentProjectSkills.size(); k++) {
                    Skill currentProjectSkill = currentProjectSkills.get(k);

                    for(int t = 0; t < currentContributorSkills.size(); t++) {
                        Skill currentContributorSkill = currentContributorSkills.get(t);

                        if(isValidAssignment(currentProject, assignedContributors)) {
                            increaseContributorsScore(contributorsToIncreaseScore);
                            assignment.setProject(ProjectInfo.from(currentProject));
                            assignment.setContributors(ContributorInfo.from(assignedContributors));
                            break endSearchForThisProject;
                        } else {

                            if (Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() <= currentContributorSkill.getLevel() &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorAndAssignedSkill(currentContributor, currentProjectSkill));
                                if(currentProjectSkill.getLevel() == currentContributorSkill.getLevel()) {
                                    contributorsToIncreaseScore.add(new ContributorAndAssignedSkill(currentContributor, currentContributorSkill));
                                }
                            }

                            if(Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == currentContributorSkill.getLevel() + 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorAndAssignedSkill(currentContributor, currentProjectSkill));
                                contributorsToIncreaseScore.add(new ContributorAndAssignedSkill(currentContributor, currentContributorSkill));
                            }
                            if(!Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                contributors.get(j).getSkills().add(new Skill(UUID.randomUUID(), currentProjectSkill.getName(), 1));
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorAndAssignedSkill(currentContributor, currentProjectSkill));
                            }
                        }
                    }
                    if(isValidAssignment(currentProject, assignedContributors)) {
                        increaseContributorsScore(contributorsToIncreaseScore);
                        assignment.setProject(ProjectInfo.from(currentProject));
                        assignment.setContributors(ContributorInfo.from(assignedContributors));
                        break endSearchForThisProject;
                    }
                }

                for(int k = 0; k < currentProjectSkills.size(); k++) {
                    Skill currentProjectSkill = currentProjectSkills.get(k);

                    for(int t = 0; t < currentContributorSkills.size(); t++) {
                        Skill currentContributorSkill = currentContributorSkills.get(t);

                        if(isValidAssignment(currentProject, assignedContributors)) {
                            increaseContributorsScore(contributorsToIncreaseScore);
                            assignment.setProject(ProjectInfo.from(currentProject));
                            assignment.setContributors(ContributorInfo.from(assignedContributors));
                            break endSearchForThisProject;
                        } else {
                            if(Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == currentContributorSkill.getLevel() + 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorAndAssignedSkill(currentContributor, currentProjectSkill));
                                contributorsToIncreaseScore.add(new ContributorAndAssignedSkill(currentContributor, currentContributorSkill));
                            }
                            if(!Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                contributors.get(j).getSkills().add(new Skill(UUID.randomUUID(), currentProjectSkill.getName(), 1));
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorAndAssignedSkill(currentContributor, currentProjectSkill));
                            }

                        }
                    }
                    if(isValidAssignment(currentProject, assignedContributors)) {
                        increaseContributorsScore(contributorsToIncreaseScore);
                        assignment.setProject(ProjectInfo.from(currentProject));
                        assignment.setContributors(ContributorInfo.from(assignedContributors));
                        break endSearchForThisProject;
                    }
                }
            }

            assignments.add(assignment);
        }

        return assignments;
    }

    public static boolean isValidAssignment(Project project, List<ContributorAndAssignedSkill> contributorAndAssignedSkills) {
        if(contributorAndAssignedSkills.size() != project.getSkills().size()) {
            return false;
        }
        return true;
    }

    public static void increaseContributorsScore(List<ContributorAndAssignedSkill> contributorAndSkills) {
        for (int i = 0; i < contributorAndSkills.size(); i++) {
            List<Skill> contributorSkills = contributorAndSkills.get(i).getContributor().getSkills();
            String assignedSkillId = contributorAndSkills.get(i).getAssignedSkill().getId() + "";
            for(int j = 0; j < contributorSkills.size(); j++) {
                String skillId = contributorSkills.get(j).getId() + "";
                if (assignedSkillId.equals(skillId)) {
                    contributorSkills.get(j).setLevel(contributorSkills.get(j).getLevel() + 1);
                }
            }
        }
    }

    public static boolean contributorHasMentor(String skillName, int skillLevel, List<ContributorAndAssignedSkill> assignedContributors ) {
        for(int i = 0; i < assignedContributors.size(); i++) {
            Skill mentorSkill = assignedContributors.get(i).getAssignedSkill();
            if(Objects.equals(mentorSkill.getName(), skillName) && mentorSkill.getLevel() >= skillLevel) {
                return true;
            }
        }
        return false;
    }

}
