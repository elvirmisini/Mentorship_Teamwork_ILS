package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FullAssignment {
    private UUID id;
    private Project project;
    private List<Contributor> contributors;

    public FullAssignment() {
    }

    public FullAssignment(UUID id, Project project, List<Contributor> contributors) {
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public List<String> getContributorNames() {
        return contributors.stream().map(Contributor::getName).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "FullAssignment{" +
                "id=" + id +
                ", project=" + project +
                ", contributors=" + contributors +
                '}';
    }

    public FullAssignment deepCopy() {
        FullAssignment fullAssignment = new FullAssignment();
        fullAssignment.setId(id);
        fullAssignment.setProject(project);

        List<Contributor> copiedContributors = new ArrayList<>();
        for (Contributor contributor : contributors) {
            copiedContributors.add(contributor.deepCopy());
        }
        fullAssignment.setContributors(copiedContributors);
        return fullAssignment;
    }
}
