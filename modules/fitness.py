
def max_value_for_names(name_dict, name_list):
    name_values = [name_dict.get(name) for name in name_list]
    max_value = max(name_values)
    return max_value

def get_fitness_value(projects, contributors, final_assignments):
    assignments = {}
    for name, skills in final_assignments.items():
        for skill in skills:
            if skill in assignments:
                assignments[skill].append(name)
            else:
                assignments[skill] = [name]

    cuntributors_current_day = {}
    for c in contributors:
        cuntributors_current_day[c] = 0

    final_day = 0
    total_score = 0
    for assignmet_project, assignmet_contributors in assignments.items():
        max_day = 0
        current_day = 0
        for p in projects:
            if p["name"] == assignmet_project:
                current_day = p["days"] 

        for c in assignmet_contributors:
            cuntributors_current_day[c] = cuntributors_current_day[c] + current_day
        
        for c in assignmet_contributors:
            if c in cuntributors_current_day:
                if cuntributors_current_day[c] > max_day:
                    max_day = cuntributors_current_day[c]
        
        final_day = max_value_for_names(cuntributors_current_day, assignmet_contributors)

        for c in assignmet_contributors:
            cuntributors_current_day[c] = max_day

        for p in projects:
            if p["name"] in assignmet_project:
                # print("final day", final_day)
                # print("best before", p["best_before"])
                if p["best_before"] > final_day:
                    total_score = total_score + p["score"]
                else:
                    total_score = total_score + (p["score"] - (final_day - p["best_before"]))
        
        # print("total score", total_score)
        # print("current day", cuntributors_current_day, "\n")

    return total_score

