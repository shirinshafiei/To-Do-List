package todo;

import db.Database;
import todo.entity.Step;
import todo.service.StepService;
import todo.service.TaskService;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Task.Status;
import todo.entity.Step.stepStatus;
import todo.validator.StepValidator;
import todo.validator.TaskValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        Database.registerValidator(Task.Task_ENTITY_CODE, new TaskValidator());
        Database.registerValidator(Step.STEP_ENTITY_CODE, new StepValidator());
        System.out.println("Welcome to To-Do List Application");
        System.out.println("1.add task\n" +
                "2.add step\n" +
                "3.delet task\n" +
                "4.delet step\n" +
                "5.update task\n" +
                "6.update step\n" +
                "7.get task-by-id\n" +
                "8.get all-tasks\n" +
                "9.get incomplete-tasks\n" +
                "10.Exit");
        System.out.println("Enter your command (type 'exit' to quit):");

        while (true) {
            int command = scanner.nextInt();
            scanner.nextLine();
            try {
                switch (command) {
                    case 1:
                        handleAddTask();
                        break;
                    case 2:
                        handleAddStep();
                        break;
                    case 3:
                        handleDeleteTask();
                        break;
                    case 4:
                        handleDeleteStep();
                        break;
                    case 5:
                        handleUpdateTask();
                        break;
                    case 6:
                        handleUpdateStep();
                        break;
                    case 7:
                         handleGetTaskById();
                         break;
                    case 8:
                        printTaskDetails();
                        break;
                    case 9:
                        handleGetIncompleteTasks();
                        break;
                    case 10:
                        return;

                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("\nEnter your next command:");
            System.out.println("1.add task\n" +
                    "2.add step\n" +
                    "3.delet task\n" +
                    "4.delet step\n" +
                    "5.update task\n" +
                    "6.update step\n" +
                    "7.get task-by-id\n" +
                    "8.get all-tasks\n" +
                    "9.get incomplete-tasks\n" +
                    "10.Exit");
        }
    }

    private static void handleAddTask() {
        try {
            System.out.println("Title:");
            String title = scanner.nextLine().trim();

            System.out.println("Description:");
            String description = scanner.nextLine().trim();

            System.out.println("Due date (yyyy-mm-dd):");
            String dueDateStr = scanner.nextLine().trim();
            Date dueDate = dateFormat.parse(dueDateStr);

            int taskId = TaskService.addTask(title, description, dueDate);
            System.out.println("Task saved successfully.");
            System.out.println("ID: " + taskId);
        } catch (ParseException e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: Invalid date format. Please use yyyy-mm-dd.");
        } catch (InvalidEntityException e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleAddStep() {
        try {
            System.out.println("TaskID:");
            int taskId = scanner.nextInt();


            if (!TaskService.taskExists(taskId)) {
                System.out.println("Cannot save step.");
                System.out.println("Error: Cannot find task with ID=" + taskId);
                return;
            }

            System.out.println("Title:");
            String title = scanner.nextLine().trim();
            scanner.nextLine();

            int stepId = StepService.addStep(taskId, title);
            System.out.println("Step saved successfully.");
            System.out.println("ID: " + stepId);
            System.out.println("Creation Date: " + new Date());
        } catch (NumberFormatException e) {
            System.out.println("Cannot save step.");
            System.out.println("Error: Invalid Task ID format. Please enter a number.");
        } catch (InvalidEntityException e) {
            System.out.println("Cannot save step.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleDeleteTask() {
        try {
            System.out.println("ID:");
            int id = scanner.nextInt();
            scanner.nextLine();

            TaskService.deleteTask(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");
        } catch (NumberFormatException e) {
            System.out.println("Cannot delete entity.");
            System.out.println("Error: Invalid ID format. Please enter a number.");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete entity.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleDeleteStep() {
        try {
            System.out.println("ID:");
            int id = scanner.nextInt();
            scanner.nextLine();

            StepService.deleteStep(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");
        } catch (NumberFormatException e) {
            System.out.println("Cannot delete entity.");
            System.out.println("Error: Invalid ID format. Please enter a number.");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete entity.");
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidEntityException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleUpdateTask() {
        try {
            System.out.println("ID:");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Field (title/description/due date/status):");
            String field = scanner.nextLine().trim().toLowerCase();

            System.out.println("New Value:");
            String newValue = scanner.nextLine().trim();

            Task task = TaskService.getTaskById(id);
            if (task == null) {
                System.out.println("Cannot update task with ID=" + id);
                System.out.println("Error: Cannot find entity with id=" + id);
                return;
            }

            String oldValue;
            switch (field) {
                case "title":
                    oldValue = task.getTitle();
                    TaskService.updateTaskTitle(id, newValue);
                    break;
                case "description":
                    oldValue = task.getDescreption();
                    TaskService.updateTaskDescription(id, newValue);
                    break;
                case "due date":
                    oldValue = dateFormat.format(task.getDueDate());
                    Date newDueDate = dateFormat.parse(newValue);
                    TaskService.updateTaskDueDate(id, newDueDate);
                    break;
                case "status":
                    oldValue = task.getStatus().toString();
                    Status newStatus = Status.valueOf(newValue);
                    TaskService.setTaskStatus(id, newStatus);
                    break;
                default:
                    System.out.println("Cannot update task with ID=" + id);
                    System.out.println("Error: Invalid field name.");
                    return;
            }

            System.out.println("Successfully updated the task.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + new Date());
        } catch (NumberFormatException e) {
            System.out.println("Cannot update task.");
            System.out.println("Error: Invalid ID format. Please enter a number.");
        } catch (ParseException e) {
            System.out.println("Cannot update task.");
            System.out.println("Error: Invalid date format. Please use yyyy-mm-dd.");
        } catch (IllegalArgumentException e) {
            System.out.println("Cannot update task.");
            System.out.println("Error: Invalid status value. Use NotStarted, InProgress or Completed.");
        } catch (InvalidEntityException | EntityNotFoundException e) {
            System.out.println("Cannot update task.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleUpdateStep() {
        try {
            System.out.println("ID:");
            int stepId = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Field (title/status):");
            String field = scanner.nextLine().trim().toLowerCase();

            System.out.println("New Value:");
            String newValue = scanner.nextLine().trim();

            Step step = StepService.getStepById(stepId);
            if (step == null) {
                System.out.println("Cannot update step with ID=" + stepId);
                System.out.println("Error: Cannot find entity with id=" + stepId);
                return;
            }

            String oldValue;
            switch (field) {
                case "title":
                    oldValue = step.getTitle();
                    StepService.updateStepTitle(stepId, newValue);
                    break;
                case "status":
                    oldValue = step.getStatus().toString();
                    stepStatus newStatus = stepStatus.valueOf(newValue);
                    StepService.updateStepStatus(stepId, newStatus);
                    break;
                default:
                    System.out.println("Cannot update step with ID=" + stepId);
                    System.out.println("Error: Invalid field name.");
                    return;
            }

            System.out.println("Successfully updated the step.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + new Date());
        } catch (NumberFormatException e) {
            System.out.println("Cannot update step.");
            System.out.println("Error: Invalid ID format. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Cannot update step.");
            System.out.println("Error: Invalid status value. Use NotStarted, InProgress or Completed.");
        } catch (InvalidEntityException | EntityNotFoundException e) {
            System.out.println("Cannot update step.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleGetTaskById() {
        try {
            System.out.println("ID:");
            int taskId = Integer.parseInt(scanner.nextLine().trim());

            Task task = TaskService.getTaskById(taskId);
            if (task == null) {
                System.out.println("Cannot find task with ID=" + taskId);
                return;
            }

            System.out.println("ID: " + taskId);
            System.out.println("Title: " + task.getTitle());
            System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
            System.out.println("Status: " + task.getStatus());

            List<Step> steps = StepService.getStepsForTask(taskId);
            if (!steps.isEmpty()) {
                System.out.println("Steps:");
                for (Step step : steps) {
                    System.out.println("    + " + step.getTitle() + ":");
                    System.out.println("        ID: " + step.id);
                    System.out.println("        Status: " + step.getStatus());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format. Please enter a number.");
        }
    }

    private static void handleGetIncompleteTasks() {
        List<Task> tasks = TaskService.getIncompleteTasks();
        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks found.");
            return;
        }

        for (Task task : tasks) {
            printTaskDetails();
            System.out.println();
        }
    }

    private static void printTaskDetails() {
        List<Task> tasks = TaskService.getAllTasks();
        for (Task task : tasks){
        System.out.println("ID: " + task.id);
        System.out.println("Title: " + task.getTitle());
        System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
        System.out.println("Status: " + task.getStatus());

        List<Step> steps = StepService.getStepsForTask(task.id);
        if (!steps.isEmpty()) {
            System.out.println("Steps:");
            for (Step step : steps) {
                System.out.println("    + " + step.getTitle() + ":");
                System.out.println("        ID: " + step.id);
                System.out.println("        Status: " + step.getStatus());
            }
        }
    }
    }
}


