from modules.file_selector import *
from modules.instance_parse import *
from modules.fitness_calculator import *
from modules.submission_saver import *
from modules.initial_solution import *
from modules.solution_validation import *

import random

def possible_mentors(contributor_name, contributor_skill, project_level, project_contributors, contributors):
    project_contributors_as_dict = {}

    for contributor in contributors:
        if contributor in project_contributors and contributor != contributor_name:
            project_contributors_as_dict[contributor] = contributors[contributor]

    for mentor_name, mentor_skill_and_level in project_contributors_as_dict.items():
        for mentor_skill, mentor_level in mentor_skill_and_level.items():
            if mentor_skill == contributor_skill and mentor_level >= project_level:
                return True
    return False

def solver(projects, contributors):
    assignments = {}
    for project in projects:
        assigned_contributors = []
        increast_skills_contributors = []
        project_skills = project["skills"]
        for project_skill_and_level in project_skills:
            for project_skill, project_level in project_skill_and_level.items():
                for contributor_name in contributors:
                    contributor_skill_and_level = contributors[contributor_name]
                    for contributor_skill, contributor_level in contributor_skill_and_level.items():
                        i = 0
                        if contributor_skill == project_skill and contributor_level >= project_level and contributor_name not in assigned_contributors and len(project_skills) > len(assigned_contributors):
                            i += 1
                            assigned_contributors.append(contributor_name)
                            if contributor_level == project_level:
                                contributors[contributor_name][contributor_skill] += 1      
                                increast_skills_contributors.append(contributor_name)
                        if i == 0:
                            if contributor_skill == project_skill and contributor_level + 1 == project_level and contributor_name not in assigned_contributors and len(project_skills) > len(assigned_contributors):
                                if possible_mentors(contributor_name, contributor_skill, project_level, assigned_contributors, contributors):
                                    assigned_contributors.append(contributor_name)
                                    contributors[contributor_name][contributor_skill] += 1 
                                    increast_skills_contributors.append(contributor_name)

                        i = 0
                        if len(project_skills) == len(assigned_contributors):
                            assignments[project["name"]] = assigned_contributors
                        else:
                            for assigned_contributor_name in increast_skills_contributors:
                                if contributor_skill in contributors[assigned_contributor_name]:
                                    contributors[assigned_contributor_name][contributor_skill] -= 1 
    return assignments

# Read input file and print contributors and projects info
number_of_contributors, number_of_projects, contributors, projects = read_contributors_and_projects(input_file)
print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects)

random.shuffle(projects)
assignments = solver(projects, contributors)
# print("\nResult:", assignments)

# Save assignments to output file
save_assignments(assignments, "C:\\Users\\uran_\\Desktop\\mentorship-and-teamwork-validator\\Solutions\\test_f.txt")
#save_assignments(assignments, "C:\\Users\\Elvir Misini\\Desktop\\mentorship_teamwork_ils\\output\\test_test_a.txt")