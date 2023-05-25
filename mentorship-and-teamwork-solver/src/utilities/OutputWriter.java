package utilities;

import entities.Assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputWriter {

    public static void writeContent(List<Assignment> assignments, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(assignments.size() + "\n");

        for (Assignment assignment : assignments) {
            writer.write(assignment.getProject().getName() + "\n");
            for (Integer index : assignment.getRoleWithContributorMap().keySet()) {
                if(index != assignment.getRoleWithContributorMap().keySet().size())
                    writer.write(assignment.getRoleWithContributorMap().get(index).getName() + " ");
                else
                    writer.write(assignment.getRoleWithContributorMap().get(index).getName());
            }
            writer.write("\n");
        }
        writer.close();

        System.out.println("\nWrote the solution in the file " + fileName);
    }
}
