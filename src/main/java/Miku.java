import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/** Runs Miku's command-line task tracker and coordinates command handling. */
public class Miku {
    private static final String SEPARATOR = "____________________________________________________________";

    /** Starts Miku and continues reading commands until the user says goodbye or input ends. */
    public static void main(String[] args) {
        configureUtf8Output();
        printWelcomeMessage();
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String trimmedCommand = scanner.nextLine().trim();
            boolean shouldExit;
            try {
                shouldExit = processCommand(tasks, trimmedCommand);
            } catch (MikuException exception) {
                printError(exception.getMessage());
                continue;
            }
            if (shouldExit) {
                break;
            }
        }
    }

    /** Ensures Miku symbols are emitted as UTF-8 on Windows Cp1252 consoles. */
    private static void configureUtf8Output() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8));
    }

    /** Dispatches one normalized command and returns whether the application should exit. */
    private static boolean processCommand(ArrayList<Task> tasks, String command) throws MikuException {
        System.out.println(SEPARATOR);
        if (command.isEmpty()) {
            throw new MikuException("The command cannot be empty. Please tell Miku what to do ♪");
        }
        String[] cmdArgs = command.split("\\s+");
        return switch (cmdArgs[0]) {
        case "bye" -> handleBye(cmdArgs);
        case "list" -> handleList(tasks, cmdArgs);
        case "mark" -> handleMark(tasks, cmdArgs);
        case "unmark" -> handleUnmark(tasks, cmdArgs);
        case "delete" -> handleDelete(tasks, cmdArgs);
        case "todo" -> handleTodo(tasks, command);
        case "deadline" -> handleDeadline(tasks, command);
        case "event" -> handleEvent(tasks, command);
        default -> throw new MikuException("I'm sorry, but Miku doesn't know what that means :-(");
        };
    }

    /** Processes a todo command. */
    private static boolean handleTodo(ArrayList<Task> tasks, String command) throws MikuException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a todo cannot be empty!! ♪");
        }
        addTask(tasks, new Todo(description));
        finishCommand();
        return false;
    }

    /** Processes a deadline command and validates its description and due date. */
    private static boolean handleDeadline(ArrayList<Task> tasks, String command) throws MikuException {
        int separatorIndex = command.indexOf(" /by ");
        if (separatorIndex < 0) {
            throw new MikuException("A deadline needs a description and a due date using /by !! ♫");
        }
        String description = command.substring("deadline".length(), separatorIndex).trim();
        String deadline = command.substring(separatorIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a deadline cannot be empty!! ♪");
        }
        if (deadline.isEmpty()) {
            throw new MikuException("The due date of a deadline cannot be empty!! ♫");
        }
        addTask(tasks, new Deadline(description, deadline));
        finishCommand();
        return false;
    }

    /** Processes an event command and validates all of its fields. */
    private static boolean handleEvent(ArrayList<Task> tasks, String command) throws MikuException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = fromIndex < 0 ? -1 : command.indexOf(" /to ", fromIndex + " /from ".length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new MikuException("An event needs a description, a start using /from, and an end using /to !! ✨");
        }
        String description = command.substring("event".length(), fromIndex).trim();
        String from = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of an event cannot be empty!! ♪");
        }
        if (from.isEmpty()) {
            throw new MikuException("The start of an event cannot be empty!! ♫");
        }
        if (to.isEmpty()) {
            throw new MikuException("The end of an event cannot be empty!! ✨");
        }
        addTask(tasks, new Event(description, from, to));
        finishCommand();
        return false;
    }

    /** Processes the list command. */
    private static boolean handleList(ArrayList<Task> tasks, String[] cmdArgs) throws MikuException {
        requireNoExtraArguments(cmdArgs, "list does not need any parameters!!");
        System.out.println("Here are the tasks in your list ♫");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + 1 + "." + tasks.get(i));
        }
        finishCommand();
        return false;
    }

    /** Processes the mark command. */
    private static boolean handleMark(ArrayList<Task> tasks, String[] cmdArgs) throws MikuException {
        Task task = getTaskFromArguments(tasks, cmdArgs, "mark");
        task.markAsDone();
        System.out.println("Okay ★ I've marked this task as done!");
        System.out.println(task);
        finishCommand();
        return false;
    }

    /** Processes the unmark command. */
    private static boolean handleUnmark(ArrayList<Task> tasks, String[] cmdArgs) throws MikuException {
        Task task = getTaskFromArguments(tasks, cmdArgs, "unmark");
        task.markAsNotDone();
        System.out.println("Oops... I've marked this task as not done yet ♪");
        System.out.println(task);
        finishCommand();
        return false;
    }

    /** Processes the delete command. */
    private static boolean handleDelete(ArrayList<Task> tasks, String[] cmdArgs) throws MikuException {
        int taskIndex = getTaskIndexFromArguments(tasks, cmdArgs, "delete");
        Task removedTask = tasks.remove(taskIndex);
        System.out.println("Noted ♪ I've removed this task for you!");
        System.out.println(removedTask);
        System.out.println("Now you have " + tasks.size() + " task(s) in the list! ☆");
        finishCommand();
        return false;
    }

    /** Processes the bye command. */
    private static boolean handleBye(String[] cmdArgs) throws MikuException {
        requireNoExtraArguments(cmdArgs, "bye does not need any parameters!!");
        System.out.println("Bye bye! Miku hopes to see you again soon! ✨");
        finishCommand();
        return true;
    }

    /** Returns the task selected by a mark, unmark, or delete command. */
    private static Task getTaskFromArguments(ArrayList<Task> tasks, String[] cmdArgs, String command) throws MikuException {
        return tasks.get(getTaskIndexFromArguments(tasks, cmdArgs, command));
    }

    /** Validates a task-number argument and returns its zero-based list index. */
    private static int getTaskIndexFromArguments(ArrayList<Task> tasks, String[] cmdArgs, String command)
            throws MikuException {
        if (cmdArgs.length < 2) {
            throw new MikuException("Please provide a task number for " + command + " ♪");
        }
        if (cmdArgs.length > 2) {
            throw new MikuException("Only one task number is needed for " + command + " ♫");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(cmdArgs[1]);
        } catch (NumberFormatException exception) {
            throw new MikuException("The task number must be a whole number!! ♫");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new MikuException("That task number is not in Miku's list!! ✨");
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
    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println("Got it! I've added this task for you ✨");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " task(s) in the list! ☆");
    }

    /** Prints the separator after a successfully processed command. */
    private static void finishCommand() {
        System.out.println(SEPARATOR);
    }

    /** Prints an input error in Miku's consistent friendly format. */
    private static void printError(String message) {
        System.out.println(" OOPS!!! " + message);
        System.out.println(SEPARATOR);
    }

    /** Prints Miku's welcome banner. */
    private static void printWelcomeMessage() {
        System.out.println(SEPARATOR);
        System.out.println("Miku");
        System.out.println("Hello! I'm Hatsune Miku ♪");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }
}
