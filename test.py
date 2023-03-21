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
        
        final_day = max(cuntributors_current_day.values())
        
        for c in assignmet_contributors:
            cuntributors_current_day[c] = max_day

        for p in projects:
            if p["name"] in assignmet_project:
                print("final day ", final_day)
                if p["best_before"] > final_day:
                    total_score = total_score + p["score"]
                else:
                    total_score = total_score + (p["score"] - (final_day - p["best_before"]))
        
        print(total_score)
        print(cuntributors_current_day)

    return total_score

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
    'Logging': ['Anna'],
    'WebChat': ['Bob', 'Maria']
}

print(calculate_score(projects, contributors, assignments))