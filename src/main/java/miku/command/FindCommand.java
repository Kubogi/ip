package miku.command;

import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.Ui;

/** Displays tasks whose descriptions contain a supplied keyword. */
public class FindCommand extends Command {
    private final String keyword;

    /** Creates a command that searches task descriptions for the given keyword. */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /** Displays the matching tasks without changing or saving the task list. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.findByDescription(keyword));
    }
}
