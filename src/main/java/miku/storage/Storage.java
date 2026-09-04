package miku.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import miku.MikuException;
import miku.task.Deadline;
import miku.task.Event;
import miku.task.Task;
import miku.task.Todo;

/** Loads and saves Miku's task list as JSON in a local data file. */
public class Storage {
    private static final Path DEFAULT_SAVE_FILE = Path.of("data", "miku.json");
    private final Path saveFile;

    /** Creates storage that uses Miku's default local data file. */
    public Storage() {
        this(DEFAULT_SAVE_FILE);
    }

    /** Creates storage that reads and writes the supplied save file. */
    public Storage(Path saveFile) {
        this.saveFile = saveFile;
    }

    /** Loads saved tasks, returning an empty list when no save file exists yet. */
    public List<Task> loadTasks() throws IOException, MikuException {
        if (Files.notExists(saveFile)) {
            return List.of();
        }
        List<Map<String, Object>> savedTasks = new JsonReader(Files.readString(saveFile, StandardCharsets.UTF_8))
                .readTaskArray();
        List<Task> tasks = new ArrayList<>();
        for (Map<String, Object> savedTask : savedTasks) {
            tasks.add(toTask(savedTask));
        }
        return tasks;
    }

    /** Replaces the saved task list with the current list. */
    public void saveTasks(List<Task> tasks) throws IOException {
        Files.createDirectories(saveFile.getParent());
        Files.writeString(saveFile, toJson(tasks), StandardCharsets.UTF_8);
    }

    /** Recreates a task from fields stored in one JSON object. */
    private Task toTask(Map<String, Object> fields) throws MikuException {
        String type = getString(fields, "type");
        String description = getString(fields, "description");
        Task task = switch (type) {
            case "T" -> new Todo(description);
            case "D" -> createDeadline(fields, description);
            case "E" -> createEvent(fields, description);
            default -> throw new MikuException("Saved task data contains an unknown task type.");
        };
        if (getBoolean(fields, "isDone")) {
            task.markAsDone();
        }
        return task;
    }

    /** Recreates a deadline and retains whether it displayed a time. */
    private Task createDeadline(Map<String, Object> fields, String description) throws MikuException {
        LocalDateTime dateTime = parseDateTime(getString(fields, "datetime"));
        boolean hasTime = getOptionalBoolean(fields, "includesTime",
                !dateTime.toLocalTime().equals(LocalTime.MIDNIGHT));
        return new Deadline(description, dateTime, hasTime);
    }

    /** Recreates an event and retains whether its endpoints displayed times. */
    private Task createEvent(Map<String, Object> fields, String description) throws MikuException {
        LocalDateTime from = parseDateTime(getString(fields, "from"));
        LocalDateTime to = parseDateTime(getString(fields, "to"));
        boolean hasStartTime = getOptionalBoolean(fields, "fromIncludesTime",
                !from.toLocalTime().equals(LocalTime.MIDNIGHT));
        boolean hasEndTime = getOptionalBoolean(fields, "toIncludesTime",
                !to.toLocalTime().equals(LocalTime.MIDNIGHT));
        return new Event(description, from, hasStartTime, to, hasEndTime);
    }

    /** Parses an ISO-8601 date-time stored in the save file. */
    private LocalDateTime parseDateTime(String value) throws MikuException {
        try {
            return LocalDateTime.parse(value);
        } catch (RuntimeException exception) {
            throw new MikuException("Saved task data contains an invalid date or time.");
        }
    }

    /** Gets a required JSON string field. */
    private String getString(Map<String, Object> fields, String name) throws MikuException {
        Object value = fields.get(name);
        if (!(value instanceof String string)) {
            throw new MikuException("Saved task data is missing a valid " + name + " field.");
        }
        return string;
    }

    /** Gets a required JSON boolean field. */
    private boolean getBoolean(Map<String, Object> fields, String name) throws MikuException {
        return getOptionalBoolean(fields, name, false, true);
    }

    /** Gets an optional JSON boolean field, using a fallback for older save files. */
    private boolean getOptionalBoolean(Map<String, Object> fields, String name, boolean fallback) throws MikuException {
        return getOptionalBoolean(fields, name, fallback, false);
    }

    /** Gets a boolean field and optionally requires it to be present. */
    private boolean getOptionalBoolean(Map<String, Object> fields, String name, boolean fallback, boolean required)
            throws MikuException {
        Object value = fields.get(name);
        if (value == null && !required) {
            return fallback;
        }
        if (!(value instanceof Boolean bool)) {
            throw new MikuException("Saved task data is missing a valid " + name + " field.");
        }
        return bool;
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
        appendStringField(json, "type", task.getType());
        json.append(',');
        appendStringField(json, "description", task.getDescription());
        json.append(", \"isDone\": ").append(task.isDone());
        if (task instanceof Deadline deadline) {
            json.append(',');
            appendStringField(json, "datetime", deadline.getDateTime().toString());
            json.append(", \"includesTime\": ").append(deadline.hasTime());
        } else if (task instanceof Event event) {
            json.append(',');
            appendStringField(json, "from", event.getFrom().toString());
            json.append(',');
            appendStringField(json, "to", event.getTo().toString());
            json.append(", \"fromIncludesTime\": ").append(event.hasStartTime());
            json.append(", \"toIncludesTime\": ").append(event.hasEndTime());
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

    /** Parses the limited JSON value types used by Miku's own save format. */
    private static class JsonReader {
        private final String json;
        private int position;

        /** Creates a reader for one JSON document. */
        JsonReader(String json) {
            this.json = json;
        }

        /** Reads a JSON array whose elements are task objects. */
        List<Map<String, Object>> readTaskArray() throws MikuException {
            List<Map<String, Object>> tasks = new ArrayList<>();
            expect('[');
            skipWhitespace();
            if (consume(']')) {
                ensureEnd();
                return tasks;
            }
            do {
                tasks.add(readObject());
                skipWhitespace();
            } while (consume(','));
            expect(']');
            ensureEnd();
            return tasks;
        }

        /** Reads one JSON object with string keys and string or boolean values. */
        private Map<String, Object> readObject() throws MikuException {
            Map<String, Object> object = new LinkedHashMap<>();
            expect('{');
            skipWhitespace();
            if (consume('}')) {
                return object;
            }
            do {
                String name = readString();
                expect(':');
                object.put(name, readValue());
                skipWhitespace();
            } while (consume(','));
            expect('}');
            return object;
        }

        /** Reads a JSON string or boolean value. */
        private Object readValue() throws MikuException {
            skipWhitespace();
            if (peek('"')) {
                return readString();
            }
            if (consumeLiteral("true")) {
                return true;
            }
            if (consumeLiteral("false")) {
                return false;
            }
            throw invalidJson();
        }

        /** Reads and unescapes a JSON string. */
        private String readString() throws MikuException {
            expect('"');
            StringBuilder value = new StringBuilder();
            while (position < json.length() && json.charAt(position) != '"') {
                char character = json.charAt(position++);
                if (character != '\\') {
                    value.append(character);
                    continue;
                }
                if (position >= json.length()) {
                    throw invalidJson();
                }
                char escaped = json.charAt(position++);
                switch (escaped) {
                    case '"', '\\', '/' -> value.append(escaped);
                    case 'b' -> value.append('\b');
                    case 'f' -> value.append('\f');
                    case 'n' -> value.append('\n');
                    case 'r' -> value.append('\r');
                    case 't' -> value.append('\t');
                    default -> throw invalidJson();
                }
            }
            expect('"');
            return value.toString();
        }

        /** Consumes a required punctuation character after whitespace. */
        private void expect(char expected) throws MikuException {
            skipWhitespace();
            if (position >= json.length() || json.charAt(position++) != expected) {
                throw invalidJson();
            }
        }

        /** Consumes punctuation when it occurs at the current whitespace-trimmed position. */
        private boolean consume(char expected) {
            skipWhitespace();
            if (position < json.length() && json.charAt(position) == expected) {
                position++;
                return true;
            }
            return false;
        }

        /** Returns whether the next non-whitespace character matches the expected one. */
        private boolean peek(char expected) {
            skipWhitespace();
            return position < json.length() && json.charAt(position) == expected;
        }

        /** Consumes a literal when it begins at the current position. */
        private boolean consumeLiteral(String literal) {
            if (json.startsWith(literal, position)) {
                position += literal.length();
                return true;
            }
            return false;
        }

        /** Advances over insignificant JSON whitespace. */
        private void skipWhitespace() {
            while (position < json.length() && Character.isWhitespace(json.charAt(position))) {
                position++;
            }
        }

        /** Rejects trailing non-whitespace content. */
        private void ensureEnd() throws MikuException {
            skipWhitespace();
            if (position != json.length()) {
                throw invalidJson();
            }
        }

        /** Creates the consistent error used for malformed save files. */
        private MikuException invalidJson() {
            return new MikuException("Saved task data is not valid JSON.");
        }
    }
}
