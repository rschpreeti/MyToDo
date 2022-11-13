package com.todo.AdvanceToDo.Entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Task {
//    @DateTimeFormat(pattern ="DD-MM-YYYY")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskId;
    private String task;
    private String severity;
    private Date startDate;
    private Date dueDate;
    private String status;
    private int userId;

    public Task() {
    }

    public Task(String task, String severity, Date startDate, Date dueDate, String status, int userId) {
        this.task = task;
        this.severity = severity;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        userId = userId;
    }
}
