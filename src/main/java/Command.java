import java.io.IOException;

/** Represents an executable user request in Miku's command-line task tracker. */
public abstract class Command {
    /** Carries out this command using Miku's application collaborators. */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws MikuException;

    /** Returns whether executing this command should end the application. */
    public boolean isExit() {
        return false;
    }

    /** Saves task changes and turns an unexpected write failure into a command error. */
    protected void saveTasks(Storage storage, TaskList tasks) throws MikuException {
        try {
            storage.saveTasks(tasks.asList());
        } catch (IOException exception) {
            throw new MikuException("Miku could not save your tasks right now. Please try again.");
        }
    }
}
