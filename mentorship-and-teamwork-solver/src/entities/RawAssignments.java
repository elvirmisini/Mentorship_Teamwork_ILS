package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RawAssignments {
    private UUID id;
    private String projectName;
    private List<String> contributorNames;

    public RawAssignments() {
    }

    public RawAssignments(UUID id, String projectName, List<String> contributorNames) {
        this.id = id;
        this.projectName = projectName;
        this.contributorNames = contributorNames;
    }

    public static RawAssignments from(Assignment assignment) {
        RawAssignments rawAssignments = new RawAssignments();
        rawAssignments.setId(assignment.getId());
        rawAssignments.setProjectName(assignment.getProject().getName());
        rawAssignments.setContributorNames(assignment.getContributorNames());
        return rawAssignments;
    }

    public static List<RawAssignments> from(List<Assignment> assignments) {
        List<RawAssignments> rawAssignments = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            if(assignments.get(i) != null) {
                if(assignments.get(i).getProject() != null && assignments.get(i).getContributors() != null) {
                    rawAssignments.add(from(assignments.get(i)));
                }
            }
        }
        return rawAssignments;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getContributorNames() {
        return contributorNames;
    }

    public void setContributorNames(List<String> contributorNames) {
        this.contributorNames = contributorNames;
    }

    @Override
    public String toString() {
        return "RawAssignments{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", contributorNames=" + contributorNames +
                '}';
    }
}
