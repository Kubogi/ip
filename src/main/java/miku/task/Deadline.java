package miku.task;

import java.time.LocalDateTime;

import miku.parser.DateTimeParser;

/** A task that must be completed before a specified date or time. */
public class Deadline extends Task {
    private LocalDateTime dateTime;
    private boolean includesTime;

    /** Creates an undone deadline with its completion date or time. */
    public Deadline(String description, LocalDateTime dateTime, boolean includesTime) {
        super("D", description);
        this.dateTime = dateTime;
        this.includesTime = includesTime;
    }

    /** Returns the deadline's typed date and time. */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /** Returns whether the deadline was entered with an explicit time. */
    public boolean includesTime() {
        return includesTime;
    }

    /** Returns this deadline in Miku's task-list display format. */
    @Override
    public String toString() {
        return super.toString() + " (by: " + DateTimeParser.format(dateTime, includesTime) + ")";
    }
}
