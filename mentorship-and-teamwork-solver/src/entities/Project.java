package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private UUID id;
    private String name;
    private int daysToComplete;
    private int score;
    private int bestBefore;
    private List<Skill> skills;

    public static class ProjectComparator implements Comparator<Project> {
        @Override
        public int compare(Project p1, Project p2) {
            double ratio1 = (double) p1.getSkills().size() / getSkillsSummary(p1.getSkills());
            double ratio2 = (double) p2.getSkills().size() / getSkillsSummary(p2.getSkills());

            return Double.compare(ratio2, ratio1); // Sort in descending order
        }

        private int getSkillsSummary(List<Skill> skills) {
            int summary = 0;
            for (Skill skill : skills) {
                summary += skill.getLevel();
            }
            return summary;
        }
    }
}
