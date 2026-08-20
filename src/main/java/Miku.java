import java.util.ArrayList;
import java.util.Scanner;

public class Miku {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = " __  __ _ _           \n"
                + "|  \\/  (_) | ___     \n"
                + "| |\\/| | | |/ / | |\n"
                + "| |  | | |   <| |_| |\n"
                + "|_|  |_|_|_|\\_\\\\__,_|\n";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Hatsune Miku ★");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            String[] cmdArgs = command.trim().split("\\s+");
            System.out.println(separator);

            if (cmdArgs[0].equals("bye")) {
                System.out.println("Bye bye! Hope to see you again soon! ☆");
                System.out.println(separator);
                break;
            } else if (cmdArgs[0].equals("list")) {
                System.out.println("Tasks:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(i+1 + ". " + tasks.get(i));
                }
            } else if (cmdArgs[0].equals("mark")) {
                // assuming it's a in-bounds integer
                Task task = tasks.get(Integer.parseInt(cmdArgs[1]) - 1);
                task.markAsDone();
                System.out.println("Okay ☆ I've marked this task as done!");
                System.out.println(task);
            } else if (cmdArgs[0].equals("unmark")) {
                Task task = tasks.get(Integer.parseInt(cmdArgs[1]) - 1);
                task.unmarkDone();
                System.out.println("Oops... I've marked this task as not done yet:");
                System.out.println(task);
            } else {
                System.out.println("Added: " + command + " ☆");
                tasks.add(new Task(command));
            }

            System.out.println(separator);
        }
    }
}
