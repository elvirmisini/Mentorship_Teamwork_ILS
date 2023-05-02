package utilities;

import entities.FullAssignment;
import entities.NameAssignment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputWriter {

    public static void writeContent(List<FullAssignment> fullAssignments, String fileName) throws IOException {
        List<NameAssignment> correctAssignments = new ArrayList<>();

        for (int i = 0; i < fullAssignments.size(); i++) {
            if (fullAssignments.get(i).getProject() != null) {
                correctAssignments.add(new NameAssignment(fullAssignments.get(i).getProject().getName(), fullAssignments.get(i).getContributorNames()));
            }
        }

        FileWriter writer = new FileWriter(fileName);
        writer.write(correctAssignments.size() + "\n");

        for (int i = 0; i < correctAssignments.size(); i++) {
            writer.write(correctAssignments.get(i).getProject() + "\n");
            for (String contributorName : correctAssignments.get(i).getAssignedContributors()) {
                writer.write(contributorName + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }
}
