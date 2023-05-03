package utilities;

import entities.FullAssignment;
import entities.NameAssignment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputWriter {

    public static void writeContent(List<FullAssignment> fullAssignments, String fileName) throws IOException {
        List<NameAssignment> nameAssignments = new ArrayList<>();

        for (FullAssignment fullAssignment : fullAssignments) {
            if (fullAssignment.getProject() != null) {
                nameAssignments.add(new NameAssignment(fullAssignment.getProject().getName(), fullAssignment.getContributorNames()));
            }
        }

        FileWriter writer = new FileWriter(fileName);
        writer.write(nameAssignments.size() + "\n");

        for (NameAssignment nameAssignment : nameAssignments) {
            writer.write(nameAssignment.getProject() + "\n");
            for (String contributorName : nameAssignment.getAssignedContributors()) {
                writer.write(contributorName + " ");
            }
            writer.write("\n");
        }
        writer.close();

        System.out.println("Wrote assignments\n");
    }
}
