/** Represents the common information and behaviour shared by all tasks. */
public abstract class Task {
    protected String type;
    protected String description;
    protected boolean isDone;

    /** Creates an undone task with the given type and description. */
    protected Task(String type, String description) {
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

    @Override
    public String toString() {
        return "[" + type + "][" + getStatusIcon() + "] " + description;
    }
}
