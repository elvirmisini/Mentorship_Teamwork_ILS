package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContributorInfo {
    private UUID id;
    private String name;

    public ContributorInfo() {
    }

    public ContributorInfo(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ContributorInfo from(Contributor contributor) {
        ContributorInfo contributorInfo = new ContributorInfo();
        contributorInfo.id = contributor.getId();
        contributorInfo.name = contributor.getName();
        return contributorInfo;
    }

    public static List<ContributorInfo> from(List<ContributorAndAssignedSkill> assignedContributors) {
        List<ContributorInfo> contributorInfoList = new ArrayList<>();
        for (int i = 0; i < assignedContributors.size(); i++) {
            contributorInfoList.add(from(assignedContributors.get(i).getContributor()));
        }
        return contributorInfoList;
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

    @Override
    public String toString() {
        return "ContributorInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
