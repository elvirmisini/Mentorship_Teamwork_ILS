package utilities;

import entities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    public static List<FullAssignment> iteratedLocalSearchWithRandomRestarts(
            List<FullAssignment> initialRandomCandidateSolution, int maxIteration, List<Project> projects,
            List<Contributor> contributors, String outputFile) {

        List<FullAssignment> S = new ArrayList<>(initialRandomCandidateSolution);
        List<FullAssignment> H = new ArrayList<>(S);
        List<FullAssignment> Best = new ArrayList<>(S);

        int i = 0;
        while (i < maxIteration) {
            System.out.println("step = " + i);

            int j = 0;
            while (j < maxIteration) {
                List<FullAssignment> R = Tweak(Copy(S), projects, contributors);
                if (Validator.areAssignmentsValid(NameAssignment.from(R), contributors, projects, outputFile)) {
                    int delta = deltaQuality(S, R, projects, contributors);
                    if (delta > 0) {
                        S = new ArrayList<>(R);
                    }
                }
                j++;
            }

            int delta = deltaQuality(Best, S, projects, contributors);
            if (delta > 0) {
                System.out.println("added " + delta);
                Best = new ArrayList<>(S);
            }

            H = NewHomeBase(H, S, projects, contributors);
            List<FullAssignment> PerturbedS = Perturb(H);
            if (Validator.areAssignmentsValid(NameAssignment.from(PerturbedS), contributors, projects, outputFile)) {
                S = PerturbedS;
            }
            i++;
        }
        return Best;
    }

    private static int deltaQuality(List<FullAssignment> oldSolution, List<FullAssignment> newSolution, List<Project> projects,
                                    List<Contributor> contributors) {
        return Quality(newSolution, projects, contributors) - Quality(oldSolution, projects, contributors);
    }

    private static List<FullAssignment> Copy(List<FullAssignment> S) {
        return new ArrayList<>(S);
    }

    // private static List<Assignment> Tweak(List<Assignment> CopyS) {
    // // Generate a random index within the bounds of the list (excluding the last
    // // element)
    // int index = (int) (Math.random() * (CopyS.size() - 1));

    // // Swap the neighboring elements at the random index
    // Assignment temp = CopyS.get(index);
    // CopyS.set(index, CopyS.get(index + 1));
    // CopyS.set(index + 1, temp);

    // return CopyS;
    // }

    private static int Quality(List<FullAssignment> R, List<Project> projects, List<Contributor> contributors) {
        return FitnessCalculator.getFitnessScore(R, contributors, projects);
    }

    private static List<FullAssignment> NewHomeBase(List<FullAssignment> H, List<FullAssignment> S, List<Project> projects,
                                                    List<Contributor> contributors) {
        if (Quality(S, projects, contributors) >= Quality(H, projects, contributors)) {
            return new ArrayList<>(S);
        } else {
            return new ArrayList<>(H);
        }
    }

    private static List<FullAssignment> Tweak(List<FullAssignment> CopyS, List<Project> projects, List<Contributor> contributors) {
        int operator = (int) (Math.random() * 4); // generate a random number between 0 and 2

        switch (operator) {
            case 0:
                return Swap(CopyS);
            case 1:
                return InsertProjects(CopyS, projects, contributors);
            case 2:
                return Inversion(CopyS);
//            case 3:
//                return ReplaceContributor(CopyS);
            case 3:
                return RemoveProject(CopyS);
            default:
                return CopyS;
        }
    }

    private static List<FullAssignment> RemoveProject(List<FullAssignment> fullAssignments) {
        Random random = new Random();
        int fromIndex = random.nextInt(fullAssignments.size());
        new ArrayList<>(fullAssignments).remove(fromIndex);
        return fullAssignments;
    }

    private static List<FullAssignment> Swap(List<FullAssignment> CopyS) {
        // Generate a random index within the bounds of the list (excluding the last
        // element)
        int index = (int) (Math.random() * (CopyS.size() - 1));

        // Swap the neighboring elements at the random index
        FullAssignment temp = CopyS.get(index);
        CopyS.set(index, CopyS.get(index + 1));
        CopyS.set(index + 1, temp);

        return CopyS;
    }

    private static List<FullAssignment> InsertProjects(List<FullAssignment> fullAssignments, List<Project> projects, List<Contributor> contributors) {
        List<String> assignedProjectIds = fullAssignments.stream()
                .map(assignment -> {
                    if (assignment != null) {
                        if(assignment.getProject() != null) {
                            return assignment.getProject().getName();
                        }
                    }
                    return null;
                })
                .collect(Collectors.toList());

        List<Project> unassignedProjects =  projects.stream().filter(project -> !assignedProjectIds.contains(project.getName())).collect(Collectors.toList());
        if(unassignedProjects.size() > 0) {
            List<FullAssignment> additionalFullAssignments = InitialSolver.solver(contributors, unassignedProjects);
//            System.out.println(additionalAssignments.size());
            fullAssignments.addAll(additionalFullAssignments);
        }

        return fullAssignments;
    }

    private static List<FullAssignment> Inversion(List<FullAssignment> CopyS) {
        // Generate two random indices within the bounds of the list
        int index1 = (int) (Math.random() * CopyS.size());
        int index2 = (int) (Math.random() * CopyS.size());

        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);

        // Reverse the elements in the sub-list between the two indices
        for (int i = start, j = end; i < j; i++, j--) {
            FullAssignment temp = CopyS.get(i);
            CopyS.set(i, CopyS.get(j));
            CopyS.set(j, temp);
        }

        return CopyS;
    }

    private static List<FullAssignment> Shake(List<FullAssignment> CopyS) {
        int k = (int) (Math.random() * CopyS.size() / 2) + 1; // generate a random k value between 1 and n/2
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < CopyS.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (int i = 0; i < k; i++) {
            int index1 = indices.get(i);
            int index2 = indices.get(i + k);
            FullAssignment temp = CopyS.get(index1);
            CopyS.set(index1, CopyS.get(index2));
            CopyS.set(index2, temp);
        }

        return CopyS;
    }

    private static List<FullAssignment> Perturb(List<FullAssignment> H) {
        // Generate two random indices within the bounds of the list
//        int index1 = (int) (Math.random() * H.size());
//        int index2 = (int) (Math.random() * H.size());
//
//        // Swap the elements at the random indices
//        Assignment temp = H.get(index1);
//        H.set(index1, H.get(index2));
//        H.set(index2, temp);
//        return H;
        Random random = new Random();

        int fromIndex = random.nextInt(H.size());
        int toIndex = random.nextInt(H.size());

        // Ensure that fromIndex is less than toIndex
        if (fromIndex > toIndex) {
            int temp = fromIndex;
            fromIndex = toIndex;
            toIndex = temp;
        }

        // Shuffle the sublist between fromIndex (inclusive) and toIndex (exclusive)
        Collections.shuffle(H.subList(fromIndex, toIndex));
        return H;
    }
}
