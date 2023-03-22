input_file_list = [
    "instances/a_an_example.in.txt",
    "instances/b_better_start_small.in.txt",
    "instances/c_collaboration.in.txt",
    "instances/d_dense_schedule.in.txt",
    "instances/e_exceptional_skills.in.txt",
    "instances/f_find_great_mentors.in.txt",
    "instances/class_task.in.txt"
]

output_file_list = [
    "output/a_an_example.out.txt",
    "output/b_better_start_small.out.txt",
    "output/c_collaboration.out.txt",
    "output/d_dense_schedule.out.txt",
    "output/e_exceptional_skills.out.txt",
    "output/f_find_great_mentors.out.txt",
    "output/class_task.out.txt"
]

file_choice = int(input("""Write 0 for a_an_example
      1 for b_better_start_small
      2 for c_collaboration
      3 for d_dense_schedule
      4 for e_exceptional_skills
      5 for f_find_great_mentors
      6 for class_task

File choice: """))

input_file = input_file_list[file_choice]
output_file = output_file_list[file_choice]
