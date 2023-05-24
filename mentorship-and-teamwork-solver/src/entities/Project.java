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
public class Project {
    private UUID id;
    private String name;
    private int score;
    private int bestBefore;
    private int daysToComplete;
    private List<Skill> skills;

    public Project deepCopy() {
        Project newProject = new Project();
        newProject.setId(id);
        newProject.setName(name);
        newProject.setDaysToComplete(daysToComplete);
        newProject.setScore(score);
        newProject.setBestBefore(bestBefore);
        List<Skill> copiedSkills = new ArrayList<>();
        for (Skill skill : skills) {
            copiedSkills.add(skill.deepCopy());
        }
        newProject.setSkills(copiedSkills);
        return newProject;
    }

    public double getPriorityScore() {
        return (double) score / (bestBefore * daysToComplete);
    }

    public double getAverageSkillLevel() {
        int total = 0;
        for (Skill skill : skills) {
            total += skill.getLevel();
        }
        return (double) total / skills.size();
    }

    public int getSkillLevels() {
        return skills.stream().mapToInt(Skill::getLevel).sum();
    }
}
