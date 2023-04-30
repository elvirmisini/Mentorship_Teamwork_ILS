package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(
            List<Assignment> initialRandomCandidateSolution, int maxIteration, List<Project> projects,
            List<Contributor> contributors, String outputFile) {
        List<Assignment> S = new ArrayList<>(initialRandomCandidateSolution);
        List<Assignment> H = new ArrayList<>(S);
        List<Assignment> Best = new ArrayList<>(S);

        int i = 0;
        while (i < maxIteration) {
            // System.out.println(i);

            int j = 0;
            while (j < maxIteration) {
                List<Assignment> R = Tweak(Copy(S));
                if (Validator.areAssignmentsValid(RawAssignments.from(R), contributors, projects, outputFile)) {
                    if (Quality(R, projects, contributors) > Quality(S, projects, contributors)) {
                        S = new ArrayList<>(R);
                    }
                }
                j++;
            }

            if (Quality(S, projects, contributors) > Quality(Best, projects, contributors)) {
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

    private static List<Assignment> Tweak(List<Assignment> CopyS) {
        int operator = (int) (Math.random() * 3); // generate a random number between 0 and 2

        switch (operator) {
            case 0:
                return Swap(CopyS);
            case 1:
                return Insert(CopyS);
            case 2:
                return Inversion(CopyS);

            default:
                return CopyS;
        }
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

    private static List<Assignment> Insert(List<Assignment> CopyS) {
        // Generate two random indices within the bounds of the list
        int index1 = (int) (Math.random() * CopyS.size());
        int index2 = (int) (Math.random() * CopyS.size());

        // Insert the element at the first index at the second index
        Assignment temp = CopyS.remove(index1);
        CopyS.add(index2, temp);

        return CopyS;
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
        int index1 = (int) (Math.random() * H.size());
        int index2 = (int) (Math.random() * H.size());

        // Swap the elements at the random indices
        Assignment temp = H.get(index1);
        H.set(index1, H.get(index2));
        H.set(index2, temp);
        return H;
    }
}
