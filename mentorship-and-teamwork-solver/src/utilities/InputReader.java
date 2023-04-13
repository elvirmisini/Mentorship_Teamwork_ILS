package utilities;

import entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class InputReader {

    public static List<String> readFileName() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Choose the file.\na, b, c, d, e, f, class\nYour choice: ");
        String chosenFile = input.nextLine();
//        String absoluteInputFilePath = "C:\\Users\\Elvir Misini\\Desktop\\Mentorship_Teamwork_ILS\\mentorship-and-teamwork-solver\\src\\input_files\\";
//        String absoluteOutputFilePath = "C:\\Users\\Elvir Misini\\Desktop\\Mentorship_Teamwork_ILS\\mentorship-and-teamwork-solver\\src\\output_files\\";
         String absoluteInputFilePath = "src\\input_files\\";
         String absoluteOutputFilePath = "src\\output_files\\";

        if (Objects.equals(chosenFile, "a")) {
            absoluteInputFilePath += "a_an_example.in.txt";
            absoluteOutputFilePath += "a.txt";
        } else if (Objects.equals(chosenFile, "b")) {
            absoluteInputFilePath += "b_better_start_small.in.txt";
            absoluteOutputFilePath += "b.txt";
        } else if (Objects.equals(chosenFile, "c")) {
            absoluteInputFilePath += "c_collaboration.in.txt";
            absoluteOutputFilePath += "c.txt";
        } else if (Objects.equals(chosenFile, "d")) {
            absoluteInputFilePath += "d_dense_schedule.in.txt";
            absoluteOutputFilePath += "d.txt";
        } else if (Objects.equals(chosenFile, "e")) {
            absoluteInputFilePath += "e_exceptional_skills.in.txt";
            absoluteOutputFilePath += "e.txt";
        } else if (Objects.equals(chosenFile, "f")) {
            absoluteInputFilePath += "f_find_great_mentors.in.txt";
            absoluteOutputFilePath += "f.txt";
        } else if (Objects.equals(chosenFile, "class")) {
            absoluteInputFilePath += "class_task.in.txt";
            absoluteOutputFilePath += "class.txt";
        } else {
            throw new Exception("Wrong input/output for file name");
        }

        return List.of(absoluteInputFilePath, absoluteOutputFilePath);
    }

    public static List<String> readFileContent(String fileName) {
        try {
            return Files.readAllLines(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<Contributor> readContributors(List<String> fileContents) {
        int numberOfContributors = Integer.parseInt(fileContents.get(0).split(" ")[0]);
        List<Contributor> contributors = new ArrayList<>();
        int i = 1;
        while (i <= numberOfContributors) {
            Contributor contributor = new Contributor();
            contributor.setId(UUID.randomUUID());
            String[] nameAndNrOfSkills = fileContents.get(i).split(" ");
            contributor.setName(nameAndNrOfSkills[0]);
            int numberOfSkills = Integer.parseInt(nameAndNrOfSkills[1]);

            List<Skill> skills = new ArrayList<>();
            for (int j = 1; j <= numberOfSkills; j++) {
                Skill skill = new Skill();
                String[] skillAndLevel = fileContents.get(i + j).split(" ");
                skill.setId(UUID.randomUUID());
                skill.setName(skillAndLevel[0]);
                skill.setLevel(Integer.parseInt(skillAndLevel[1]));
                skills.add(skill);
            }

            contributor.setSkills(skills);
            contributors.add(contributor);

            numberOfContributors += numberOfSkills;
            i += numberOfSkills + 1;
        }
        return contributors;
    }

    public static List<Project> readProjects(List<String> fileContents) {
        int numberOfProjects = Integer.parseInt(fileContents.get(0).split(" ")[1]);

        int lineStartOfProjectsInContent = 0;
        for (int i = 0; i < fileContents.size(); i++) {
            String[] content = fileContents.get(i).split(" ");
            if (content.length == 5) {
                lineStartOfProjectsInContent = i;
                break;
            }
        }
        List<String> fileContentsForProjects = fileContents.subList(lineStartOfProjectsInContent, fileContents.size());
        List<Project> projectList = new ArrayList<>();
        int i = 0;
        while (i < numberOfProjects) {
            Project project = new Project();
            project.setId(UUID.randomUUID());
            String[] projectInfo = fileContentsForProjects.get(i).split(" ");
            project.setName(projectInfo[0]);
            project.setDaysToComplete(Integer.parseInt(projectInfo[1]));
            project.setScore(Integer.parseInt(projectInfo[2]));
            project.setBestBefore(Integer.parseInt(projectInfo[3]));
            int numberOfSkills = Integer.parseInt(projectInfo[4]);

            List<Skill> skills = new ArrayList<>();
            for (int j = 1; j <= numberOfSkills; j++) {
                Skill skill = new Skill();
                skill.setId(UUID.randomUUID());
                String[] skillAndLevel = fileContentsForProjects.get(i + j).split(" ");
                skill.setName(skillAndLevel[0]);
                skill.setLevel(Integer.parseInt(skillAndLevel[1]));
                skills.add(skill);
            }
            project.setSkills(skills);
            projectList.add(project);
            numberOfProjects += numberOfSkills;
            i += numberOfSkills + 1;
        }
        return projectList;
    }

    public static List<RawAssignments> readRawAssignments(String filename) {
        List<RawAssignments> rawAssignments = new ArrayList<>();
        List<String> fileContents = readFileContent(filename);
        for (int i = 1, j = 2; i < fileContents.size(); i = i + 2, j = j + 2) {
            RawAssignments assignment = new RawAssignments();
            assignment.setProjectName(fileContents.get(i));
            assignment.setId(UUID.randomUUID());
            String[] contributorNames = fileContents.get(j).split(" ");
            List<String> assignmentContributors = new ArrayList<>(Arrays.asList(contributorNames));
            assignment.setContributorNames(assignmentContributors);
            rawAssignments.add(assignment);
        }
        return rawAssignments;
    }
}
