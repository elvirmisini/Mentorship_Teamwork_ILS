def get_the_latest_day_of_all_given_contributors(all_contributors, assignment_contributors):
    """
    Given all contributors and the list of assignment contributors, returns the latest work day from those contributors.
    """
    latest_work_day = max(all_contributors.get(name, 0) for name in assignment_contributors)
    return latest_work_day


def convert_info_from_contributor_based_to_project_based(contributor_based_assignments):
    """
    Converts the assignment information from a contributor-based to a project-based format.
    """
    project_based_assignments = {}
    for contributor, projects in contributor_based_assignments.items():
        for project in projects:
            if project in project_based_assignments:
                project_based_assignments[project].append(contributor)
            else:
                project_based_assignments[project] = [contributor]
    return project_based_assignments


def set_the_latest_end_day_to_all_contributors_of_a_project(assignment_contributors, contributors_final_work_day):
    """
    Sets the latest end work day for all contributors of a project.
    """
    latest_work_day = max(contributors_final_work_day.get(name, 0) for name in assignment_contributors)
    for contributor in assignment_contributors:
        contributors_final_work_day[contributor] = latest_work_day


def get_the_fitness_value(projects, contributors, assignments):
    """
    Calculates the fitness value of the assignments.
    """
    # Initialize the dictionary that saves the final day of work for every contributor.
    cuntributors_final_work_day = {contributor: 0 for contributor in contributors}

    total_projects_score = 0

    for assignmet_projects, assignmet_contributors in assignments.items():
        number_of_days_to_complete_project = 0

        # Get the number of days needed to complete the current project.
        for project in projects:
            if project["name"] == assignmet_projects:
                number_of_days_to_complete_project = project["days"]
                project_best_before_days = project["best_before"]
                project_score = project["score"]
                break

        # Update the final work day for each contributor assigned to the project.
        for contributor in assignmet_contributors:
            cuntributors_final_work_day[contributor] += number_of_days_to_complete_project

        set_the_latest_end_day_to_all_contributors_of_a_project(assignmet_contributors, cuntributors_final_work_day)

        # Get the latest end work day of all contributors assigned to the project.
        end_work_day_of_project = get_the_latest_day_of_all_given_contributors(cuntributors_final_work_day, assignmet_contributors)

        # Calculate the score of the current project and add it to the total score.
        if project_best_before_days > end_work_day_of_project:
            total_projects_score += project_score
        elif (project_score - (end_work_day_of_project - project_best_before_days)) >= 0:
            total_projects_score += (project_score - (end_work_day_of_project - project_best_before_days))
        else:
            total_projects_score += 0

    return total_projects_score