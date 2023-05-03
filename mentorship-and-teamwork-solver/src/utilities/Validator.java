package utilities;

import entities.*;

import java.util.*;
import java.util.stream.Collectors;

public class Validator {

    public static boolean areTheFinalAssignmentsValid(List<NameAssignment> nameAssignments, List<Contributor> contributors, List<Project> projects) {
        List<FullAssignment> assignments = new ArrayList<>();

        for (NameAssignment nameAssignment : nameAssignments) {
            FullAssignment fullAssignment = new FullAssignment();
            fullAssignment.setId(nameAssignment.getId());

            Project project = new Project();
            project.setName(nameAssignment.getProject());
            fullAssignment.setProject(project);

            List<Contributor> contributorList = new ArrayList<>();
            for (int j = 0; j < nameAssignment.getAssignedContributors().size(); j++) {
                Contributor contributor = new Contributor();
                contributor.setName(nameAssignment.getAssignedContributors().get(j));
                contributorList.add(contributor);
            }
            fullAssignment.setContributors(contributorList);

            assignments.add(fullAssignment);
        }

        return areAssignmentsValid(assignments, contributors, projects);
    }

    public static boolean areAssignmentsValid(List<FullAssignment> fullAssignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Project> projectMapByName = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        List<String> contributorNames = contributors.stream().map(Contributor::getName).collect(Collectors.toList());

        for (FullAssignment fullAssignment : fullAssignments) {
            String assignedProject = fullAssignment.getProject().getName();
            List<String> assignedContributors = fullAssignment.getContributorNames();

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

            if (fullAssignment.getContributors().size() != projectMapByName.get(assignedProject).getSkills().size()) {
                System.out.println("Error. Project " + fullAssignment.getProject() + " has wrong number of contributors!");
                return false;
            }
        }

        return areAssignedContributorsToProjectsValidForFullAssignments(fullAssignments, contributors, projects);
    }

    private static boolean areAssignedContributorsToProjectsValidForFullAssignments(List<FullAssignment> assignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Contributor> contributorMap = contributors.stream().collect(Collectors.toMap(Contributor::getName, contributor -> contributor));
        Map<String, Project> projectMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));

        List<FullAssignment> fullAssignments = new ArrayList<>();

        for (int i = 0; i < assignments.size(); i++) {
            Project project = projectMap.get(assignments.get(i).getProject().getName());
            List<Contributor> assignedContributors = new ArrayList<>();
            for (int j = 0; j < assignments.get(i).getContributors().size(); j++) {
                assignedContributors.add(contributorMap.get(assignments.get(i).getContributorNames().get(j)));
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
                System.out.println("Assignment: " + fullAssignments.get(i));
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