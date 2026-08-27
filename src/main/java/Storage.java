import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Saves Miku's current task list as JSON in a local data file. */
public class Storage {
    private static final Path SAVE_FILE = Path.of("data", "miku.json");

    /** Replaces the saved task list with the current list. */
    public void saveTasks(List<Task> tasks) throws IOException {
        Files.createDirectories(SAVE_FILE.getParent());
        Files.writeString(SAVE_FILE, toJson(tasks), StandardCharsets.UTF_8);
    }

    /** Converts the task list to a JSON array using the task field names. */
    private String toJson(List<Task> tasks) {
        StringBuilder json = new StringBuilder("[\n");
        for (int index = 0; index < tasks.size(); index++) {
            json.append("  ").append(toJson(tasks.get(index)));
            if (index < tasks.size() - 1) {
                json.append(',');
            }
            json.append('\n');
        }
        return json.append("]\n").toString();
    }

    /** Converts one task to JSON, including only fields that belong to its type. */
    private String toJson(Task task) {
        StringBuilder json = new StringBuilder("{");
        appendStringField(json, "type", task.type);
        json.append(',');
        appendStringField(json, "description", task.description);
        json.append(", \"isDone\": ").append(task.isDone);
        if (task instanceof Deadline deadline) {
            json.append(',');
            appendStringField(json, "datetime", deadline.getDatetime());
        } else if (task instanceof Event event) {
            json.append(',');
            appendStringField(json, "from", event.getFrom());
            json.append(',');
            appendStringField(json, "to", event.getTo());
        }
        return json.append('}').toString();
    }

    /** Appends a JSON string property, escaping special characters in its value. */
    private void appendStringField(StringBuilder json, String name, String value) {
        json.append('"').append(name).append("\": \"").append(escape(value)).append('"');
    }

    /** Escapes the characters that would otherwise make a JSON string invalid. */
    private String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
