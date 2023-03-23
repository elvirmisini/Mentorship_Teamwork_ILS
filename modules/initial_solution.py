import random

def assign_and_solve(projects, contributors):
    """
    Assigns contributors to projects based on skills and skill levels, 
    and returns a dictionary with the unassigned projects, assignments,
    and the updated contributors with new skill levels.

    Args:
        projects (list): A list of dictionaries, where each dictionary represents a project 
                         and contains the project's name, duration, score, best before date, 
                         and required skills with corresponding skill levels.
        contributors (dict): A dictionary where each key is a contributor's name and each 
                             value is another dictionary representing the contributor's skills 
                             and their corresponding skill levels.

    Returns:
        dict: A dictionary containing three keys: 'unassigned', 'assignments', and 'contributors'.
              'unassigned' contains a list of unassigned projects, 'assignments' contains a dictionary 
              where each key is a contributor's name and each value is a list of projects assigned to 
              that contributor, and 'contributors' contains the updated dictionary of contributors with 
              new skill levels.
    """
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
                            skills[skill] += 1
                    elif skill in project['skills'] and skills[skill] == level - 1:
                        for mentor in assigned_contributors:
                            if (mentor != contributor 
                                and skill in contributors[mentor] 
                                and contributors[mentor][skill] >= level):
                                candidate_contributors.append(contributor)
                                skills[skill] += 1
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

    return {"unassigned": unassigned_projects, "assignments": assignments, "contributors": contributors}

def update_assignments(first_result, final_assignments):
    repeat = True
    while repeat:
        if len(first_result['unassigned']) != 0:
            new_one = assign_and_solve(first_result['unassigned'], first_result['contributors'])['assignments']

            for key, value in new_one.items():
                if isinstance(value, str):
                    value = [x.strip() for x in value.split(',')]
                if isinstance(final_assignments.get(key), str):
                    final_assignments[key] = [final_assignments[key]]
                final_assignments[key] = final_assignments.get(key, []) + value
        repeat = False