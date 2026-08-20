import java.util.ArrayList;
import java.util.Scanner;

/**
 * The entry point for the Miku chatbot.
 */
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
        ArrayList<String> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);

            if (command.equals("bye")) {
                System.out.println("Bye bye! Hope to see you again soon! ☆");
                System.out.println(separator);
                break;
            } else if (command.equals("list")) {
                System.out.println("Tasks:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(i+1 + ". " + tasks.get(i));
                }
            } else {
                System.out.println("Added: " + command + " ☆");
                tasks.add(command);
            }

            System.out.println(separator);
        }
    }
}
