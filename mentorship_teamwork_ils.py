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
