package miku.command;

import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.Ui;

/** Ends the current Miku session after showing a farewell message. */
public class ExitCommand extends Command {
    /** Displays the farewell message. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /** Indicates that this command ends the application session. */
    @Override
    public boolean isExit() {
        return true;
    }
}
