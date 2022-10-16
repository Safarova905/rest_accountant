package rest.accountant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rest.accountant.entities.Task;
import rest.accountant.services.TaskService;

@RestController("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("tasks", taskService);
        System.out.println("Returning tasks:");
        return "tasks";
    }
    @GetMapping("/{id}")
    public String showTask(@PathVariable Integer id, Model model) {
        model.addAttribute("task", taskService);
        return "taskShow";
    }
    @PutMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("task", taskService);
        return "taskForm";
    }

    @RequestMapping("/task/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        return "taskForm";
    }
    @RequestMapping(value = "task", method = RequestMethod.POST)
    public String saveTask(Task task) {
        taskService.saveTask(task);
        return "redirect:/task/" + task.getId();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

}
