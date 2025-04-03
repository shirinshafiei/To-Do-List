package todo.entity;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Task extends Entity implements Trackable {
    public static final int Task_ENTITY_CODE = 1;

    public  enum Status {
        NotStarted,
        InProgress,
        Completed
    }

    private String title;
    private String descreption;
    private Date dueDate;
    private Status status;
    private Date creationDate;
    private Date lastModificationDate;

    public Task(String title, String descreption, Date dueDate, Status status) {
        this.title = title;
        this.descreption = descreption;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Entity copy() {
        Task copy = new Task(title, descreption, dueDate, status);
        copy.id = id;
        copy.setCreationDate(getCreationDate());
        copy.setLastModificationDate(getLastModificationDate());
        return copy;
    }

    @Override
    public int getEntityCode() {
        return Task_ENTITY_CODE;
    }

    @Override
    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date date) {
        this.lastModificationDate = date;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }
}
