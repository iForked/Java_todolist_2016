package eu.epitech.todolist;

/**
 * Created by theo on 1/24/17.
 */

public class TaskClass {

    private final String task;
    private final String description;

    public TaskClass(String task, String description){
        this.task = task;
        this.description = description;
    }

    public String getTask(){
        return task;
    }

    public String getDescription(){
        return description;
    }
}
