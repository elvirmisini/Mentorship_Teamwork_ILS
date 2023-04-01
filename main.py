from modules.file_selector import *
from modules.instance_parse import *
from modules.fitness_calculator import *
from modules.submission_saver import *
from modules.initial_solution import *
from modules.solution_validation import *

# Read input file and print contributors and projects info
number_of_contributors, number_of_projects, contributors, projects = read_contributors_and_projects(input_file)
print_contributors_and_projects_info(number_of_contributors, number_of_projects, contributors, projects)

#Implement One Operator: Chose randomly a project, modify best_before and score
# randomly with a value from -20 to +20, but best_days and score should not be lower than 1
#see the results generated with this change

selected_project = random.choice(projects)
random_offset = random.randint(-20, 20)
selected_project['best_before'] = max(selected_project['best_before'] + random_offset, 1)
selected_project['score']  = max(selected_project['best_before'] + random_offset, 1)


# Assign and solve projects
first_result = assign_and_solve(projects, contributors)
final_assignments = first_result['assignments']

# Update assignments until no unassigned tasks left
update_assignments(first_result, final_assignments)

# Calculate and print fitness score and final assignments
assignments = convert_info_from_contributor_based_to_project_based(final_assignments)
fitness_score = get_the_fitness_value(projects, contributors, assignments)
print("\nFitness score:", fitness_score)
print("\nResult:", assignments)

# Save assignments to output file
save_assignments(assignments, output_file)

if validate_solution(contributors, projects, assignments):
    print("\nThe solution is valid")
else:
    print("\nThe solution is not valid")