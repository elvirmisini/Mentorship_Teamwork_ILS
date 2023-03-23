# this function looks at the values of the end work day for every contributor that we give
# and returns the last work day from those contriburors, for example
# contributor 1 works until day 17, contributor 2 works until day 5, this function returns 17
def get_the_latest_day_of_all_given_contributors(all_contributors, assignment_contributors):
    latest_work_day = max(all_contributors.get(name, 0) for name in assignment_contributors)
    return latest_work_day

# this function converts this format {'Contributor1': ['Project1', 'Project2'], 'Contributor2': ['Project1']}
# to this format {'Project1': ['Contributor1', 'Contributor2'], 'Project2': ['Contributor2']}
def convert_info_from_contributor_based_to_project_based(contributor_based_assignments):
    project_based_assignments = {}
    for contributor, projects in contributor_based_assignments.items():
        for project in projects:
            if project in project_based_assignments:
                project_based_assignments[project].append(contributor)
            else:
                project_based_assignments[project] = [contributor]
    return project_based_assignments

# for example contributor 1 end the work for this project in the 17 day, but contributor 2 can start erlier so
# it can finish the work erlier, this function sets the end work day 17 for both contributors
def set_the_latest_end_day_to_all_contributors_of_a_project(assignment_contributors, contributors_final_work_day):
    latest_work_day = max(contributors_final_work_day.get(name, 0) for name in assignment_contributors)
    for contributor in assignment_contributors:
        contributors_final_work_day[contributor] = latest_work_day

def get_the_fitness_value(projects, contributors, assignments):
    # this dictonary saves the final day of work for every contributor 
    cuntributors_final_work_day = {}
    # in the begining every contributor starts at 0 work days
    for contributor in contributors:
        cuntributors_final_work_day[contributor] = 0

    total_projects_score = 0

    for assignmet_projects, assignmet_contributors in assignments.items():
        number_of_days_to_complete_project = 0

        for project in projects:
            if project["name"] == assignmet_projects:
                number_of_days_to_complete_project = project["days"] 
                project_best_before_days = project["best_before"]
                project_score = project["score"]
                break

        for contributor in assignmet_contributors:
            cuntributors_final_work_day[contributor] = cuntributors_final_work_day[contributor] + number_of_days_to_complete_project
        
        set_the_latest_end_day_to_all_contributors_of_a_project(assignmet_contributors, cuntributors_final_work_day)

        end_work_day_of_project = get_the_latest_day_of_all_given_contributors(cuntributors_final_work_day, assignmet_contributors)

        # here is calculated the score of the current project and added to the total_score
        if project_best_before_days > end_work_day_of_project:
            total_projects_score = total_projects_score + project_score
        elif (project_score - (end_work_day_of_project - project_best_before_days)) >= 0:
            total_projects_score = total_projects_score + (project_score - (end_work_day_of_project - project_best_before_days))
        else:
            total_projects_score = total_projects_score + 0

    return total_projects_score