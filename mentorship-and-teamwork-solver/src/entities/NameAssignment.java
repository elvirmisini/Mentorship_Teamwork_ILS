package entities;

import java.util.List;
import java.util.UUID;

public class NameAssignment {
    private UUID id;
    private String project;
    private List<String> assignedContributors;

    public NameAssignment() {
    }

    public NameAssignment(String project, List<String> assignedContributors) {
        this.project = project;
        this.assignedContributors = assignedContributors;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<String> getAssignedContributors() {
        return assignedContributors;
    }

    public void setAssignedContributors(List<String> assignedContributors) {
        this.assignedContributors = assignedContributors;
    }
}
