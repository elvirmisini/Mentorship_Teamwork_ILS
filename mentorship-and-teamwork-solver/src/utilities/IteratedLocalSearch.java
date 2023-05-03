package utilities;

import entities.Contributor;
import entities.FullAssignment;
import entities.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    public static List<FullAssignment> iteratedLocalSearchWithRandomRestarts(List<FullAssignment> initialSolution, int maxIteration, List<Project> projects, List<Contributor> contributors) {
        List<FullAssignment> S = new ArrayList<>(initialSolution);
        List<FullAssignment> H = new ArrayList<>(S);
        List<FullAssignment> Best = new ArrayList<>(S);

        int i = 0;
        while (i < maxIteration) {
            System.out.println("step = " + i);

            int j = 0;
            while (j < maxIteration) {
                List<FullAssignment> R = Tweak(Copy(S), projects, contributors);
                if (Validator.areAssignmentsValid(R, contributors, projects)) {
                    int delta = deltaQuality(S, R);
                    if (delta > 0) {
                        S = new ArrayList<>(R);
                    }
                }
                j++;
            }

            int delta = deltaQuality(Best, S);
            if (delta > 0) {
                System.out.println("added " + delta);
                Best = new ArrayList<>(S);
            }

            H = NewHomeBase(H, S);
            List<FullAssignment> PerturbedS = Perturb(H);
            if (Validator.areAssignmentsValid(PerturbedS, contributors, projects)) {
                S = PerturbedS;
            }
            i++;
        }
        return Best;
    }

    private static int deltaQuality(List<FullAssignment> oldSolution, List<FullAssignment> newSolution) {
        return Quality(newSolution) - Quality(oldSolution);
    }

    private static List<FullAssignment> Copy(List<FullAssignment> S) {
        return new ArrayList<>(S);
    }

    private static int Quality(List<FullAssignment> R) {
        return FitnessCalculator.getFitnessScore(R);
    }

    private static List<FullAssignment> NewHomeBase(List<FullAssignment> H, List<FullAssignment> S) {
        if (Quality(S) >= Quality(H)) {
            return new ArrayList<>(S);
        } else {
            return new ArrayList<>(H);
        }
    }

    private static List<FullAssignment> Tweak(List<FullAssignment> CopyS, List<Project> projects, List<Contributor> contributors) {
        int operator = (int) (Math.random() * 4);

        switch (operator) {
            case 0:
                return Swap(CopyS);
            case 1:
                return InsertProjects(CopyS, projects, contributors);
            case 2:
                return Inversion(CopyS);
            case 3:
                return RemoveProject(CopyS);
            default:
                return CopyS;
        }
    }

    private static List<FullAssignment> Swap(List<FullAssignment> CopyS) {
        int index = (int) (Math.random() * (CopyS.size() - 1));
        FullAssignment temp = CopyS.get(index);
        CopyS.set(index, CopyS.get(index + 1));
        CopyS.set(index + 1, temp);
        return CopyS;
    }

    private static List<FullAssignment> InsertProjects(List<FullAssignment> fullAssignments, List<Project> projects, List<Contributor> contributors) {
        List<String> assignedProjectIds = fullAssignments.stream().map(assignment -> {
            if (assignment != null) {
                if (assignment.getProject() != null) {
                    return assignment.getProject().getName();
                }
            }
            return null;
        }).collect(Collectors.toList());

        List<Project> unassignedProjects = projects.stream().filter(project -> !assignedProjectIds.contains(project.getName())).collect(Collectors.toList());
        if (unassignedProjects.size() > 0) {
            List<FullAssignment> additionalFullAssignments = InitialSolver.solve(contributors, unassignedProjects);
            fullAssignments.addAll(additionalFullAssignments);
        }

        return fullAssignments;
    }

    private static List<FullAssignment> Inversion(List<FullAssignment> CopyS) {
        int index1 = (int) (Math.random() * CopyS.size());
        int index2 = (int) (Math.random() * CopyS.size());

        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);

        for (int i = start, j = end; i < j; i++, j--) {
            FullAssignment temp = CopyS.get(i);
            CopyS.set(i, CopyS.get(j));
            CopyS.set(j, temp);
        }
        return CopyS;
    }

    private static List<FullAssignment> RemoveProject(List<FullAssignment> fullAssignments) {
        Random random = new Random();
        int fromIndex = random.nextInt(fullAssignments.size());
        new ArrayList<>(fullAssignments).remove(fromIndex);
        return fullAssignments;
    }

    private static List<FullAssignment> Perturb(List<FullAssignment> H) {
        Random random = new Random();
        int fromIndex = random.nextInt(H.size());
        int toIndex = random.nextInt(H.size());

        if (fromIndex > toIndex) {
            int temp = fromIndex;
            fromIndex = toIndex;
            toIndex = temp;
        }

        Collections.shuffle(H.subList(fromIndex, toIndex));
        return H;
    }
}