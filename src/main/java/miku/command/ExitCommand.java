package miku.command;

import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.ResponseFormatter;

/** Ends the current Miku session after showing a farewell message. */
public class ExitCommand extends Command {
    /** Displays the farewell message. */
    @Override
    public String execute(TaskList tasks, ResponseFormatter responseFormatter, Storage storage) {
        return responseFormatter.formatGoodbye();
    }

    /** Indicates that this command ends the application session. */
    @Override
    public boolean isExit() {
        return true;
    }
}
