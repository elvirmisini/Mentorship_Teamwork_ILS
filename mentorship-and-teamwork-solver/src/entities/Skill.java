package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private UUID id;
    private String name;
    private int level;

    public Skill deepCopy() {
        Skill newSkill = new Skill();
        newSkill.setId(id);
        newSkill.setName(name);
        newSkill.setLevel(level);
        return newSkill;
    }
}
