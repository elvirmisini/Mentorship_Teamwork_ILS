
package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contributor {
    private UUID id;
    private String name;
    private List<Skill> skills;

    public Contributor() {
    }

    public Contributor(UUID id, String name, List<Skill> skills) {
        this.id = id;
        this.name = name;
        this.skills = skills;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Contributor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                '}';
    }

    public Contributor deepCopy() {
        Contributor newContributor = new Contributor();
        newContributor.setId(id);
        newContributor.setName(name);
        List<Skill> copiedSkills = new ArrayList<>();
        for (Skill skill : skills) {
            copiedSkills.add(skill.deepCopy());
        }
        newContributor.setSkills(copiedSkills);
        return newContributor;
    }
}
