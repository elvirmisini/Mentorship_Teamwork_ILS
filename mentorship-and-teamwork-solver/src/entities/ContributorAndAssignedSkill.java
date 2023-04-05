package entities;

public class ContributorAndAssignedSkill {
    private Contributor contributor;
    private Skill assignedSkill;

    public ContributorAndAssignedSkill() {
    }

    public ContributorAndAssignedSkill(Contributor contributor, Skill assignedSkill) {
        this.contributor = contributor;
        this.assignedSkill = assignedSkill;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public Skill getAssignedSkill() {
        return assignedSkill;
    }

    public void setAssignedSkill(Skill assignedSkill) {
        this.assignedSkill = assignedSkill;
    }

    @Override
    public String toString() {
        return "ContributorAndAssignedSkill{" +
                "contributor=" + contributor +
                ", assignedSkill=" + assignedSkill +
                '}';
    }
}
