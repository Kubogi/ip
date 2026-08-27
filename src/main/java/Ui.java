import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Handles all command-line input/output presentation for Miku. */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";

    /** Configures standard output and error streams to display Miku's symbols correctly. */
    public static void configureUtf8Output() {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8));
    }

    /** Displays Miku's welcome banner at the start of a session. */
    public void showWelcome() {
        showSeparator();
        System.out.println("z");
        System.out.println("Hello! I'm Hatsune Miku \u266a");
        System.out.println("What can I do for you?");
        showSeparator();
    }

    /** Explains that saved tasks could not be restored and Miku is starting fresh. */
    public void showLoadingError() {
        System.out.println(" OOPS!!! Miku could not load saved tasks, so we're starting with an empty list \u266a");
        showSeparator();
    }

    /** Displays the current task list. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list \u266b");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println(index + 1 + "." + tasks.get(index));
        }
    }

    /** Displays the confirmation after a task has been added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it! I've added this task for you \u2728");
        System.out.println(task);
        showTaskCount(taskCount);
    }

    /** Displays the confirmation after a task has been marked complete. */
    public void showTaskMarked(Task task) {
        System.out.println("Okay \u2605 I've marked this task as done!");
        System.out.println(task);
    }

    /** Displays the confirmation after a task has been marked incomplete. */
    public void showTaskUnmarked(Task task) {
        System.out.println("Oops... I've marked this task as not done yet \u266a");
        System.out.println(task);
    }

    /** Displays the confirmation after a task has been removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted \u266a I've removed this task for you!");
        System.out.println(task);
        showTaskCount(taskCount);
    }

    /** Displays Miku's goodbye message. */
    public void showGoodbye() {
        System.out.println("Bye bye! Miku hopes to see you again soon! \u2728");
    }

    /** Displays a friendly error message for an invalid command. */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
        showSeparator();
    }

    /** Displays the standard separator after each command response. */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /** Displays the number of tasks currently tracked. */
    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " task(s) in the list! \u2606");
    }
}
