import java.io.IOException;
import java.util.Scanner;

/** Runs Miku's command-line task tracker and coordinates command handling. */
public class Miku {
    /** Starts Miku and continues reading commands until the user says goodbye or input ends. */
    public static void main(String[] args) {
        Ui.configureUtf8Output();
        Ui ui = new Ui();
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        TaskList tasks = new TaskList();
        Storage storage = new Storage();
        while (scanner.hasNextLine()) {
            String trimmedCommand = scanner.nextLine().trim();
            boolean shouldExit;
            try {
                shouldExit = processCommand(tasks, storage, ui, trimmedCommand);
            } catch (MikuException exception) {
                ui.showError(exception.getMessage());
                continue;
            }
            if (shouldExit) {
                break;
            }
        }
    }

    /** Dispatches one normalized command and returns whether the application should exit. */
    private static boolean processCommand(TaskList tasks, Storage storage, Ui ui, String command)
            throws MikuException {
        ui.showSeparator();
        if (command.isEmpty()) {
            throw new MikuException("The command cannot be empty. Please tell Miku what to do \u266a");
        }
        String[] cmdArgs = command.split("\\s+");
        return switch (cmdArgs[0]) {
        case "bye" -> handleBye(ui, cmdArgs);
        case "list" -> handleList(tasks, ui, cmdArgs);
        case "mark" -> handleMark(tasks, storage, ui, cmdArgs);
        case "unmark" -> handleUnmark(tasks, storage, ui, cmdArgs);
        case "delete" -> handleDelete(tasks, storage, ui, cmdArgs);
        case "todo" -> handleTodo(tasks, storage, ui, command);
        case "deadline" -> handleDeadline(tasks, storage, ui, command);
        case "event" -> handleEvent(tasks, storage, ui, command);
        default -> throw new MikuException("I'm sorry, but Miku doesn't know what that means :-(");
        };
    }

    /** Processes a todo command. */
    private static boolean handleTodo(TaskList tasks, Storage storage, Ui ui, String command)
            throws MikuException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a todo cannot be empty!! \u266a");
        }
        addTask(tasks, storage, ui, new Todo(description));
        ui.showSeparator();
        return false;
    }

    /** Processes a deadline command and validates its description and due date. */
    private static boolean handleDeadline(TaskList tasks, Storage storage, Ui ui, String command)
            throws MikuException {
        int separatorIndex = command.indexOf(" /by ");
        if (separatorIndex < 0) {
            throw new MikuException("A deadline needs a description and a due date using /by !! \u266b");
        }
        String description = command.substring("deadline".length(), separatorIndex).trim();
        String deadline = command.substring(separatorIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a deadline cannot be empty!! \u266a");
        }
        if (deadline.isEmpty()) {
            throw new MikuException("The due date of a deadline cannot be empty!! \u266b");
        }
        DateTimeParser.ParsedDateTime parsedDeadline = DateTimeParser.parse(deadline);
        addTask(tasks, storage, ui, new Deadline(description, parsedDeadline.value(), parsedDeadline.includesTime()));
        ui.showSeparator();
        return false;
    }

    /** Processes an event command and validates all of its fields. */
    private static boolean handleEvent(TaskList tasks, Storage storage, Ui ui, String command)
            throws MikuException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = fromIndex < 0 ? -1 : command.indexOf(" /to ", fromIndex + " /from ".length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new MikuException("An event needs a description, a start using /from, and an end using /to !! \u2728");
        }
        String description = command.substring("event".length(), fromIndex).trim();
        String from = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of an event cannot be empty!! \u266a");
        }
        if (from.isEmpty()) {
            throw new MikuException("The start of an event cannot be empty!! \u266b");
        }
        if (to.isEmpty()) {
            throw new MikuException("The end of an event cannot be empty!! \u2728");
        }
        DateTimeParser.ParsedDateTime parsedFrom = DateTimeParser.parse(from);
        DateTimeParser.ParsedDateTime parsedTo = DateTimeParser.parse(to);
        addTask(tasks, storage, ui, new Event(description, parsedFrom.value(), parsedFrom.includesTime(),
                parsedTo.value(), parsedTo.includesTime()));
        ui.showSeparator();
        return false;
    }

    /** Processes the list command. */
    private static boolean handleList(TaskList tasks, Ui ui, String[] cmdArgs) throws MikuException {
        requireNoExtraArguments(cmdArgs, "list does not need any parameters!!");
        ui.showTaskList(tasks);
        ui.showSeparator();
        return false;
    }

    /** Processes the mark command. */
    private static boolean handleMark(TaskList tasks, Storage storage, Ui ui, String[] cmdArgs)
            throws MikuException {
        Task task = getTaskFromArguments(tasks, cmdArgs, "mark");
        task.markAsDone();
        saveTasks(storage, tasks);
        ui.showTaskMarked(task);
        ui.showSeparator();
        return false;
    }

    /** Processes the unmark command. */
    private static boolean handleUnmark(TaskList tasks, Storage storage, Ui ui, String[] cmdArgs)
            throws MikuException {
        Task task = getTaskFromArguments(tasks, cmdArgs, "unmark");
        task.markAsNotDone();
        saveTasks(storage, tasks);
        ui.showTaskUnmarked(task);
        ui.showSeparator();
        return false;
    }

    /** Processes the delete command. */
    private static boolean handleDelete(TaskList tasks, Storage storage, Ui ui, String[] cmdArgs)
            throws MikuException {
        int taskIndex = getTaskIndexFromArguments(tasks, cmdArgs, "delete");
        Task removedTask = tasks.remove(taskIndex);
        saveTasks(storage, tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
        ui.showSeparator();
        return false;
    }

    /** Processes the bye command. */
    private static boolean handleBye(Ui ui, String[] cmdArgs) throws MikuException {
        requireNoExtraArguments(cmdArgs, "bye does not need any parameters!!");
        ui.showGoodbye();
        ui.showSeparator();
        return true;
    }

    /** Returns the task selected by a mark, unmark, or delete command. */
    private static Task getTaskFromArguments(TaskList tasks, String[] cmdArgs, String command) throws MikuException {
        return tasks.get(getTaskIndexFromArguments(tasks, cmdArgs, command));
    }

    /** Validates a task-number argument and returns its zero-based list index. */
    private static int getTaskIndexFromArguments(TaskList tasks, String[] cmdArgs, String command)
            throws MikuException {
        if (cmdArgs.length < 2) {
            throw new MikuException("Please provide a task number for " + command + " \u266a");
        }
        if (cmdArgs.length > 2) {
            throw new MikuException("Only one task number is needed for " + command + " \u266b");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(cmdArgs[1]);
        } catch (NumberFormatException exception) {
            throw new MikuException("The task number must be a whole number!! \u266b");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new MikuException("That task number is not in Miku's list!! \u2728");
        }
        return taskNumber - 1;
    }

    /** Rejects parameters for commands that do not accept them. */
    private static void requireNoExtraArguments(String[] cmdArgs, String message) throws MikuException {
        if (cmdArgs.length > 1) {
            throw new MikuException(message);
        }
    }

    /** Adds a task and prints the confirmation shown after a successful addition. */
    private static void addTask(TaskList tasks, Storage storage, Ui ui, Task task) throws MikuException {
        tasks.add(task);
        saveTasks(storage, tasks);
        ui.showTaskAdded(task, tasks.size());
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
