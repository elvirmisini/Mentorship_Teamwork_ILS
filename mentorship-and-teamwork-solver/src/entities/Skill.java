package entities;

import java.util.UUID;

public class Skill {
    private UUID id;
    private String name;
    private int level;

    public Skill() {
    }

    public Skill(UUID id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    public Skill deepCopy() {
        Skill newSkill = new Skill();
        newSkill.setId(id);
        newSkill.setName(name);
        newSkill.setLevel(level);
        return newSkill;
    }
}
