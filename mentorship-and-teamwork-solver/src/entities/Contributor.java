package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contributor {
    private UUID id;
    private String name;
    private List<Skill> skills;

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

    public int getHighestSkillLevel() {
        int highestLevel = 0;
        for(Skill skill : skills) {
            if(skill.getLevel() > highestLevel) {
                highestLevel = skill.getLevel();
            }
        }
        return highestLevel;
    }

    public double getCombinedScore() {
        return 0.5 * getHighestSkillLevel() + 0.5 * skills.size();
    }
}
