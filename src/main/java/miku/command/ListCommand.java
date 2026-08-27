package miku.command;

import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.Ui;

/** Displays the current task list. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
