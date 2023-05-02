package utilities;

import entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class InitialSolver {

    public static List<FullAssignment> solver(List<Contributor> contributors, List<Project> projects) {
        List<FullAssignment> fullAssignments = new ArrayList<>();

        for(int i = 0; i < projects.size(); i++) {
            FullAssignment fullAssignment = new FullAssignment();
            fullAssignment.setId(UUID.randomUUID());

            Project currentProject = projects.get(i);
            List<Skill> currentProjectSkills = currentProject.getSkills();

            List<ContributorWithAssignedSkill> assignedContributors = new ArrayList<>();
            List<String> assignedContributorIds = new ArrayList<>();
            List<ContributorWithAssignedSkill> contributorsToIncreaseScore = new ArrayList<>();
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
                                if(currentProjectSkill.getLevel() == currentContributorSkill.getLevel()) {
                                    contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(currentContributor, currentContributorSkill));
                                }
                            }

                            if(Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == currentContributorSkill.getLevel() + 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                                contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(currentContributor, currentContributorSkill));
                            }
                            if(!Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                contributors.get(j).getSkills().add(new Skill(UUID.randomUUID(), currentProjectSkill.getName(), 1));
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                            }
                        }
                    }
                    if(isValidAssignment(currentProject, assignedContributors)) {
                        increaseContributorsScore(contributorsToIncreaseScore);
                        fullAssignment.setProject(currentProject);
                        fullAssignment.setContributors(getContributors(assignedContributors));
                        break endSearchForThisProject;
                    }
                }

                for(int k = 0; k < currentProjectSkills.size(); k++) {
                    Skill currentProjectSkill = currentProjectSkills.get(k);

                    for(int t = 0; t < currentContributorSkills.size(); t++) {
                        Skill currentContributorSkill = currentContributorSkills.get(t);

                        if(isValidAssignment(currentProject, assignedContributors)) {
                            increaseContributorsScore(contributorsToIncreaseScore);
                            fullAssignment.setProject(currentProject);
                            fullAssignment.setContributors(getContributors(assignedContributors));
                            break endSearchForThisProject;
                        } else {
                            if(Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == currentContributorSkill.getLevel() + 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                                contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(currentContributor, currentContributorSkill));
                            }
                            if(!Objects.equals(currentProjectSkill.getName(), currentContributorSkill.getName()) &&
                                    currentProjectSkill.getLevel() == 1 &&
                                    contributorHasMentor(currentProjectSkill.getName(), currentProjectSkill.getLevel(), assignedContributors) &&
                                    !assignedContributorIds.contains(currentContributor.getId() + "") &&
                                    !assignedSkillIds.contains(currentProjectSkill.getId() + "")) {

                                assignedContributorIds.add(currentContributor.getId() + "");
                                contributors.get(j).getSkills().add(new Skill(UUID.randomUUID(), currentProjectSkill.getName(), 1));
                                assignedSkillIds.add(currentProjectSkill.getId() + "");
                                assignedContributors.add(new ContributorWithAssignedSkill(currentContributor, currentProjectSkill));
                            }

                        }
                    }
                    if(isValidAssignment(currentProject, assignedContributors)) {
                        increaseContributorsScore(contributorsToIncreaseScore);
                        fullAssignment.setProject(currentProject);
                        fullAssignment.setContributors(getContributors(assignedContributors));
                        break endSearchForThisProject;
                    }
                }
            }
            if(fullAssignment.getProject() != null) {
                fullAssignments.add(fullAssignment);
            }
        }

        return fullAssignments;
    }

    public static boolean isValidAssignment(Project project, List<ContributorWithAssignedSkill> contributorWithAssignedSkills) {
        if(contributorWithAssignedSkills.size() != project.getSkills().size()) {
            return false;
        }
        return true;
    }

    public static void increaseContributorsScore(List<ContributorWithAssignedSkill> contributorAndSkills) {
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

    public static boolean contributorHasMentor(String skillName, int skillLevel, List<ContributorWithAssignedSkill> assignedContributors ) {
        for(int i = 0; i < assignedContributors.size(); i++) {
            Skill mentorSkill = assignedContributors.get(i).getAssignedSkill();
            if(Objects.equals(mentorSkill.getName(), skillName) && mentorSkill.getLevel() >= skillLevel) {
                return true;
            }
        }
        return false;
    }

    private static List<Contributor> getContributors(List<ContributorWithAssignedSkill> contributorWithAssignedSkills) {
        return contributorWithAssignedSkills.stream().map(ContributorWithAssignedSkill::getContributor).collect(Collectors.toList());
    }

}
