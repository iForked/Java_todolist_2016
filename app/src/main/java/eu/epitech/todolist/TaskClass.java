package eu.epitech.todolist;

/**
 * Created by theo on 1/24/17.
 */

public class TaskClass {

    private final String task;
    private final String description;
    private final String date;

    public TaskClass(String task, String description, String date){
        this.task = task;
        this.description = description;
        this.date = date;
    }

    public String getTask(){
        return task;
    }

    public String getDescription(){
        return description;
    }

    public String getDate() { return date; }
}
