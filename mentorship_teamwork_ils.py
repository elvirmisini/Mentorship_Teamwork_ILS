from typing import List, Dict
import random

with open("instances/a_an_example.in.txt", "r") as f:
#with open("instances/b_better_start_small.in.txt", "r") as f:
#with open("instances/c_collaboration.in.txt", "r") as f:
#with open("instances/d_dense_schedule.in.txt", "r") as f:
#with open("instances/e_exceptional_skills.in.txt", "r") as f:
#with open("instances/f_find_great_mentors.in.txt", "r") as f:
#with open("instances/class_task.in.txt", "r") as f:
    # read the first line
    c, p = map(int, f.readline().split())

    contributors = {}
    for i in range(c):
        name, n = f.readline().split()
        n = int(n)
        skills = {}
        for j in range(n):
            skill, li = f.readline().split()
            li = int(li)
            skills[skill] = li
        contributors[name] = skills

    projects = []
    for i in range(p):
        name, di, si, bi, ri = f.readline().split()
        di, si, bi, ri = int(di), int(si), int(bi), int(ri)
        skills = {}
        for j in range(ri):
            skill, lk = f.readline().split()
            lk = int(lk)
            skills[skill] = lk
        project = {
            "name": name,
            "days": di,
            "score": si,
            "best_before": bi,
            "skills": skills
        }
        projects.append(project)


print({'Contributors': c, 'Projects': p})
print('')
print("Contributors:")
print(contributors)
print('')
print("Projects: ")
print(projects)

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


def fitness(assignments, projects):
    total_score = 0
    for contributor, assigned_projects in assignments.items():
        for project in assigned_projects:
            for p in projects:
                if p['name'] == project:
                    total_score += p['score']
    return total_score

fitness_score = fitness(final_assignments, projects)
print()
print("Fitness score: ", fitness_score)
print
printed_values = set()


#save submmision file
with open("output/a_an_example.out.txt", "w") as f:
#with open("output/b_better_start_small.out.txt", "w") as f:
#with open("output/c_collaboration.out.txt", "w") as f:
#with open("output/d_dense_schedule.out.txt", "w") as f:
#with open("output/e_exceptional_skills.out.txt", "w") as f:
#with open("output/f_find_great_mentors.out.txt", "w") as f:
#with open("output/class_task.out.txt", "w") as f:
    final_assignments = {k: v if isinstance(v, list) else [v.strip() for v in v.split(',')] for k, v in final_assignments.items()}
    f.write(str(len(projects)) + "\n")
    print(str(len(projects)))
    for value in sorted(set(sum(final_assignments.values(), []))):
        print(value)
        f.write(value + "\n")
        for key, values in sorted(final_assignments.items()):
            if value in values and value not in printed_values:
                print(key)
                f.write(key + " ")
        printed_values.add(value)
        f.write("\n")
