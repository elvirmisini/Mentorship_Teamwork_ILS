package entities;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Project {
    private UUID id;
    private String name;
    private int daysToComplete;
    private int score;
    private int bestBefore;
    private List<Skill> skills;

    public Project() {
    }

    public Project(UUID id, String name, int daysToComplete, int score, int bestBefore, List<Skill> skills) {
        this.id = id;
        this.name = name;
        this.daysToComplete = daysToComplete;
        this.score = score;
        this.bestBefore = bestBefore;
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

    public int getDaysToComplete() {
        return daysToComplete;
    }

    public void setDaysToComplete(int daysToComplete) {
        this.daysToComplete = daysToComplete;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(int bestBefore) {
        this.bestBefore = bestBefore;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", daysToComplete=" + daysToComplete +
                ", score=" + score +
                ", bestBefore=" + bestBefore +
                ", skills=" + skills +
                '}';
    }

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
