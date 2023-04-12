# Mentorship_Teamwork_ILS

> In this link our target was to find an initial solution (not with Iteratet Local Search) for the problem in this link: [Mentorship and Teamwork](https://codingcompetitions.withgoogle.com/hashcode/round/00000000008caae7/000000000098afc8).

This solution prezented in here does not always finds a solution, because we implemented the element of randomness, so it does not make logical choses for contributors.
In the other phaze of this project our target is to modify this code, so the solution qill be with ILS(Iterated Local Search)

> To execute this project tou need to type this command in terminal:

```python
python .\mentorship_teamwork_ils.py
```
> than you will need to specify which file you want to find the solution:

```console
Write the letter corresponding to the file you want to choose:
      a for a_an_example
      b for b_better_start_small
      c for c_collaboration
      e for e_exceptional_skills
      f for f_find_great_mentors
      class for class_task
```
>> for example your input is "a"
>>than your results will be like this:

Data parsed like this:
```yaml
File choice: a
{'Number of contributors': 3, 'Number of projects': 3}

Contributors:
{'Anna': {'C++': 2}, 'Bob': {'HTML': 5, 'CSS': 5}, 'Maria': {'Python': 3}}

Projects:
[{'name': 'Logging', 'days': 5, 'score': 10, 'best_before': 5, 'skills': {'C++': 3}}, {'name': 'WebServer', 'days': 7, 'score': 10, 'best_before': 7, 'skills': {'HTML': 3, 'C++': 2}}, {'name': 'WebChat', 'days': 10, 'score': 20, 'best_before': 20, 'skills': {'Python': 3, 'HTML': 3}}]

Fitness score: 33

Result: {'WebServer': ['Bob', 'Anna'], 'WebChat': ['Bob', 'Maria'], 'Logging': ['Anna']}

The solution is valid
```


and of course the output file will be saved like this:

```text
3
Logging
Anna 
WebChat
Bob Maria 
WebServer
Anna Bob 
```

# Phase 2:


### To execute this phaze, you just need to run the test.java file


On this phaze we had to implement one operator, to make the initial algorithm structure and also to define the main algorithm parameters, this time we did with Java programming language, so you need to execute the main.java function.

> than you need to chose one file:


![image](https://user-images.githubusercontent.com/58117020/230908708-e607e864-f0c9-457b-a27d-1b5993c5f1bf.png)
> for example "a", and we get this output

![image](https://user-images.githubusercontent.com/58117020/230908994-0425daff-f308-4856-b9df-d925d476319e.png)

> or if it is not vliad:

![image](https://user-images.githubusercontent.com/58117020/231461472-e7402014-edc3-423e-995d-913394ed4db5.png)

