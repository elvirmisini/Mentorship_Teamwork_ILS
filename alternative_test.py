from modules.file_selector import *
from modules.instance_parse import *
from modules.fitness_calculator import *
from modules.submission_saver import *
from modules.initial_solution import *
from modules.solution_validation import *

# Read input file and print contributors and projects info
number_of_contributors, number_of_projects, contributors, projects = read_contributors_and_projects(input_file)
print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects)

fitnes_score_list = []
while len(fitnes_score_list) < 5:
    # Assign and solve projects
    first_result = assign_and_solve(projects, contributors)
    final_assignments = first_result['assignments']

    # Update assignments until no unassigned tasks left
    update_assignments(first_result, final_assignments)

    # Calculate and print fitness score and final assignments
    assignments = convert_info_from_contributor_based_to_project_based(final_assignments)
    
    if number_of_contributors > 100 and validate_solution(contributors, projects, assignments): 
        fitness_score = get_the_fitness_value(projects, contributors, assignments)
        fitnes_score_list.append(fitness_score)
        break
    
    if validate_solution(contributors, projects, assignments):        
        fitness_score = get_the_fitness_value(projects, contributors, assignments)
        fitnes_score_list.append(fitness_score)

print(len(fitnes_score_list))
print(fitnes_score_list)
print("The assignment with the best fitness score", max(fitnes_score_list))

# Save assignments to output file
save_assignments(assignments, output_file)
    

