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
public class NameAssignment {
    private UUID id;
    private String project;
    private List<String> assignedContributors;

    public NameAssignment(String project, List<String> assignedContributors) {
        this.project = project;
        this.assignedContributors = assignedContributors;
    }

    public static NameAssignment from(FullAssignment fullAssignment) {
        NameAssignment rawAssignments = new NameAssignment();
        rawAssignments.setId(fullAssignment.getId());
        rawAssignments.setProject(fullAssignment.getProject().getName());
        rawAssignments.setAssignedContributors(fullAssignment.getContributorNames());
        return rawAssignments;
    }

    public static List<NameAssignment> from(List<FullAssignment> fullAssignments) {
        List<NameAssignment> rawAssignments = new ArrayList<>();
        for (int i = 0; i < fullAssignments.size(); i++) {
            if(fullAssignments.get(i) != null) {
                if(fullAssignments.get(i).getProject() != null && fullAssignments.get(i).getContributors() != null) {
                    rawAssignments.add(from(fullAssignments.get(i)));
                }
            }
        }
        return rawAssignments;
    }
}