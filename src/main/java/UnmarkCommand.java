/** Marks one task as incomplete. */
public class UnmarkCommand extends TaskNumberCommand {
    /** Creates a command targeting a one-based task number. */
    public UnmarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MikuException {
        Task task = getTask(tasks);
        task.markAsNotDone();
        saveTasks(storage, tasks);
        ui.showTaskUnmarked(task);
    }
}
