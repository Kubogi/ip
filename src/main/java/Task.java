/**
 * The entry point for the Miku chatbot.
 */

public class Task {
    protected String description;
    protected boolean isDone;

    public Task (String description) {
        this.description = description;
        isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "★" : " ";
    }

    public void markAsDone() {
        isDone = true;
    }

    public void unmarkDone() { isDone = false; }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
