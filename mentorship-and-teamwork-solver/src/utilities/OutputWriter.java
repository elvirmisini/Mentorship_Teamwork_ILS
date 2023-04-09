package utilities;

import entities.Assignment;
import entities.AssignmentInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputWriter {

    public static void writeContent(List<Assignment> assignments, String fileName) throws IOException {
        List<AssignmentInfo> correctAssignments = new ArrayList<>();

        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getProject() != null) {
                correctAssignments.add(new AssignmentInfo(assignments.get(i).getProject().getName(),
                        assignments.get(i).getContributorNames()));
            }
        }

        FileWriter writer = new FileWriter(fileName);
        writer.write(correctAssignments.size() + "\n");

        for (int i = 0; i < correctAssignments.size(); i++) {
            writer.write(correctAssignments.get(i).getProjectName() + "\n");
            for (String contributorName : correctAssignments.get(i).getAssignedContributors()) {
                writer.write(contributorName + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }
}
