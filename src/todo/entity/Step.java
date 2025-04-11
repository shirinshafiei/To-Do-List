package todo.entity;

import db.Entity;

public class Step extends Entity {
    public static final int STEP_ENTITY_CODE = 2;

    public enum stepStatus {
        Notstarted,
        Completed
    }

    private String title;
    private stepStatus status;
    private int taskRef;

    public Step(String title, stepStatus status, int taskRef) {
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

    public stepStatus getStatus() {
        return status;
    }

    public void setStatus(stepStatus status) {
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
