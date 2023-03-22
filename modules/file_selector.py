file_paths = {
    'a': {
        'input': 'instances/a_an_example.in.txt',
        'output': 'output/a_an_example.out.txt'
    },
    'b': {
        'input': 'instances/b_better_start_small.in.txt',
        'output': 'output/b_better_start_small.out.txt'
    },
    'c': {
        'input': 'instances/c_collaboration.in.txt',
        'output': 'output/c_collaboration.out.txt'
    },
    'd': {
        'input': 'instances/d_dense_schedule.in.txt',
        'output': 'output/d_dense_schedule.out.txt'
    },
    'e': {
        'input': 'instances/e_exceptional_skills.in.txt',
        'output': 'output/e_exceptional_skills.out.txt'
    },
    'f': {
        'input': 'instances/f_find_great_mentors.in.txt',
        'output': 'output/f_find_great_mentors.out.txt'
    },
    'class': {
        'input': 'instances/class_task.in.txt',
        'output': 'output/class_task.out.txt'
    }
}

file_choice = input("""Write the letter corresponding to the file you want to choose:
      a for a_an_example
      b for b_better_start_small
      c for c_collaboration
      d for d_dense_schedule
      e for e_exceptional_skills
      f for f_find_great_mentors
      class for class_task

File choice: """)

input_file = file_paths[file_choice]['input']
output_file = file_paths[file_choice]['output']
