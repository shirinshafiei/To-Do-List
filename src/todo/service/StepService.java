package todo.service;

import db.Database;
import db.Entity;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Step.stepStatus;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class StepService {
    public static int addStep(int taskRef, String title) throws InvalidEntityException {
        Step step = new Step(title, Step.stepStatus.Notstarted, taskRef);
        Database.add(step);
        return step.id;
    }

    public static void deleteStep(int stepId) throws InvalidEntityException {
        Database.delete(stepId);
    }

    public static void deleteAllStepsForTask(int taskId) {
        List<Step> stepsToDelete = getStepsForTask(taskId);
        for (Step step : stepsToDelete) {
            Database.delete(step.id);
        }
    }

    public static void updateStepTitle(int stepId, String newTitle) throws InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.setTitle(newTitle);
        Database.update(step);
    }

    public static void updateStepStatus(int stepId, stepStatus newStatus) throws InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.setStatus(newStatus);
        Database.update(step);
        checkAndUpdateTaskStatus(step.getTaskRef());
    }

    public static Step getStepById(int stepId) {
        return (Step) Database.get(stepId);
    }

    public static List<Step> getStepsForTask(int taskId) {
        List<Step> steps = new ArrayList<>();
        for (Entity entity : Database.getAll(Step.STEP_ENTITY_CODE)) {
            Step step = (Step) entity;
            if (step.getTaskRef() == taskId) {
                steps.add(step);
            }
        }
        return steps;
    }

    public static void completeAllStepsForTask(int taskId) throws InvalidEntityException {
        for (Step step : getStepsForTask(taskId)) {
            if (step.getStatus() != stepStatus.Completed) {
                updateStepStatus(step.id, stepStatus.Completed);
            }
        }
    }

    private static void checkAndUpdateTaskStatus(int taskId) throws InvalidEntityException {
        List<Step> steps = getStepsForTask(taskId);
        Task task = (Task) Database.get(taskId);

        boolean allCompleted = !steps.isEmpty();

        for (Step step : steps) {
            if (step.getStatus() != stepStatus.Completed) {
                allCompleted = false;
            }
        }

        if (allCompleted) {
            task.setStatus(Task.Status.Completed);
        } else {
            task.setStatus(Task.Status.InProgress);
        }

        Database.update(task);
    }

}