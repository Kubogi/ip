package miku.task;

/** Represents the common information and behaviour shared by all tasks. */
public abstract class Task {
    protected String type;
    protected String description;
    protected boolean isDone;

    /** Creates an undone task with the given type and description. */
    protected Task(String type, String description) {
        assert "T".equals(type) || "D".equals(type) || "E".equals(type)
                : "Tasks must use a supported type marker.";
        assert description != null : "Tasks must have a description.";
        this.type = type;
        this.description = description;
        isDone = false;
    }

    /** Returns the symbol used to show whether this task is complete. */
    public String getStatusIcon() {
        return isDone ? "★" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /** Keeps compatibility with the original command implementation. */
    public void unmarkDone() {
        markAsNotDone();
    }

    /** Returns this task's description. */
    public String getDescription() {
        return description;
    }

    /** Returns this task's type marker. */
    public String getType() {
        return type;
    }

    /** Returns whether this task has been completed. */
    public boolean isDone() {
        return isDone;
    }

    /** Returns this task in Miku's task-list display format. */
    @Override
    public String toString() {
        return "[" + type + "][" + getStatusIcon() + "] " + description;
    }
}
