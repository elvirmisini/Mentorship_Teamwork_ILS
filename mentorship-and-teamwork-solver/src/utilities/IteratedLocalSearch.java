package utilities;

import entities.Assignment;
import entities.Contributor;
import entities.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IteratedLocalSearch {

    public static List<Assignment> iteratedLocalSearchWithRandomRestarts(List<Assignment> initialSolution,
                                                                             int maxMinutes, List<Project> projects, List<Contributor> contributors) {
        List<Contributor> contributorsBeforeILS = contributors.stream().map(Contributor::deepCopy)
                .collect(Collectors.toList());
        List<Project> projectsBeforeILS = projects.stream().map(Project::deepCopy).collect(Collectors.toList());
        List<Assignment> initialSolutionBeforeILS = initialSolution.stream().map(Assignment::deepCopy).collect(Collectors.toList());

        List<Assignment> S = new ArrayList<>(initialSolution);
        List<Assignment> H = new ArrayList<>(S);
        List<Assignment> Best = new ArrayList<>(S);

        long startTime = System.currentTimeMillis();
        long maxMillis = TimeUnit.MINUTES.toMillis(maxMinutes);

        while (System.currentTimeMillis() - startTime < maxMillis) {
            int innerIteration = 0;
            while (System.currentTimeMillis() - startTime < maxMillis && innerIteration < maxMinutes * 60) {
                List<Assignment> R = Tweak(Copy(S), projects, contributors);
                if (Validator.areAssignmentsValid(R, contributors, projects)) {
                    int delta = deltaQuality(S, R);
                    if (delta > 0) {
                        S = new ArrayList<>(R);
                    }
                }
                innerIteration++;
            }

            int delta = deltaQuality(Best, S);
            if (delta > 0) {
                Best = new ArrayList<>(S);
            }

            H = NewHomeBase(H, S);
            List<Assignment> PerturbedS = Perturb(H);
            if (Validator.areAssignmentsValid(PerturbedS, contributors, projects)) {
                S = PerturbedS;
            }
        }

        if (Validator.areAssignmentsValid(Best, contributorsBeforeILS, projectsBeforeILS)) {
            return Best;
        } else {
            System.out.println("Changed assignments with ILS are not valid!");
            return initialSolutionBeforeILS;
        }
    }

    private static int deltaQuality(List<Assignment> oldSolution, List<Assignment> newSolution) {
        return Quality(newSolution) - Quality(oldSolution);
    }

    private static List<Assignment> Copy(List<Assignment> S) {
        return new ArrayList<>(S);
    }

    private static int Quality(List<Assignment> R) {
        return FitnessCalculator.getFitnessScore(R);
    }

    private static List<Assignment> NewHomeBase(List<Assignment> H, List<Assignment> S) {
        if (Quality(S) >= Quality(H)) {
            return new ArrayList<>(S);
        } else {
            return new ArrayList<>(H);
        }
    }

    private static List<Assignment> Tweak(List<Assignment> CopyS, List<Project> projects,
                                              List<Contributor> contributors) {
//        int operator = (int) (Math.random() * 4);
//
//        switch (operator) {
//            case 0:
//                return Swap(CopyS);
//            case 1:
//                return InsertProjects(CopyS, projects, contributors);
//            case 2:
//                return Inversion(CopyS);
//            case 3:
//                return RemoveProject(CopyS);
//            default:
//                return CopyS;
//        }
        return Swap(CopyS);
    }

    private static List<Assignment> Swap(List<Assignment> CopyS) {
        int index = (int) (Math.random() * (CopyS.size() - 1));
        Assignment temp = CopyS.get(index);
        CopyS.set(index, CopyS.get(index + 1));
        CopyS.set(index + 1, temp);
        return CopyS;
    }

    private static List<Assignment> InsertProjects(List<Assignment> fullAssignments, List<Project> projects,
                                                       List<Contributor> contributors) {
        List<String> assignedProjectIds = fullAssignments.stream().map(assignment -> {
            if (assignment != null) {
                if (assignment.getProject() != null) {
                    return assignment.getProject().getName();
                }
            }
            return null;
        }).collect(Collectors.toList());

        List<Project> unassignedProjects = projects.stream()
                .filter(project -> !assignedProjectIds.contains(project.getName())).collect(Collectors.toList());
        if (unassignedProjects.size() > 0) {
            List<Assignment> additionalFullAssignments = InitialSolver.solveMentorshipAndTeamwork(unassignedProjects, contributors);
            fullAssignments.addAll(additionalFullAssignments);
        }

        return fullAssignments;
    }

    private static List<Assignment> Inversion(List<Assignment> CopyS) {
        int index1 = (int) (Math.random() * CopyS.size());
        int index2 = (int) (Math.random() * CopyS.size());

        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);

        for (int i = start, j = end; i < j; i++, j--) {
            Assignment temp = CopyS.get(i);
            CopyS.set(i, CopyS.get(j));
            CopyS.set(j, temp);
        }
        return CopyS;
    }

    private static List<Assignment> RemoveProject(List<Assignment> fullAssignments) {
        Random random = new Random();
        int fromIndex = random.nextInt(fullAssignments.size());
        new ArrayList<>(fullAssignments).remove(fromIndex);
        return fullAssignments;
    }

    private static List<Assignment> Perturb(List<Assignment> H) {
//        Random random = new Random();
//        int fromIndex = random.nextInt(H.size());
//        int toIndex = random.nextInt(H.size());
//
//        if (fromIndex > toIndex) {
//            int temp = fromIndex;
//            fromIndex = toIndex;
//            toIndex = temp;
//        }
//
//        Collections.shuffle(H.subList(fromIndex, toIndex));
//        return H;
        return Swap(H);
    }
}