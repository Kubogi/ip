/** Adds a parser-created task to Miku's task list. */
public class AddCommand extends Command {
    private final Task task;

    /** Creates a command that adds the given task. */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws MikuException {
        tasks.add(task);
        saveTasks(storage, tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
