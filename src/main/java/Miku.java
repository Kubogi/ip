import java.io.IOException;
import java.util.Scanner;

/** Runs Miku's command-line task tracker and coordinates command handling. */
public class Miku {
    /** Starts Miku and continues reading commands until the user says goodbye or input ends. */
    public static void main(String[] args) {
        Ui.configureUtf8Output();
        Ui ui = new Ui();
        Parser parser = new Parser();
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage();
        TaskList tasks = loadTasks(storage, ui);
        while (scanner.hasNextLine()) {
            boolean shouldExit;
            try {
                shouldExit = processCommand(tasks, storage, ui, parser, scanner.nextLine().trim());
            } catch (MikuException exception) {
                ui.showError(exception.getMessage());
                continue;
            }
            if (shouldExit) {
                break;
            }
        }
    }

    /** Loads persisted tasks and starts with an empty list if the save data is unavailable or invalid. */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException | MikuException exception) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Dispatches one parsed command and returns whether the application should exit. */
    private static boolean processCommand(TaskList tasks, Storage storage, Ui ui, Parser parser, String command)
            throws MikuException {
        ui.showSeparator();
        Parser.ParsedCommand parsedCommand = parser.parse(command);
        return switch (parsedCommand.type()) {
        case BYE -> handleBye(ui, parsedCommand.arguments());
        case LIST -> handleList(tasks, ui, parsedCommand.arguments());
        case MARK -> handleMark(tasks, storage, ui, parsedCommand.arguments());
        case UNMARK -> handleUnmark(tasks, storage, ui, parsedCommand.arguments());
        case DELETE -> handleDelete(tasks, storage, ui, parsedCommand.arguments());
        case ADD_TASK -> handleAddTask(tasks, storage, ui, parsedCommand.task());
        };
    }

    /** Adds a parser-created task, saves it, and displays its confirmation. */
    private static boolean handleAddTask(TaskList tasks, Storage storage, Ui ui, Task task) throws MikuException {
        tasks.add(task);
        saveTasks(storage, tasks);
        ui.showTaskAdded(task, tasks.size());
        ui.showSeparator();
        return false;
    }

    /** Processes the list command. */
    private static boolean handleList(TaskList tasks, Ui ui, String[] arguments) throws MikuException {
        requireNoExtraArguments(arguments, "list does not need any parameters!!");
        ui.showTaskList(tasks);
        ui.showSeparator();
        return false;
    }

    /** Processes the mark command. */
    private static boolean handleMark(TaskList tasks, Storage storage, Ui ui, String[] arguments)
            throws MikuException {
        Task task = getTaskFromArguments(tasks, arguments, "mark");
        task.markAsDone();
        saveTasks(storage, tasks);
        ui.showTaskMarked(task);
        ui.showSeparator();
        return false;
    }

    /** Processes the unmark command. */
    private static boolean handleUnmark(TaskList tasks, Storage storage, Ui ui, String[] arguments)
            throws MikuException {
        Task task = getTaskFromArguments(tasks, arguments, "unmark");
        task.markAsNotDone();
        saveTasks(storage, tasks);
        ui.showTaskUnmarked(task);
        ui.showSeparator();
        return false;
    }

    /** Processes the delete command. */
    private static boolean handleDelete(TaskList tasks, Storage storage, Ui ui, String[] arguments)
            throws MikuException {
        int taskIndex = getTaskIndexFromArguments(tasks, arguments, "delete");
        Task removedTask = tasks.remove(taskIndex);
        saveTasks(storage, tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
        ui.showSeparator();
        return false;
    }

    /** Processes the bye command. */
    private static boolean handleBye(Ui ui, String[] arguments) throws MikuException {
        requireNoExtraArguments(arguments, "bye does not need any parameters!!");
        ui.showGoodbye();
        ui.showSeparator();
        return true;
    }

    /** Returns the task selected by a mark, unmark, or delete command. */
    private static Task getTaskFromArguments(TaskList tasks, String[] arguments, String command) throws MikuException {
        return tasks.get(getTaskIndexFromArguments(tasks, arguments, command));
    }

    /** Validates a task-number argument and returns its zero-based list index. */
    private static int getTaskIndexFromArguments(TaskList tasks, String[] arguments, String command)
            throws MikuException {
        if (arguments.length < 2) {
            throw new MikuException("Please provide a task number for " + command + " \u266a");
        }
        if (arguments.length > 2) {
            throw new MikuException("Only one task number is needed for " + command + " \u266b");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments[1]);
        } catch (NumberFormatException exception) {
            throw new MikuException("The task number must be a whole number!! \u266b");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new MikuException("That task number is not in Miku's list!! \u2728");
        }
        return taskNumber - 1;
    }

    /** Rejects parameters for commands that do not accept them. */
    private static void requireNoExtraArguments(String[] arguments, String message) throws MikuException {
        if (arguments.length > 1) {
            throw new MikuException(message);
        }
    }

    /** Saves task changes and turns an unexpected write failure into a command error. */
    private static void saveTasks(Storage storage, TaskList tasks) throws MikuException {
        try {
            storage.saveTasks(tasks.asList());
        } catch (IOException exception) {
            throw new MikuException("Miku could not save your tasks right now. Please try again.");
        }
    }
}
