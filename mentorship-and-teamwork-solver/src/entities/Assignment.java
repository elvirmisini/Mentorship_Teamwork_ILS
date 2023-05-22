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
}
