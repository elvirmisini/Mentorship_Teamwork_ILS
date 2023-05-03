package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}