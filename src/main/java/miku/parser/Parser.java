package miku.parser;

import miku.MikuException;
import miku.command.AddCommand;
import miku.command.Command;
import miku.command.DeleteCommand;
import miku.command.ExitCommand;
import miku.command.FindCommand;
import miku.command.ListCommand;
import miku.command.MarkCommand;
import miku.command.UnmarkCommand;
import miku.task.Deadline;
import miku.task.Event;
import miku.task.Task;
import miku.task.Todo;

/** Converts raw user input into executable Miku commands. */
public class Parser {
    /** Parses one normalized user command. */
    public Command parse(String command) throws MikuException {
        if (command.isEmpty()) {
            throw new MikuException("The command cannot be empty. Please tell Miku what to do \u266a");
        }
        String[] arguments = command.split("\\s+");
        return switch (arguments[0]) {
            case "bye" -> {
                requireNoExtraArguments(arguments, "bye does not need any parameters!!");
                yield new ExitCommand();
            }
            case "list" -> {
                requireNoExtraArguments(arguments, "list does not need any parameters!!");
                yield new ListCommand();
            }
            case "find" -> new FindCommand(parseFindKeyword(command));
            case "mark" -> new MarkCommand(parseTaskNumber(arguments, "mark"));
            case "unmark" -> new UnmarkCommand(parseTaskNumber(arguments, "unmark"));
            case "delete" -> new DeleteCommand(parseTaskNumber(arguments, "delete"));
            case "todo" -> new AddCommand(parseTodo(command));
            case "deadline" -> new AddCommand(parseDeadline(command));
            case "event" -> new AddCommand(parseEvent(command));
            default -> throw new MikuException("I'm sorry, but Miku doesn't know what that means :-(");
        };
    }

    /** Validates and returns the one-based task number supplied to a task command. */
    private int parseTaskNumber(String[] arguments, String command) throws MikuException {
        if (arguments.length < 2) {
            throw new MikuException("Please provide a task number for " + command + " \u266a");
        }
        if (arguments.length > 2) {
            throw new MikuException("Only one task number is needed for " + command + " \u266b");
        }
        try {
            return Integer.parseInt(arguments[1]);
        } catch (NumberFormatException exception) {
            throw new MikuException("The task number must be a whole number!! \u266b");
        }
    }

    /** Rejects parameters for commands that do not accept them. */
    private void requireNoExtraArguments(String[] arguments, String message) throws MikuException {
        if (arguments.length > 1) {
            throw new MikuException(message);
        }
    }

    /** Validates and returns the keyword that Miku should search for in task descriptions. */
    private String parseFindKeyword(String command) throws MikuException {
        assert command.equals("find") || command.startsWith("find ")
                : "Find parsing must receive a find command.";
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new MikuException("Please give Miku a keyword to find \u266a");
        }
        return keyword;
    }

    /** Validates a todo command and creates its task. */
    private Task parseTodo(String command) throws MikuException {
        assert command.equals("todo") || command.startsWith("todo ")
                : "Todo parsing must receive a todo command.";
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a todo cannot be empty!! \u266a");
        }
        return new Todo(description);
    }

    /** Validates a deadline command and creates its task. */
    private Task parseDeadline(String command) throws MikuException {
        assert command.equals("deadline") || command.startsWith("deadline ")
                : "Deadline parsing must receive a deadline command.";
        int separatorIndex = command.indexOf(" /by ");
        if (separatorIndex < 0) {
            throw new MikuException("A deadline needs a description and a due date using /by !! \u266b");
        }
        assert separatorIndex >= "deadline".length()
                : "Deadline separator must follow the command name.";
        String description = command.substring("deadline".length(), separatorIndex).trim();
        String deadline = command.substring(separatorIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a deadline cannot be empty!! \u266a");
        }
        if (deadline.isEmpty()) {
            throw new MikuException("The due date of a deadline cannot be empty!! \u266b");
        }
        DateTimeParser.ParsedDateTime parsedDeadline = DateTimeParser.parse(deadline);
        return new Deadline(description, parsedDeadline.dateTime(), parsedDeadline.hasTime());
    }

    /** Validates an event command and creates its task. */
    private Task parseEvent(String command) throws MikuException {
        assert command.equals("event") || command.startsWith("event ")
                : "Event parsing must receive an event command.";
        int fromIndex = command.indexOf(" /from ");
        int toIndex = fromIndex < 0 ? -1 : command.indexOf(" /to ", fromIndex + " /from ".length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new MikuException(
                    "An event needs a description, a start using /from, and an end using /to !! \u2728");
        }
        assert fromIndex >= "event".length() && toIndex > fromIndex
                : "Event delimiters must follow the command name in order.";
        String description = command.substring("event".length(), fromIndex).trim();
        String start = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String end = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of an event cannot be empty!! \u266a");
        }
        if (start.isEmpty()) {
            throw new MikuException("The start of an event cannot be empty!! \u266b");
        }
        if (end.isEmpty()) {
            throw new MikuException("The end of an event cannot be empty!! \u2728");
        }
        DateTimeParser.ParsedDateTime parsedStart = DateTimeParser.parse(start);
        DateTimeParser.ParsedDateTime parsedEnd = DateTimeParser.parse(end);
        DateTimeParser.validateEventRange(parsedStart.dateTime(), parsedEnd.dateTime());
        return new Event(description, parsedStart.dateTime(), parsedStart.hasTime(),
                parsedEnd.dateTime(), parsedEnd.hasTime());
    }
}
