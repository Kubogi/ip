/** Removes one task from Miku's task list. */
public class DeleteCommand extends TaskNumberCommand {
    /** Creates a command targeting a one-based task number. */
    public DeleteCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MikuException {
        Task removedTask = tasks.remove(getTaskIndex(tasks));
        saveTasks(storage, tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}
