package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;

import java.util.*;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(
            List<Assignment> initialRandomCandidateSolution, int maxIteration, List<Project> projects,
            List<Contributor> contributors, String outputFile) {

        List<Assignment> S = new ArrayList<>(initialRandomCandidateSolution);
        List<Assignment> H = new ArrayList<>(S);
        List<Assignment> Best = new ArrayList<>(S);

        int i = 0;
        while (i < maxIteration) {
            System.out.println("step = " + i);

            int j = 0;
            while (j < maxIteration) {
                List<Assignment> R = Tweak(Copy(S), projects, contributors);
                if (Validator.areAssignmentsValid(RawAssignments.from(R), contributors, projects, outputFile)) {
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
            List<Assignment> PerturbedS = Perturb(H);
            if (Validator.areAssignmentsValid(RawAssignments.from(PerturbedS), contributors, projects, outputFile)) {
                S = PerturbedS;
            }
            i++;
        }
        return Best;
    }

    private static int deltaQuality(List<Assignment> oldSolution, List<Assignment> newSolution, List<Project> projects,
            List<Contributor> contributors) {
        return Quality(newSolution, projects, contributors) - Quality(oldSolution, projects, contributors);
    }

    private static List<Assignment> Copy(List<Assignment> S) {
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

    private static int Quality(List<Assignment> R, List<Project> projects, List<Contributor> contributors) {
        return FitnessCalculator.getFitnessScore(R, contributors, projects);
    }

    private static List<Assignment> NewHomeBase(List<Assignment> H, List<Assignment> S, List<Project> projects,
            List<Contributor> contributors) {
        if (Quality(S, projects, contributors) >= Quality(H, projects, contributors)) {
            return new ArrayList<>(S);
        } else {
            return new ArrayList<>(H);
        }
    }

    private static List<Assignment> Tweak(List<Assignment> CopyS, List<Project> projects, List<Contributor> contributors) {
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

    private static List<Assignment> RemoveProject(List<Assignment> assignments) {
        Random random = new Random();
        int fromIndex = random.nextInt(assignments.size());
        new ArrayList<>(assignments).remove(fromIndex);
        return assignments;
    }

    private static List<Assignment> Swap(List<Assignment> CopyS) {
        // Generate a random index within the bounds of the list (excluding the last
        // element)
        int index = (int) (Math.random() * (CopyS.size() - 1));

        // Swap the neighboring elements at the random index
        Assignment temp = CopyS.get(index);
        CopyS.set(index, CopyS.get(index + 1));
        CopyS.set(index + 1, temp);

        return CopyS;
    }

    private static List<Assignment> InsertProjects(List<Assignment> assignments, List<Project> projects, List<Contributor> contributors) {
        List<String> assignedProjectIds = assignments.stream()
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
            List<Assignment> additionalAssignments = InitialSolver.solver(contributors, unassignedProjects);
//            System.out.println(additionalAssignments.size());
            assignments.addAll(additionalAssignments);
        }

        return assignments;
    }

    private static List<Assignment> Inversion(List<Assignment> CopyS) {
        // Generate two random indices within the bounds of the list
        int index1 = (int) (Math.random() * CopyS.size());
        int index2 = (int) (Math.random() * CopyS.size());

        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);

        // Reverse the elements in the sub-list between the two indices
        for (int i = start, j = end; i < j; i++, j--) {
            Assignment temp = CopyS.get(i);
            CopyS.set(i, CopyS.get(j));
            CopyS.set(j, temp);
        }

        return CopyS;
    }

    private static List<Assignment> Shake(List<Assignment> CopyS) {
        int k = (int) (Math.random() * CopyS.size() / 2) + 1; // generate a random k value between 1 and n/2
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < CopyS.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (int i = 0; i < k; i++) {
            int index1 = indices.get(i);
            int index2 = indices.get(i + k);
            Assignment temp = CopyS.get(index1);
            CopyS.set(index1, CopyS.get(index2));
            CopyS.set(index2, temp);
        }

        return CopyS;
    }

    private static List<Assignment> Perturb(List<Assignment> H) {
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
