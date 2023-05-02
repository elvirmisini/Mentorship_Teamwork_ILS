package utilities;

import entities.*;

import java.util.*;
import java.util.stream.Collectors;

public class Validator {

    public static boolean areAssignmentsValid(List<NameAssignment> nameAssignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Project> projectMapByName = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        List<String> contributorNames = contributors.stream().map(Contributor::getName).collect(Collectors.toList());

        for (NameAssignment nameAssignment : nameAssignments) {
            String assignedProject = nameAssignment.getProject();
            List<String> assignedContributors = nameAssignment.getAssignedContributors();

            if (!projectMapByName.containsKey(assignedProject)) {
                System.out.println("Error. Assigned project " + assignedProject + " does not exist!");
                return false;
            }

            if (!contributorNames.containsAll(assignedContributors)) {
                System.out.println("Error. One or more assigned contributor " + assignedContributors + " do not exist!");
                return false;
            }

            if (assignedContributors.size() == 0 || assignedContributors.contains("")) {
                System.out.println("Error. Assigned project " + assignedProject + " does not have contributors!");
                return false;
            }

            if (assignedContributors.size() != new HashSet<>(assignedContributors).size()) {
                System.out.println("Error. One of contributors in project " + assignedProject + " is working in two positions!");
                return false;
            }

            if (nameAssignment.getAssignedContributors().size() != projectMapByName.get(assignedProject).getSkills().size()) {
                System.out.println("Error. Project " + nameAssignment.getProject() + " has wrong number of contributors!");
                return false;
            }
        }

        return areAssignedContributorsToProjectsValid(nameAssignments, contributors, projects);
    }

    private static boolean areAssignedContributorsToProjectsValid(List<NameAssignment> rawAssignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Contributor> contributorMap = contributors.stream().collect(Collectors.toMap(Contributor::getName, contributor -> contributor));
        Map<String, Project> projectMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));

        List<FullAssignment> fullAssignments = new ArrayList<>();

        for (int i = 0; i < rawAssignments.size(); i++) {
            Project project = projectMap.get(rawAssignments.get(i).getProject());
            List<Contributor> assignedContributors = new ArrayList<>();
            for (int j = 0; j < rawAssignments.get(i).getAssignedContributors().size(); j++) {
                assignedContributors.add(contributorMap.get(rawAssignments.get(i).getAssignedContributors().get(j)));
            }
            fullAssignments.add(new FullAssignment(UUID.randomUUID(), project, assignedContributors));
        }

        for (int i = 0; i < fullAssignments.size(); i++) {
            Project project = fullAssignments.get(i).getProject();
            List<Skill> projectSkills = project.getSkills();
            List<Contributor> contributorList = fullAssignments.get(i).getContributors();

            List<String> checkedContributorIds = new ArrayList<>();
            checkedContributorIds.add("");
            List<String> checkedSkillIds = new ArrayList<>();
            checkedSkillIds.add("");
            List<ContributorWithAssignedSkill> contributorsToIncreaseScore = new ArrayList<>();

            int contributorFulfillsAtLeastOneRequiredSkill = 0;

            for (int j = 0; j < contributorList.size(); j++) {
                Contributor contributor = contributorList.get(j);
                String contributorId = contributor.getId() + "";
                List<Skill> contributorSkills = contributor.getSkills();
                List<String> contributorSkillNames = contributorSkills.stream().map(Skill::getName).collect(Collectors.toList());

                for (int k = 0; k < projectSkills.size(); k++) {
                    Skill projectSkill = projectSkills.get(k);

                    for (int t = 0; t < contributorSkills.size(); t++) {
                        Skill contributorSkill = contributorSkills.get(t);

                        boolean hasSkillWithoutMentor = Objects.equals(projectSkill.getName(), contributorSkill.getName()) && projectSkill.getLevel() <= contributorSkill.getLevel();
                        boolean hasSkillWithMentor = Objects.equals(projectSkill.getName(), contributorSkill.getName()) && projectSkill.getLevel() == contributorSkill.getLevel() + 1 && contributorHasMentor(projectSkill.getName(), projectSkill.getLevel(), contributorList, contributorId);

                        boolean hasNoSkillButCanCreateItWithMentor = false;
                        if (!contributorSkillNames.contains(projectSkill.getName())) {
                            if (!Objects.equals(projectSkill.getName(), contributorSkill.getName()) && projectSkill.getLevel() == 1) {
                                List<Contributor> contributorListWithoutCurrentContributor = contributorList.stream().filter(contributor1 -> !(contributor1.getId() + "").equals(contributor.getId() + "")).collect(Collectors.toList());

                                if (contributorHasMentor(projectSkill.getName(), projectSkill.getLevel(), contributorListWithoutCurrentContributor, contributorId)) {
                                    hasNoSkillButCanCreateItWithMentor = true;
                                }
                            }
                        }

                        if (!checkedContributorIds.contains(contributorId) && !checkedSkillIds.contains(projectSkill.getId() + "") && (hasSkillWithoutMentor || hasSkillWithMentor || hasNoSkillButCanCreateItWithMentor)) {
                            checkedContributorIds.add(contributorId);
                            checkedSkillIds.add(projectSkill.getId() + "");
                            if (hasNoSkillButCanCreateItWithMentor) {
                                if (!contributorSkillNames.contains(projectSkill.getName())) {
                                    contributor.getSkills().add(new Skill(UUID.randomUUID(), projectSkill.getName(), 1));
                                    contributorFulfillsAtLeastOneRequiredSkill++;
                                }
                            } else {
                                contributorFulfillsAtLeastOneRequiredSkill++;
                                if (projectSkill.getLevel() == contributorSkill.getLevel() || projectSkill.getLevel() == contributorSkill.getLevel() + 1) {
                                    contributorsToIncreaseScore.add(new ContributorWithAssignedSkill(contributor, contributorSkill));
                                }
                            }
                        }
                    }
                }
            }
            if (contributorFulfillsAtLeastOneRequiredSkill == 0) {
                System.out.println("Error. Contributor does not have any skill for the project " + project.getName());
                return false;
            } else {
                increaseContributorsScore(contributorsToIncreaseScore);
            }
        }
        return true;
    }

    private static boolean contributorHasMentor(String skill, int level, List<Contributor> possibleMentors, String currentContributorId) {
        for (int i = 0; i < possibleMentors.size(); i++) {
            Contributor mentor = possibleMentors.get(i);
            List<Skill> mentorSkills = mentor.getSkills();
            if (!(mentor.getId() + "").equals(currentContributorId)) {
                for (int j = 0; j < mentorSkills.size(); j++) {
                    if (Objects.equals(mentorSkills.get(j).getName(), skill) && mentorSkills.get(j).getLevel() >= level) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void increaseContributorsScore(List<ContributorWithAssignedSkill> contributorAndSkills) {
        for (int i = 0; i < contributorAndSkills.size(); i++) {
            List<Skill> contributorSkills = contributorAndSkills.get(i).getContributor().getSkills();
            String assignedSkillId = contributorAndSkills.get(i).getAssignedSkill().getId() + "";
            for (int j = 0; j < contributorSkills.size(); j++) {
                String skillId = contributorSkills.get(j).getId() + "";
                if (assignedSkillId.equals(skillId)) {
                    contributorSkills.get(j).setLevel(contributorSkills.get(j).getLevel() + 1);
                }
            }
        }
    }
}