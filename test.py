def max_value_for_names(name_dict, name_list):
    name_values = [name_dict.get(name) for name in name_list]
    max_value = max(name_values)
    return max_value

def calculate_score(projects, contributors, assignments):
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
                print("final day", final_day)
                print("best before", p["best_before"])
                if p["best_before"] > final_day:
                    total_score = total_score + p["score"]
                else:
                    total_score = total_score + (p["score"] - (final_day - p["best_before"]))
        
        print("total score", total_score)
        print("current day", cuntributors_current_day, "\n")

    return total_score

# contributors = {
#     'Ilire': {'C++': 2, 'Python': 3}, 
#     'Diell': {'HTML': 5, 'CSS': 5, 'C++': 2, 'JavaScript': 3}, 
#     'Maria': {'Python': 3}, 
#     'Flutura': {'JavaScript': 2}, 
#     'Njomza': {'C++': 2, 'JavaScript': 2, 'HTML': 1}, 
#     'Bajram': {'HTML': 1, 'PHP': 2, 'Python': 3}, 
#     'Dardan': {'HTML': 3, 'SQL': 3}
# }

# projects = [
#     {'name': 'Logging', 'days': 5, 'score': 10, 'best_before': 5, 'skills': {'C++': 3}}, 
#     {'name': 'WebServer', 'days': 7, 'score': 10, 'best_before': 7, 'skills': {'HTML': 3, 'C++': 2}}, 
#     {'name': 'WebChat', 'days': 10, 'score': 20, 'best_before': 20, 'skills': {'Python': 3, 'HTML': 3}}, 
#     {'name': 'Database', 'days': 10, 'score': 15, 'best_before': 18, 'skills': {'SQL': 2}}, 
#     {'name': 'WebSockets', 'days': 15, 'score': 20, 'best_before': 20, 'skills': {'JavaScript': 3, 'Python': 2}}, 
#     {'name': 'Comments', 'days': 3, 'score': 5, 'best_before': 12, 'skills': {'JavaScript': 2}}, 
#     {'name': 'Archive', 'days': 20, 'score': 30, 'best_before': 25, 'skills': {'Javascript': 2, 'Python': 1, 'HTML': 3, 'CSS': 2}}, 
#     {'name': 'JoinMeet', 'days': 18, 'score': 35, 'best_before': 40, 'skills': {'Javascript': 3, 'Python': 2, 'SQL': 3}}, 
#     {'name': 'Upload', 'days': 12, 'score': 20, 'best_before': 18, 'skills': {'Python': 3, 'SQL': 3}}, 
#     {'name': 'People', 'days': 5, 'score': 10, 'best_before': 12, 'skills': {'JavaScript': 2}}
# ]

# assignments = {
#     'WebServer': ['Dardan', 'Ilire'], 
#     'WebChat': ['Dardan', 'Maria'], 
#     'Database': ['Dardan'], 
#     'Archive': ['Dardan', 'Maria', 'Diell'], 
#     'JoinMeet': ['Dardan', 'Bajram'], 
#     'Upload': ['Dardan', 'Bajram'], 
#     'Logging': ['Ilire'], 
#     'WebSockets': ['Maria', 'Diell'], 
#     'Comments': ['Flutura'], 
#     'People': ['Njomza']
# }

projects = [
    {'name': 'WebServer', 'days': 7, 'score': 10, 'best_before': 7, 'skills': {'HTML': 3, 'C++': 2}}, 
    {'name': 'Logging', 'days': 5, 'score': 10, 'best_before': 5, 'skills': {'C++': 3}}, 
    {'name': 'WebChat', 'days': 10, 'score': 20, 'best_before': 20, 'skills': {'Python': 3, 'HTML': 3}}
]

contributors = {
    'Anna': {'C++': 2}, 
    'Bob': {'HTML': 5, 'CSS': 5}, 
    'Maria': {'Python': 3}
}

assignments = {
    'WebServer': ['Bob', 'Anna'], 
    'WebChat': ['Bob', 'Maria'], 
    'Logging': ['Anna']
}

print("Fitness score", calculate_score(projects, contributors, assignments))