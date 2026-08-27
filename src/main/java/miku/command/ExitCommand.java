package miku.command;

import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.Ui;

/** Ends the current Miku session after showing a farewell message. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
