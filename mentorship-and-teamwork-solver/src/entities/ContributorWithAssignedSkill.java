package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributorWithAssignedSkill {
    private Contributor contributor;
    private Skill assignedSkill;
}