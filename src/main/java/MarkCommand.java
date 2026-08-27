/** Marks one task as complete. */
public class MarkCommand extends TaskNumberCommand {
    /** Creates a command targeting a one-based task number. */
    public MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MikuException {
        Task task = getTask(tasks);
        task.markAsDone();
        saveTasks(storage, tasks);
        ui.showTaskMarked(task);
    }
}
