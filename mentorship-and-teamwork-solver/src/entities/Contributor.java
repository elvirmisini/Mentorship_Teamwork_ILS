package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contributor {
    private UUID id;
    private String name;
    private List<Skill> skills;

    public int getSkillLevel(String skillName) {
        for (Skill skill : skills) {
            if (skill.getName().equals(skillName)) {
                return skill.getLevel();
            }
        }
        return 0;
    }
}
