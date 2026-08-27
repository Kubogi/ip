/** Converts raw user input into Miku commands and validated task objects. */
public class Parser {
    /** Identifies the action represented by a parsed command. */
    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, ADD_TASK
    }

    /** Holds a command type, its whitespace-separated arguments, and an optional task to add. */
    public record ParsedCommand(CommandType type, String[] arguments, Task task) { }

    /** Parses one normalized user command. */
    public ParsedCommand parse(String command) throws MikuException {
        if (command.isEmpty()) {
            throw new MikuException("The command cannot be empty. Please tell Miku what to do \u266a");
        }
        String[] arguments = command.split("\\s+");
        return switch (arguments[0]) {
        case "bye" -> new ParsedCommand(CommandType.BYE, arguments, null);
        case "list" -> new ParsedCommand(CommandType.LIST, arguments, null);
        case "mark" -> new ParsedCommand(CommandType.MARK, arguments, null);
        case "unmark" -> new ParsedCommand(CommandType.UNMARK, arguments, null);
        case "delete" -> new ParsedCommand(CommandType.DELETE, arguments, null);
        case "todo" -> new ParsedCommand(CommandType.ADD_TASK, arguments, parseTodo(command));
        case "deadline" -> new ParsedCommand(CommandType.ADD_TASK, arguments, parseDeadline(command));
        case "event" -> new ParsedCommand(CommandType.ADD_TASK, arguments, parseEvent(command));
        default -> throw new MikuException("I'm sorry, but Miku doesn't know what that means :-(");
        };
    }

    /** Validates a todo command and creates its task. */
    private Task parseTodo(String command) throws MikuException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new MikuException("The description of a todo cannot be empty!! \u266a");
        }
        return new Todo(description);
    }

    /** Validates a deadline command and creates its task. */
    private Task parseDeadline(String command) throws MikuException {
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
        return new Deadline(description, parsedDeadline.value(), parsedDeadline.includesTime());
    }

    /** Validates an event command and creates its task. */
    private Task parseEvent(String command) throws MikuException {
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
        return new Event(description, parsedFrom.value(), parsedFrom.includesTime(),
                parsedTo.value(), parsedTo.includesTime());
    }
}
