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

    public double getAverageSkillLevel() {
        double sumLevels = 0;
        for(Skill skill : skills) {
            sumLevels += skill.getLevel();
        }
        return (skills.size() > 0) ? sumLevels / skills.size() : 0;
    }

    public double getCombinedScore() {
        return 0.6 * getAverageSkillLevel() + 0.4 * skills.size();
    }
}
