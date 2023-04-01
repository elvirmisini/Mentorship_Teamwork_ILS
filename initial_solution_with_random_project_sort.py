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
                for contributor_skill, contributor_level in contributor_skills.items():
                    if contributor_skill == pr_skill and contributor_level >= pr_level:
                        list_of_contributors.add(project_contributor)
                        # print(project_name, "-", project_contributor, " - ", contributor_skill, contributor_level, " - ", pr_skill, pr_level)
                        
        correct_combination[project_name] = list_of_contributors
    
    projects_and_contributors = {}
    for combination in correct_combination:
        if correct_combination[combination] != set():
            projects_and_contributors[combination] = correct_combination[combination]
    
    return projects_and_contributors

if __name__ == "__main__":
    number_of_contributors, number_of_projects, contributors, projects = read_contributors_and_projects(input_file)
    # print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects)
    random.shuffle(projects)

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

    save_assignments(correct, output_file)
    