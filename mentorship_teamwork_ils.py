from typing import List, Dict
import random


with open("instances/a_an_example.in.txt", "r") as f:
#with open("instances/b_better_start_small.in.txt", "r") as f:
#with open("instances/c_collaboration.in.txt", "r") as f:
#with open("instances/d_dense_schedule.in.txt", "r") as f:
#with open("instances/e_exceptional_skills.in.txt", "r") as f:
#with open("instances/f_find_great_mentors.in.txt", "r") as f:
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
print('')


final_assignments = {}
def asign_and_solve(projects,contributors):
    assignments = {}
    unassigned_projects = []
    print(len(projects))
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
                    assignments[contributor].append(project['name'])
                else:
                    assignments[contributor] = [project['name']]
            else:
                print(f"No candidates found for project {project['name']}")

        if assigned_contributors:
            print(project['name'])
            for contributor in assigned_contributors:
                if contributor in assignments:
                    print(f"{contributor}")
                else:
                    print(contributor)
        else:
           # print(f"No contributors found for project {project['name']}")
            unassigned_projects.append(project)
        #print(contributors)
        #print(assignments)
        
    #print(f"Unassigned projects: {unassigned_projects}")
    return {"unasigned":unassigned_projects,"sas":assignments,"contributors":contributors}

first_result=asign_and_solve(projects,contributors)

# print(first_result)

# if len(first_result['unasigned'])!=0:
#      print(asign_and_solve(first_result['unasigned'],first_result['contributors']))
