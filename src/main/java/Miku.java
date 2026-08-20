import java.util.ArrayList;
import java.util.Scanner;

public class Miku {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "Miku";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Hatsune Miku ☆");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            String trimmedCommand = command.trim();
            if (trimmedCommand.isEmpty()) {
                continue;
            }
            String[] cmdArgs = trimmedCommand.split("\\s+");
            System.out.println(separator);

            if (cmdArgs[0].equals("bye")) {
                System.out.println("Bye bye! Miku hopes to see you again soon! ☆");
                System.out.println(separator);
                break;
            } else if (cmdArgs[0].equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(i + 1 + "." + tasks.get(i));
                }
            } else if (cmdArgs[0].equals("mark")) {
                Task task = tasks.get(Integer.parseInt(cmdArgs[1]) - 1);
                task.markAsDone();
                System.out.println("Okay ☆ I've marked this task as done!");
                System.out.println(task);
            } else if (cmdArgs[0].equals("unmark")) {
                Task task = tasks.get(Integer.parseInt(cmdArgs[1]) - 1);
                task.markAsNotDone();
                System.out.println("Oops... I've marked this task as not done yet:");
                System.out.println(task);
            } else if (cmdArgs[0].equals("todo") && cmdArgs.length > 1) {
                addTask(tasks, new Todo(trimmedCommand.substring("todo".length()).trim()));
            } else if (cmdArgs[0].equals("deadline") && trimmedCommand.contains(" /by ")) {
                int separatorIndex = trimmedCommand.indexOf(" /by ");
                String description = trimmedCommand.substring("deadline".length(), separatorIndex).trim();
                String deadline = trimmedCommand.substring(separatorIndex + " /by ".length()).trim();

                if (!description.isEmpty() && !deadline.isEmpty()) {
                    addTask(tasks, new Deadline(description, deadline));
                }
            } else if (cmdArgs[0].equals("event") && trimmedCommand.contains(" /from ") && trimmedCommand.contains(" /to ")) {
                int fromIndex = trimmedCommand.indexOf(" /from ");
                int toIndex = trimmedCommand.indexOf(" /to ", fromIndex + 7);

                String description = trimmedCommand.substring("event".length(), fromIndex).trim();
                String from = trimmedCommand.substring(fromIndex + " /from ".length(), toIndex).trim();
                String to = trimmedCommand.substring(toIndex + " /to ".length()).trim();

                if (!description.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                    addTask(tasks, new Event(description, from, to));
                }
            }

            System.out.println(separator);
        }
    }

    /** Adds a task and prints the confirmation shown after a successful addition. */
    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println("Got it! I've added this task for you ☆");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " task(s) in the list! ☆");
    }
}
