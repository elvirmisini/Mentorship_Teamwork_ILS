package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Assignment {
    private UUID id;
    private ProjectInfo project;
    private List<ContributorInfo> contributors;

    public Assignment() {
    }

    public Assignment(UUID id, ProjectInfo project, List<ContributorInfo> contributors) {
        this.id = id;
        this.project = project;
        this.contributors = contributors;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public List<ContributorInfo> getContributors() {
        return contributors;
    }

    public List<String> getContributorNames() {
        List<String> contributorNames = new ArrayList<>();
        for(int i = 0; i < contributors.size(); i++) {
            contributorNames.add(contributors.get(i).getName());
        }
        return contributorNames;
    }

    public void setContributors(List<ContributorInfo> contributors) {
        this.contributors = contributors;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", project=" + project +
                ", contributors=" + contributors +
                '}';
    }
}
