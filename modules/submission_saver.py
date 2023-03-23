def save_assignments(assignments, output_file):
    with open(output_file, 'w') as f:
        # Write the number of projects
        f.write(f"{len(assignments)}\n")
        
        # Write the project names and team members
        for project, team_members in assignments.items():
            # Write the project name
            f.write(f"{project}\n")
            
            # Write the team members
            f.write(" ".join(team_members))
            f.write("\n")