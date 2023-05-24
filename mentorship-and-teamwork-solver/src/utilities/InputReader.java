package utilities;

import entities.Contributor;
import entities.NameAssignment;
import entities.Project;
import entities.Skill;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class InputReader {

    public static List<String> readFileName(String chosenFile) throws Exception {
        String absoluteInputFilePath = "C:\\Users\\uran_\\Desktop\\Algoritmet e Inspiruara nga Natyra\\Mentorship_Teamwork_ILS\\mentorship-and-teamwork-solver\\src\\input_files\\";
        String absoluteOutputFilePath = "C:\\Users\\uran_\\Desktop\\Algoritmet e Inspiruara nga Natyra\\Mentorship_Teamwork_ILS\\mentorship-and-teamwork-solver\\src\\output_files\\";
        String fileExtension = ".txt";

        switch (chosenFile) {
            case "a":
                absoluteInputFilePath += "a_an_example.in" + fileExtension;
                absoluteOutputFilePath += "a" + fileExtension;
                break;
            case "b":
                absoluteInputFilePath += "b_better_start_small.in" + fileExtension;
                absoluteOutputFilePath += "b" + fileExtension;
                break;
            case "c":
                absoluteInputFilePath += "c_collaboration.in" + fileExtension;
                absoluteOutputFilePath += "c" + fileExtension;
                break;
            case "d":
                absoluteInputFilePath += "d_dense_schedule.in" + fileExtension;
                absoluteOutputFilePath += "d" + fileExtension;
                break;
            case "e":
                absoluteInputFilePath += "e_exceptional_skills.in" + fileExtension;
                absoluteOutputFilePath += "e" + fileExtension;
                break;
            case "f":
                absoluteInputFilePath += "f_find_great_mentors.in" + fileExtension;
                absoluteOutputFilePath += "f" + fileExtension;
                break;
            case "class":
                absoluteInputFilePath += "class_task.in" + fileExtension;
                absoluteOutputFilePath += "class" + fileExtension;
                break;
            default:
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

        int currentLine = 1;
        while (currentLine <= numberOfContributors) {
            Contributor contributor = new Contributor();
            contributor.setId(UUID.randomUUID());

            String[] nameAndNrOfSkills = fileContents.get(currentLine).split(" ");
            contributor.setName(nameAndNrOfSkills[0]);

            int numberOfSkills = Integer.parseInt(nameAndNrOfSkills[1]);
            List<Skill> skills = new ArrayList<>();

            for (int j = 1; j <= numberOfSkills; j++) {
                Skill skill = new Skill();
                String[] skillAndLevel = fileContents.get(currentLine + j).split(" ");
                skill.setId(UUID.randomUUID());
                skill.setName(skillAndLevel[0]);
                skill.setLevel(Integer.parseInt(skillAndLevel[1]));
                skills.add(skill);
            }

            contributor.setSkills(skills);
            contributors.add(contributor);

            numberOfContributors += numberOfSkills;
            currentLine += numberOfSkills + 1;
        }

        return contributors;
    }

    public static List<Project> readProjects(List<String> fileLines) {
        int totalProjects = Integer.parseInt(fileLines.get(0).split(" ")[1]);

        int projectsStartLine = findProjectsStartLine(fileLines);
        List<String> projectLines = fileLines.subList(projectsStartLine, fileLines.size());

        List<Project> projectList = new ArrayList<>();
        int currentLineIndex = 0;

        while (currentLineIndex < totalProjects) {
            Project project = new Project();
            project.setId(UUID.randomUUID());

            String[] projectInfo = projectLines.get(currentLineIndex).split(" ");
            project.setName(projectInfo[0]);
            project.setDaysToComplete(Integer.parseInt(projectInfo[1]));
            project.setScore(Integer.parseInt(projectInfo[2]));
            project.setBestBefore(Integer.parseInt(projectInfo[3]));

            int skillsCount = Integer.parseInt(projectInfo[4]);
            List<Skill> skills = extractSkillsFromLines(projectLines, currentLineIndex + 1, skillsCount);

            project.setSkills(skills);
            projectList.add(project);

            totalProjects += skillsCount;
            currentLineIndex += skillsCount + 1;
        }
        return projectList;
    }

    private static int findProjectsStartLine(List<String> fileLines) {
        for (int i = 0; i < fileLines.size(); i++) {
            String[] content = fileLines.get(i).split(" ");
            if (content.length == 5) {
                return i;
            }
        }
        return 0;
    }

    private static List<Skill> extractSkillsFromLines(List<String> projectLines, int startIndex, int skillsCount) {
        List<Skill> skills = new ArrayList<>();
        for (int i = startIndex, limit = startIndex + skillsCount; i < limit; i++) {
            Skill skill = new Skill();
            skill.setId(UUID.randomUUID());

            String[] skillAndLevel = projectLines.get(i).split(" ");
            skill.setName(skillAndLevel[0]);
            skill.setLevel(Integer.parseInt(skillAndLevel[1]));

            skills.add(skill);
        }
        return skills;
    }

    public static List<NameAssignment> readAssignments(String filename) {
        List<NameAssignment> nameAssignments = new ArrayList<>();
        List<String> fileLines = readFileContent(filename);

        for (int projectLineIndex = 1, contributorsLineIndex = 2; projectLineIndex < fileLines.size(); projectLineIndex += 2, contributorsLineIndex += 2) {

            NameAssignment nameAssignment = new NameAssignment();
            nameAssignment.setProject(fileLines.get(projectLineIndex));
            nameAssignment.setId(UUID.randomUUID());

            String[] contributorNamesArray = fileLines.get(contributorsLineIndex).split(" ");
            List<String> assignedContributors = new ArrayList<>(Arrays.asList(contributorNamesArray));
            nameAssignment.setAssignedContributors(assignedContributors);

            nameAssignments.add(nameAssignment);
        }
        return nameAssignments;
    }
}