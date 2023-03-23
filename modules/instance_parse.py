def read_contributors_and_projects(input_file):
    """
    Reads the given input file and returns the contributors and projects information
    in a structured way.

    Args:
        input_file (str): The path of the input file.

    Returns:
        Tuple:
            A tuple containing the number of contributors and projects, contributors' information
            and the projects' information respectively.
    """
    with open(input_file, "r") as f:
        # read the first line
        number_of_contributors, number_of_projects = map(int, f.readline().split())

        # read contributors information
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

        # read projects information
        projects = []
        for i in range(number_of_projects):
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

    return number_of_contributors, number_of_projects, contributors, projects

def print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects):
    print({'Number of contributors': number_of_contributors, 'Number of projects': number_of_projects})
    print('')
    print("Contributors:")
    print(contributors)
    print('')
    print("Projects: ")
    print(projects)