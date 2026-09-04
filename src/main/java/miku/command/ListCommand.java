package miku.command;

import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.ResponseFormatter;

/** Displays the current task list. */
public class ListCommand extends Command {
    /** Displays the current task list without changing it. */
    @Override
    public String execute(TaskList tasks, ResponseFormatter responseFormatter, Storage storage) {
        return responseFormatter.formatTaskList(tasks);
    }
}
