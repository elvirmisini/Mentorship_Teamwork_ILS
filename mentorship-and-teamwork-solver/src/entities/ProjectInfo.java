package entities;

import java.util.UUID;

public class ProjectInfo {
    private UUID id;
    private String name;

    public ProjectInfo() {
    }

    public ProjectInfo(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ProjectInfo from(Project project) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.id = project.getId();
        projectInfo.name = project.getName();
        return projectInfo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
