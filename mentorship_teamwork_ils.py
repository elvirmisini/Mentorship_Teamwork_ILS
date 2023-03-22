from modules.input_and_output_file_choice import *
from modules.instance_parse import *
from modules.fitness import *
from modules.submission_file import *

import random

final_assignments = {}
def asign_and_solve(projects,contributors):
    assignments = {}
    unassigned_projects = []
    
    for project in projects:
        assigned_contributors = []
        for skill, level in project['skills'].items():
            candidate_contributors = []
            for contributor, skills in contributors.items():
                if skill in skills:
                    if skills[skill] >= level:
                        candidate_contributors.append(contributor)
                        if skills[skill] == level:
                            skills[skill]+=1
                    elif skill in project['skills'] and skills[skill] == level - 1:
                        for mentor in assigned_contributors:
                            if mentor != contributor and skill in contributors[mentor] and contributors[mentor][skill] >= level:
                                candidate_contributors.append(contributor)
                                skills[skill]+=1
                                assignments[contributor] = mentor
                                break
            if candidate_contributors:
                contributor = random.choice(candidate_contributors)
                assigned_contributors.append(contributor)
                if contributor in assignments:
                    if isinstance(assignments[contributor], str):
                        assignments[contributor] = [assignments[contributor], project['name']]
                    else:
                        assignments[contributor].append(project['name'])
                else:
                    assignments[contributor] = [project['name']]

        if assigned_contributors:
            continue
        else:
            unassigned_projects.append(project)
    return {"unassigned":unassigned_projects,"assignments":assignments,"contributors":contributors}

first_result=asign_and_solve(projects,contributors)
final_assignments=first_result['assignments']

repeat=True
while repeat:
    if len(first_result['unassigned'])!=0:
        new_one=asign_and_solve(first_result['unassigned'],first_result['contributors'])['assignments']

        for key, value in new_one.items():
            if isinstance(value, str):
                value = [x.strip() for x in value.split(',')]
            if isinstance(final_assignments.get(key), str):
                final_assignments[key] = [final_assignments[key]]
            final_assignments[key] = final_assignments.get(key, []) + value
    repeat=False

print("\nFitness score:", get_fitness_value(projects, contributors, final_assignments))
print("\nResult:", final_assignments)
save_submission_file(output_file, projects, final_assignments)