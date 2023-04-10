package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;
import entities.RawAssignments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(List<Assignment> initialRandomCandidateSolution, int maxIteration, List<Project> projects, List<Contributor> contributors, String outputFile) {
        List<Assignment> S = new ArrayList<>(initialRandomCandidateSolution);
        List<Assignment> H = new ArrayList<>(S);
        List<Assignment> Best = new ArrayList<>(S);

        int i = 0;
        while (i < maxIteration) {

            int j = 0;
            while (j < maxIteration) {
                List<Assignment> R = Tweak(Copy(S), projects, contributors, outputFile);
                System.out.println("= " + Quality(R, projects, contributors) + " - " +  Quality(S, projects, contributors));
                if (Quality(R, projects, contributors) > Quality(S, projects, contributors)) {
                    S = new ArrayList<>(R);
                }
                j++;
            }

            System.out.println("? " + Quality(S, projects, contributors) + " - " +  Quality(Best, projects, contributors));
            if(Quality(S, projects, contributors) > Quality(Best, projects, contributors)) {
                Best = new ArrayList<>(S);
            }

            H = NewHomeBase(H, S, projects, contributors);
            S = Perturb(H, projects, contributors, outputFile);
            i++;
        }
        return Best;
    }

    private static List<Assignment> Copy(List<Assignment> S) {
        return new ArrayList<>(S);
    }

    private static List<Assignment> Tweak(List<Assignment> CopyS, List<Project> projects, List<Contributor> contributors, String outputFile) {
        List<Assignment> teakedS = new ArrayList<>(CopyS);
        Collections.shuffle(teakedS, new Random());

        boolean generatedAValidChange = true;
        while(generatedAValidChange) {
            if(Validator.areAssignmentsValid(RawAssignments.from(teakedS), contributors, projects, outputFile)) {
                generatedAValidChange = false;
            }
            Collections.shuffle(teakedS, new Random());
        }

        return teakedS;
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

    private static List<Assignment> Perturb(List<Assignment> H, List<Project> projects, List<Contributor> contributors, String outputFile) {
        List<Assignment> pertubedSolution = new ArrayList<>(H);
        Collections.shuffle(pertubedSolution, new Random());

        boolean generatedAValidChange = true;
        while(generatedAValidChange) {
            if(Validator.areAssignmentsValid(RawAssignments.from(pertubedSolution), contributors, projects, outputFile)) {
                generatedAValidChange = false;
            }
            Collections.shuffle(pertubedSolution, new Random());
        }

        return pertubedSolution;
    }
}
