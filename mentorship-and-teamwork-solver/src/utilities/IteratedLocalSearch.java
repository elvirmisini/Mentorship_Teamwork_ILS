package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.ArrayList;
import java.util.List;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(List<Assignment> initialSolution, List<Project> projects, List<Contributor> contributors, int maxIterations, int maxRestarts) {
        List<Assignment> bestSolution = initialSolution;
        int bestValue = FitnessCalculator.getFitnessScore(initialSolution, contributors, projects);

        int restarts = 0;
        while (restarts < maxRestarts) {
            List<Assignment> currentSolution = bestSolution;
            for (int i = 0; i < maxIterations; i++) {
                List<Assignment> candidateSolution = neighborhoodFunction(bestSolution);
                int candidateValue = FitnessCalculator.getFitnessScore(candidateSolution, contributors, projects);
                if (candidateValue > bestValue) {
                    currentSolution = candidateSolution;
                    bestValue = candidateValue;
                }
            }
            if (FitnessCalculator.getFitnessScore(currentSolution, contributors, projects) > bestValue) {
                bestSolution = currentSolution;
                bestValue = FitnessCalculator.getFitnessScore(currentSolution, contributors, projects);
            }
            restarts++;
        }
        return bestSolution;
    }

    private static List<Assignment> neighborhoodFunction(List<Assignment> bestSolution) {
        List<Assignment> changedSolution = new ArrayList<>();

        return changedSolution;
    }

}
