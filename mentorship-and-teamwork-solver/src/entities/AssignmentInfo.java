package entities;

import java.util.List;

public class AssignmentInfo {
    private String projectName;
    private List<String> assignedContributors;

    public AssignmentInfo() {
    }

    public AssignmentInfo(String projectName, List<String> assignedContributors) {
        this.projectName = projectName;
        this.assignedContributors = assignedContributors;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getAssignedContributors() {
        return assignedContributors;
    }

    public void setAssignedContributors(List<String> assignedContributors) {
        this.assignedContributors = assignedContributors;
    }

    @Override
    public String toString() {
        return "AssignmentInfo{" +
                "projectName='" + projectName + '\'' +
                ", assignedContributors=" + assignedContributors +
                '}';
    }
}
