def read_contributors_and_projects(filename):
    try:
        with open(filename, "r") as f:
            if not f.read():
                print(f"Error: The file {filename} is empty.")
                exit()

        with open(filename, "r") as f:
            number_of_contributors, number_of_projects = map(int, f.readline().split())

            contributors = {}
            for i in range(number_of_contributors):
                name, n = f.readline().split()
                n = int(n)
                skills = {}
                for j in range(n):
                    skill, li = f.readline().split()
                    li = int(li)
                    skills[skill] = li

                contributors[name] = skills

            projects = []
            for i in range(number_of_projects):
                name, di, si, bi, ri = f.readline().split()
                di, si, bi, ri = int(di), int(si), int(bi), int(ri)
                skills = []
                for j in range(ri):
                    skill, lk = f.readline().split()
                    lk = int(lk)
                    skills.append({skill:lk})

                project = {
                    "name": name,
                    "days": di,
                    "score": si,
                    "best_before": bi,
                    "skills": skills,
                }
                projects.append(project)

        return number_of_contributors, number_of_projects, contributors, projects

    except IOError:
        print(f"Error: Could not open file '{filename}'.")
        exit()

def print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects):
    print({'Number of contributors': number_of_contributors, 'Number of projects': number_of_projects})
    print('')
    print("Contributors:")
    print(contributors)
    print('')
    print("Projects: ")
    print(projects)