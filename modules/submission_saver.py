def save_assignments(output_file_path, project_names, final_assignments):
    printed_values = set()
    with open(output_file_path, "w") as f:
        final_assignments = {k: v if isinstance(v, list) else [v.strip() for v in v.split(',')] for k, v in final_assignments.items()}
        f.write(str(len(project_names)) + "\n")
        print(str(len(project_names)))
        for value in sorted(set(sum(final_assignments.values(), []))):
            # print(value)
            f.write(value + "\n")
            for key, values in sorted(final_assignments.items()):
                if value in values and value not in printed_values:
                    # print(key)
                    f.write(key + " ")
            # printed_values.add(value)
            f.write("\n")