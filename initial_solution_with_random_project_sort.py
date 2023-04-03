from modules.file_selector import *
from modules.instance_parse import *
from modules.fitness_calculator import *
from modules.submission_saver import *
from modules.initial_solution import *
from modules.solution_validation import *


import random


def get_possible_combinations_of_projects_and_contributors(projects, contributors):
    projects_skills = {}
    for project in projects:
        current_project_name = project["name"]
        current_project_skills = project["skills"]
        current_project_skills_as_list = []
        for current_project_skill in current_project_skills:
            current_project_skills_as_list.append(list(current_project_skill.keys()))
        flat_list = []
        for sublist in current_project_skills_as_list:
            for element in sublist:
                flat_list.append(element)
        projects_skills[current_project_name] = flat_list

    contributors_skills = {}
    for current_contributor_name in contributors:
        current_contributor_skills = contributors[current_contributor_name]
        lista_me_skills = []
        for current_contributor_skill in current_contributor_skills:
            lista_me_skills.append(current_contributor_skill)
        contributors_skills[current_contributor_name] = lista_me_skills
    
    possible_combination_of_project_with_contributors = {}
    for project_name, project_skills in projects_skills.items():
        project_with_contributors = set()
        for contributor_name, contributor_skills in contributors_skills.items():
            for contributor_skill in contributor_skills:
                if contributor_skill in project_skills:
                    project_with_contributors.add(contributor_name)
        possible_combination_of_project_with_contributors[project_name] = project_with_contributors
    
    new_possible_combination_of_project_with_contributors = {}
    for possible_combination in possible_combination_of_project_with_contributors:
        lista = []
        for l in possible_combination_of_project_with_contributors[possible_combination]:
            lista.append(l)
        new_possible_combination_of_project_with_contributors[possible_combination] = lista
    return new_possible_combination_of_project_with_contributors


def get_possible_combinations_of_project_and_contributors(projects, possible_combination_of_project_with_contributors):
    projects_as_dict = {}
    for project in projects:
        projects_as_dict[project["name"]] = project    
    possible_combination_of_project_with_contributors = get_possible_combinations_of_projects_and_contributors(projects, contributors)

    new_possible_combination_of_project_with_contributors = {}
    for possible_combination in possible_combination_of_project_with_contributors:
        number_of_project_skills = len(projects_as_dict[possible_combination]["skills"])
        new_possible_combination_of_project_with_contributors[possible_combination] = possible_combination_of_project_with_contributors[possible_combination][0:number_of_project_skills]
    
    return new_possible_combination_of_project_with_contributors, projects_as_dict


def get_only_the_correct_groups(combination_of_projects_and_contributors, projects_as_dict, contributors):
    correct_combination = {}
    for project_name, project_contributors in combination_of_projects_and_contributors.items():
        list_of_contributors = set()
        project_skills = projects_as_dict[project_name]["skills"]
        for project_skill in project_skills:
            pr_skill = list(project_skill.keys())[0]
            pr_level = project_skill[pr_skill]

            for project_contributor in project_contributors:
                contributor_skills = contributors[project_contributor]
               # other_contributors = set()

               # we should have a list of other contributors of the project and have thair level skills,
               # so we can add a mentor to the actual contributor

                for contributor_skill, contributor_level in contributor_skills.items():
                    if contributor_skill == pr_skill and contributor_level >= pr_level:
                        list_of_contributors.add(project_contributor)
                        #######################################
                        print(contributor_level)
                        if contributor_level == pr_level:
                            contributor_level += 1
                        #other_contributors.add(project_contributor)
                        print(project_contributor)
                        print(contributor_level)
                        print('============')
                       ###########################
                        #print(project_name, "-", project_contributor, " - ", contributor_skill, contributor_level, " - ", pr_skill, pr_level)
                        ######
               # print("Other contributors for", project_name, ":", other_contributors - {project_contributor})
        correct_combination[project_name] = list_of_contributors
    
    projects_and_contributors = {}
    for combination in correct_combination:
        if correct_combination[combination] != set():
            projects_and_contributors[combination] = correct_combination[combination]
    
    return projects_and_contributors

def set_latest_end_day_to_all_contributors_of_a_project(assignment_contributors, contributors_final_work_day):
    """
    Sets the latest end work day for all contributors of a project.
    """
    latest_work_day = max(contributors_final_work_day.get(name, 0) for name in assignment_contributors)
    for contributor in assignment_contributors:
        contributors_final_work_day[contributor] = latest_work_day


def get_latest_day_of_all_given_contributors(all_contributors, assignment_contributors):
    """
    Given all contributors and the list of assignment contributors, returns the latest work day from those contributors.
    """
    latest_work_day = max(all_contributors.get(name, 0) for name in assignment_contributors)
    return latest_work_day


def convert_project_list_to_dictionary(projects):
    projects_in_dict = {}
    for project in projects:
        name = project['name']
        project_data = project.copy()
        del project_data['name']
        projects_in_dict[name] = project_data
    return projects_in_dict


def objective_function(projects, contributors, assignments):
    contributors_final_work_day = {contributor: 0 for contributor in contributors}
    total_projects_score = 0

    projects_in_dict = convert_project_list_to_dictionary(projects)

    for assignment_project, assignment_contributors in assignments.items():
        num_days_to_complete_project = 0

        if not assignment_project in projects_in_dict:
            print(f"Error. The project {assignment_project} is not correct.")
            print("Check if the input and output files correspond with each other.")
            exit()

        project = projects_in_dict[assignment_project]

        num_days_to_complete_project = project["days"]
        project_best_before_days = project["best_before"]
        project_score = project["score"]

        # Update the final work day for each contributor assigned to the project.
        for contributor in assignment_contributors:
            contributors_final_work_day[contributor] += num_days_to_complete_project

        set_latest_end_day_to_all_contributors_of_a_project(assignment_contributors, contributors_final_work_day)

        # Get the latest end work day of all contributors assigned to the project.
        end_work_day_of_project = get_latest_day_of_all_given_contributors(contributors_final_work_day, assignment_contributors)

        # Calculate the score of the current project and add it to the total score.
        if project_best_before_days > end_work_day_of_project:
            total_projects_score += project_score
        elif (project_score - (end_work_day_of_project - project_best_before_days)) >= 0:
            total_projects_score += project_score - (end_work_day_of_project - project_best_before_days)
        else:
            total_projects_score += 0

    return total_projects_score


def iterated_local_search_with_random_restarts(initial_solution, max_iterations, max_restarts):
    best_solution = initial_solution
    best_value = objective_function(projects, contributors, initial_solution)

    restarts = 0
    while restarts < max_restarts:
        current_solution = best_solution
        for i in range(max_iterations):
            candidate_solution = neighborhood_function(best_solution)
            candidate_value = objective_function(projects, contributors, current_solution)
            if candidate_value > best_value:
                current_solution = candidate_solution
                best_value = candidate_value
        if objective_function(projects, contributors, current_solution) > best_value:
            best_solution = current_solution
            best_value = objective_function(projects, contributors, current_solution)
        restarts += 1
    return best_solution


# kta na duhet me e ndrru
def neighborhood_function(best_solution):
    new_solution = best_solution
    return new_solution

    
if __name__ == "__main__":
    number_of_contributors, number_of_projects, contributors, projects = read_contributors_and_projects(input_file)
    # print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects)
    # random.shuffle(projects)

    combinations, projects_as_dict = get_possible_combinations_of_project_and_contributors(projects, get_possible_combinations_of_projects_and_contributors(projects, contributors))
    # print("\n", combinations, "\n")

    # from the combinaiton we get something like this
    # {'WebChat': ['Bob', 'Maria'], 'Logging': ['Anna'], 'WebServer': ['Bob', 'Anna']} 
    # what we should do is get the correct combinations and save them in a file
    # for example this combinations 'WebChat': ['Bob', 'Maria'], 'WebServer': ['Bob', 'Anna'] are correct 
    # these combination 'Logging': ['Anna'] is not correct 
    # so we save only the correct combinations in the output file

    x = get_only_the_correct_groups(combinations, projects_as_dict, contributors)
    for key in x:
        x[key] = list(x[key])
    
    correct = {}
    for project in projects:
        for x_name in x:
            if x_name == project["name"]:
                nr_contributors = len(x[x_name])
                nr_con_in_project = len(project["skills"])
                if nr_con_in_project == nr_contributors:
                    correct[x_name] = x[x_name]


    ######################################################################################################################################################
    # The code implementing Iterated Local Search   
    ######################################################################################################################################################


    final_solution = iterated_local_search_with_random_restarts(correct, 100, 100)

    save_assignments(final_solution, "C:\\Users\\Elvir Misini\\Desktop\\mentorship_teamwork_ils\\output\\test_test_a.txt")