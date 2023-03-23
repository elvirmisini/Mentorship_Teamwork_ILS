def check_if_contributor_is_in_multiple_roles_in_one_project(result):
    for project_name, contributors in result.items():
        if len(contributors) != len(set(contributors)):
            print(f"Error: Multiple occurrences of the same contributor in project '{project_name}'.")
            return False
    return True

def check_that_all_contributors_in_the_result_are_valid(contributors, result):
    for project_name, assigned_contributors in result.items():
        for contributor_name in assigned_contributors:
            if contributor_name not in contributors:
                print(f"Invalid result: {contributor_name} is not a valid contributor")
                return False
    return True

def check_that_all_projects_in_the_result_are_valid(projects, result):
    for project_name in result.keys():
        if project_name not in [p['name'] for p in projects]:
            print(f"Invalid result: {project_name} is not a valid project")
            return False
    return True

def validate_solution(contributors, projects, result):
    return check_if_contributor_is_in_multiple_roles_in_one_project(result) \
        and check_that_all_contributors_in_the_result_are_valid(contributors, result) \
        and check_that_all_projects_in_the_result_are_valid(projects, result)