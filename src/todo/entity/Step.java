package todo.entity;

import db.Entity;

public class Step extends Entity {
    public static final int STEP_ENTITY_CODE = 2;

    public enum Status {
        NotStarted,
        Completed
    }

    private String title;
    private Status status;
    private int taskRef;

    public Step(String title, Status status, int taskRef) {
        this.title = title;
        this.status = status;
        this.taskRef = taskRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(int taskRef) {
        this.taskRef = taskRef;
    }

    @Override
    public Entity copy() {
        Step copy = new Step(title, status, taskRef);
        copy.id = id;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }
}
