package entities;

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
