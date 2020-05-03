package com.example.inclass15_group7;

import java.io.Serializable;

public class Data implements Serializable {

    String TaskName, Priority, Date, DocID, TaskStatus;

    public Data(String taskName, String priority, String date, String docID, String taskStatus) {
        TaskName = taskName;
        Priority = priority;
        Date = date;
        DocID = docID;
        TaskStatus = taskStatus;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        TaskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Data{" +
                "TaskName='" + TaskName + '\'' +
                ", Priority='" + Priority + '\'' +
                ", Date='" + Date + '\'' +
                ", DocID='" + DocID + '\'' +
                '}';
    }
}
