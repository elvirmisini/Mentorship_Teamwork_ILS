package utilities;

import entities.*;

import java.util.*;
import java.util.stream.Collectors;

public class Validator {

    public static boolean areAssignmentsValid(List<RawAssignments> rawAssignments, List<Contributor> contributors, List<Project> projects, String outputFile) {
        List<String> assignmentProjectNames = rawAssignments.stream().map(RawAssignments::getProjectName).collect(Collectors.toList());
        List<String> projectNames = projects.stream().map(Project::getName).collect(Collectors.toList());
        if(!checkIfAssignedProjectsExist(assignmentProjectNames, projectNames)) {
            return false;
        }

        if (!checkIsAssignedProjectHasContributors(rawAssignments)) {
            return false;
        }

        List<String> assignmentContributorNames = rawAssignments.stream().map(RawAssignments::getContributorNames)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<String> contributorNames = contributors.stream().map(Contributor::getName).collect(Collectors.toList());
        if(!checkIfAssignedContributorsExist(assignmentContributorNames, contributorNames)) {
            return false;
        }

        if(!checkIfContributorsWorkInOneProjectPerTime(rawAssignments)) {
            return false;
        }

        Map<String, Integer> projectNameAndNrOfSkills = projects.stream().collect(Collectors.toMap(Project::getName, project -> project.getSkills().size()));
        if(!checkIfProjectsHaveTheCorrectNumberOfContributors(rawAssignments, projectNameAndNrOfSkills)) {
            return false;
        }
        // We should only check this for the last assigment because that is the one we write in the file
//        if(!checkIfTheNumberOfAssignedProjectsIsValid(rawAssignments.size(), outputFile)) {
//            return false;
//        }

        if(!areAssignedContributorsToProjectsValid(rawAssignments, contributors, projects)) {
            return false;
        }

        if(!checkIfAssignedContributorsHaveAtLeastOneRequiredProjectSkill(rawAssignments, contributors, projects)) {
            return false;
        }

        return true;
    }

    private static boolean checkIfAssignedProjectsExist(List<String> assignmentProjectNames, List<String> projectNames) {
        for (String assignmentProjectName : assignmentProjectNames) {
            if (!projectNames.contains(assignmentProjectName)) {
                System.out.println("Error. Assigned project " + assignmentProjectName + " does not exist!");
                return false;
            }
        }
        return true;
    }

    private static boolean checkIfAssignedContributorsExist(List<String> assignmentContributorNames, List<String> contributorNames) {
        for (String assignmentContributorName : assignmentContributorNames) {
            if (!contributorNames.contains(assignmentContributorName)) {
                System.out.println("Error. Assigned contributor " + assignmentContributorName + " does not exist!");
                return false;
            }
        }
        return true;
    }

    private static boolean checkIsAssignedProjectHasContributors(List<RawAssignments> rawAssignments) {
        for(RawAssignments assignment : rawAssignments) {
            if (assignment.getContributorNames().size() == 0 || assignment.getContributorNames().contains("")) {
                System.out.println("Error. Assigned project " + assignment.getProjectName() + " does not have contributors!");
                return false;
            }
        }
        return true;
    }

    private static boolean checkIfContributorsWorkInOneProjectPerTime(List<RawAssignments> rawAssignments) {
        for(int i = 0; i < rawAssignments.size(); i++) {
            if(rawAssignments.get(i).getContributorNames().size() != new HashSet<>(rawAssignments.get(i).getContributorNames()).size()) {
                System.out.println("Error. One of contributors in project " + rawAssignments.get(i).getProjectName() + " is working in two positions!");
                return false;
            }
        }
        return true;
    }

    private static boolean checkIfProjectsHaveTheCorrectNumberOfContributors(List<RawAssignments> rawAssignments, Map<String, Integer> projectNameAndNrOfSkills) {
        for(int i = 0; i < rawAssignments.size(); i++) {
            if(rawAssignments.get(i).getContributorNames().size() != projectNameAndNrOfSkills.get(rawAssignments.get(i).getProjectName())) {
                System.out.println("Error. Project " + rawAssignments.get(i).getProjectName() + " has wrong number of contributors!");
                return false;
            }
        }
        return true;
    }

    private static boolean checkIfTheNumberOfAssignedProjectsIsValid(int numberOfAssignedProjects, String outputFile) {
        if(numberOfAssignedProjects != Integer.parseInt(InputReader.readFileContent(outputFile).get(0))) {
            System.out.println("Error. The number of projects (" + Integer.parseInt(InputReader.readFileContent(outputFile).get(0)) + ") written on the output file"
                    + " is not the same as the number of assigned projects (" + numberOfAssignedProjects + ")!");
            return false;
        }
        return true;
    }

    private static boolean checkIfAssignedContributorsHaveAtLeastOneRequiredProjectSkill(List<RawAssignments> rawAssignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Project> projectMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        Map<String, Contributor> contributorMap = contributors.stream().collect(Collectors.toMap(Contributor::getName, contributor -> contributor));

        for(int i = 0; i < rawAssignments.size(); i++) {
            String projectName = rawAssignments.get(i).getProjectName();
            List<Skill> projectSkills = projectMap.get(projectName).getSkills();
            List<String> projectSkillInfos = projectSkills.stream().map(Skill::getName).collect(Collectors.toList());

            List<String> projectContributors = rawAssignments.get(i).getContributorNames();
            for (int j = 0; j < projectContributors.size(); j++) {
                List<Skill> contributorSkills = contributorMap.get(projectContributors.get(j)).getSkills();
                List<String> contributorSkillInfos = contributorSkills.stream().map(Skill::getName).collect(Collectors.toList());

                boolean hasRequiredSkill = false;
                for (int k = 0; k < projectSkillInfos.size(); k++) {
                    String projectSkill = projectSkillInfos.get(k);

                    if (projectMap.get(projectName).getSkills().get(k).getLevel() == 1) {
                        hasRequiredSkill = true;
                        break;
                    }
                    if (contributorSkillInfos.contains(projectSkill)) {
                        hasRequiredSkill = true;
                        break;
                    }

                }

                if (!hasRequiredSkill) {
                    System.out.println("Error. At least one contributor in project " + rawAssignments.get(i).getProjectName()
                            + " has no skill that is required in the project!");
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean areAssignedContributorsToProjectsValid(List<RawAssignments> rawAssignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Contributor> contributorMap = contributors.stream().collect(Collectors.toMap(Contributor::getName, contributor -> contributor));
        Map<String, Project> projectMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));

        List<CompleteAssignment> completeAssignments = new ArrayList<>();

        for(int i = 0; i < rawAssignments.size(); i++) {
            Project project = projectMap.get(rawAssignments.get(i).getProjectName());
            List<Contributor> assignedContributors = new ArrayList<>();
            for(int j = 0; j < rawAssignments.get(i).getContributorNames().size(); j++) {
                assignedContributors.add(contributorMap.get(rawAssignments.get(i).getContributorNames().get(j)));
            }
            completeAssignments.add(new CompleteAssignment(UUID.randomUUID(), project, assignedContributors));
        }

        for(int i = 0; i < completeAssignments.size(); i++) {
            Project project = completeAssignments.get(i).getProject();
            List<Skill> projectSkills = project.getSkills();
            List<Contributor> contributorList = completeAssignments.get(i).getContributors();

            List<String> checkedContributorIds = new ArrayList<>();
            checkedContributorIds.add("");
            List<String> checkedSkillIds = new ArrayList<>();
            checkedSkillIds.add("");
            List<ContributorAndAssignedSkill> contributorsToIncreaseScore = new ArrayList<>();

            int contributorFulfillsAtLeastOneRequiredSkill = 0;

            for(int j = 0; j < contributorList.size(); j++) {
                Contributor contributor = contributorList.get(j);
                String contributorId = contributor.getId() + "";
                List<Skill> contributorSkills = contributor.getSkills();
                List<String> contributorSkillNames = contributorSkills.stream().map(Skill::getName).collect(Collectors.toList());

                for(int k = 0; k < projectSkills.size(); k++) {
                    Skill projectSkill = projectSkills.get(k);

                    for (int t = 0; t < contributorSkills.size(); t++) {
                        Skill contributorSkill = contributorSkills.get(t);

                        boolean hasSkillWithoutMentor = Objects.equals(projectSkill.getName(), contributorSkill.getName()) && projectSkill.getLevel() <= contributorSkill.getLevel();
                        boolean hasSkillWithMentor = Objects.equals(projectSkill.getName(), contributorSkill.getName()) && projectSkill.getLevel() == contributorSkill.getLevel() + 1
                                && contributorHasMentor(projectSkill.getName(), projectSkill.getLevel(), contributorList, contributorId);

                        boolean hasNoSkillButCanCreateItWithMentor = false;
                        if(!contributorSkillNames.contains(projectSkill.getName())) {
                            if(!Objects.equals(projectSkill.getName(), contributorSkill.getName()) && projectSkill.getLevel() == 1) {
                                List<Contributor> contributorListWithoutCurrentContributor = contributorList.stream()
                                        .filter(contributor1 -> !(contributor1.getId() + "").equals(contributor.getId() + ""))
                                        .collect(Collectors.toList());

                                if(contributorHasMentor(projectSkill.getName(), projectSkill.getLevel(), contributorListWithoutCurrentContributor, contributorId)) {
                                    hasNoSkillButCanCreateItWithMentor = true;
                                }
                            }
                        }

                        if(!checkedContributorIds.contains(contributorId) && !checkedSkillIds.contains(projectSkill.getId() + "") && (hasSkillWithoutMentor || hasSkillWithMentor || hasNoSkillButCanCreateItWithMentor)) {
                            checkedContributorIds.add(contributorId);
                            checkedSkillIds.add(projectSkill.getId() + "");
                            if(hasNoSkillButCanCreateItWithMentor) {
                                if(!contributorSkillNames.contains(projectSkill.getName())) {
                                    contributor.getSkills().add(new Skill(UUID.randomUUID(), projectSkill.getName(), 1));
                                    contributorFulfillsAtLeastOneRequiredSkill++;
                                }
                            }
                            else {
                                contributorFulfillsAtLeastOneRequiredSkill++;
                                if(projectSkill.getLevel() == contributorSkill.getLevel() || projectSkill.getLevel() == contributorSkill.getLevel() + 1) {
                                    contributorsToIncreaseScore.add(new ContributorAndAssignedSkill(contributor, contributorSkill));
                                }
                            }
                        }
                    }
                }
            }
            if(contributorFulfillsAtLeastOneRequiredSkill == 0) {
                System.out.println("Error. Contributor does not have any skill for the project " + project.getName());
                return false;
            } else {
                increaseContributorsScore(contributorsToIncreaseScore);
            }
        }
        return true;
    }

    private static boolean contributorHasMentor(String skill, int level, List<Contributor> possibleMentors, String currentContributorId) {
        for(int i = 0; i < possibleMentors.size(); i++) {
            Contributor mentor = possibleMentors.get(i);
            List<Skill> mentorSkills = mentor.getSkills();
            if (!(mentor.getId() + "").equals(currentContributorId)) {
                for (int j = 0; j < mentorSkills.size(); j++) {
                    if(Objects.equals(mentorSkills.get(j).getName(), skill) && mentorSkills.get(j).getLevel() >= level) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void increaseContributorsScore(List<ContributorAndAssignedSkill> contributorAndSkills) {
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
}
