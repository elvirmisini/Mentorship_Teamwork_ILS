package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    private UUID id;
    private Project project;
    private Map<Integer, Contributor> roleWithContributorMap;

    public Assignment deepCopy() {
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setProject(project);

        Map<Integer, Contributor> copiedRoleWithContributorMap = new HashMap<>();
        for (Integer index : roleWithContributorMap.keySet()) {
            copiedRoleWithContributorMap.put(index, roleWithContributorMap.get(index).deepCopy());
        }
        assignment.setRoleWithContributorMap(copiedRoleWithContributorMap);
        return assignment;
    }

    public static List<Assignment> from(List<NameAssignment> nameAssignments, List<Project> projects, List<Contributor> contributors) {
        List<Assignment> _assignments = new ArrayList<>();

        for (NameAssignment nameAssignment : nameAssignments) {
            Assignment assignment = new Assignment();
            for (Project project : projects) {
                if (Objects.equals(project.getName(), nameAssignment.getProject())) {
                    assignment.setProject(project);
                }
            }
            Map<Integer, Contributor> contributorMap = new HashMap<>();
            for (Contributor contributor : contributors) {
                if (nameAssignment.getAssignedContributors().contains(contributor.getName())) {
                    contributorMap.put(contributorMap.size(), contributor);
                }
            }
            assignment.setRoleWithContributorMap(contributorMap);
            _assignments.add(assignment);
        }

        return _assignments;
    }
}
