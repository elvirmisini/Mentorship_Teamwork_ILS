package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Validator {

    public static boolean areAssignmentsValid(List<Assignment> assignments, List<Contributor> contributors, List<Project> projects) {
        Map<String, Project> projectNamesMap = projects.stream().collect(Collectors.toMap(Project::getName, project -> project));
        List<String> contributorNames = contributors.stream().map(Contributor::getName).collect(Collectors.toList());

        for (Assignment assignment : assignments) {
            String assignedProject = assignment.getProject().getName();
            List<String> assignedContributors = assignment.getRoleWithContributorMap().values().stream().map(Contributor::getName).collect(Collectors.toList());

            if (!projectNamesMap.containsKey(assignedProject)) {
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

            if (assignment.getRoleWithContributorMap().size() != projectNamesMap.get(assignedProject).getSkills().size()) {
                System.out.println("Error. Project " + assignment.getProject() + " has wrong number of contributors!");
                return false;
            }
        }
        return true;
    }
}