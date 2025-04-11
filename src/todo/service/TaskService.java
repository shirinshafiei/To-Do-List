package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Task.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskService {

    public static int addTask(String title, String description, Date dueDate) throws InvalidEntityException {
        Task task = new Task(title, description, dueDate, Status.NotStarted);
        Database.add(task);
        return Database.ID - 1;
    }

    public static void deleteTask(int id) {

        StepService.deleteAllStepsForTask(id);

    }

    public static void updateTaskTitle(int taskId, String newTitle) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setTitle(newTitle);
        Database.update(task);
    }

    public static void updateTaskDescription(int taskId, String newDescription) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setDescreption(newDescription);
        Database.update(task);
    }

    public static void updateTaskDueDate(int taskId, Date newDueDate) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setDueDate(newDueDate);
        Database.update(task);
    }

    public static void setTaskStatus(int taskId, Status newStatus) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(newStatus);
        Database.update(task);

        if (newStatus == Status.Completed) {
            StepService.completeAllStepsForTask(taskId);
        }
    }

    public static Task getTaskById(int taskId) {
        return (Task) Database.get(taskId);
    }

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Entity entity : Database.getAll(Task.Task_ENTITY_CODE)) {
            tasks.add((Task) entity);
        }
        tasks.sort((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));
        return tasks;
    }

    public static List<Task> getIncompleteTasks() {
        List<Task> incompleteTasks = new ArrayList<>();
        for (Task task : getAllTasks()) {
            if (task.getStatus() != Status.Completed) {
                incompleteTasks.add(task);
            }
        }
        return incompleteTasks;
    }

    public static boolean taskExists(int taskId) {
        try {
            Database.get(taskId);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}