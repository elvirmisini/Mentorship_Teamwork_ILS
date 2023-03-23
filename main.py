from modules.file_selector import *
from modules.instance_parse import *
from modules.fitness_calculator import *
from modules.submission_saver import *
from modules.initial_solution import *

# Read input file and print contributors and projects info
number_of_contributors, number_of_projects, contributors, projects = read_contributors_and_projects(input_file)
print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects)

# Assign and solve projects
first_result = assign_and_solve(projects, contributors)
final_assignments = first_result['assignments']

# Update assignments until no unassigned tasks left
update_assignments(first_result, final_assignments)

# Calculate and print fitness score and final assignments
converted_assignments = convert_info_from_contributor_based_to_project_based(final_assignments)
fitness_score = get_the_fitness_value(projects, contributors, converted_assignments)
print("\nFitness score:", fitness_score)
print("\nResult:", final_assignments)

# Save assignments to output file
save_assignments(output_file, projects, final_assignments)