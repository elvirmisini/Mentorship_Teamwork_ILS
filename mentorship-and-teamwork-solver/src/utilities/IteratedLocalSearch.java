package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;

import java.util.ArrayList;
import java.util.List;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(List<Assignment> initialRandomCandidateSolution, int maxIteration, List<Project> projects, List<Contributor> contributors, String outputFile) {
        List<Assignment> S = new ArrayList<>(initialRandomCandidateSolution);
        List<Assignment> H = new ArrayList<>(S);
        List<Assignment> Best = new ArrayList<>(S);

        int i = 0;
        while (i < maxIteration) {
//            System.out.println(i);

            int j = 0;
            while (j < maxIteration) {
                List<Assignment> R = Tweak(Copy(S));
                if(Validator.areAssignmentsValid(RawAssignments.from(R), contributors, projects, outputFile)) {
                    if (Quality(R, projects, contributors) > Quality(S, projects, contributors)) {
                        S = new ArrayList<>(R);
                    }
                }
                j++;
            }

            if(Quality(S, projects, contributors) > Quality(Best, projects, contributors)) {
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

    private static List<Assignment> Tweak(List<Assignment> CopyS) {
        // Generate a random index within the bounds of the list (excluding the last element)
        int index = (int) (Math.random() * (CopyS.size() - 1));

        // Swap the neighboring elements at the random index
        Assignment temp = CopyS.get(index);
        CopyS.set(index, CopyS.get(index + 1));
        CopyS.set(index + 1, temp);

        return CopyS;
    }

    private static int Quality(List<Assignment> R, List<Project> projects, List<Contributor> contributors) {
        return FitnessCalculator.getFitnessScore(R, contributors, projects);
    }

    private static List<Assignment> NewHomeBase(List<Assignment> H, List<Assignment> S, List<Project> projects, List<Contributor> contributors) {
        if(Quality(S, projects, contributors) >= Quality(H, projects, contributors)) {
            return new ArrayList<>(S);
        }
        else {
            return new ArrayList<>(H);
        }
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
