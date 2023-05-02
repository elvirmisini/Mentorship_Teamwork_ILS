package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullAssignment {
    private UUID id;
    private Project project;
    private List<Contributor> contributors;

    public List<String> getContributorNames() {
        return contributors.stream().map(Contributor::getName).collect(Collectors.toList());
    }
}
