package entities;

public class ContributorWithAssignedSkill {
    private Contributor contributor;
    private Skill assignedSkill;

    public ContributorWithAssignedSkill() {
    }

    public ContributorWithAssignedSkill(Contributor contributor, Skill assignedSkill) {
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
}
