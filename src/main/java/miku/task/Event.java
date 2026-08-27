package miku.task;

import java.time.LocalDateTime;

import miku.parser.DateTimeParser;

/** A task that starts and ends at specified dates or times. */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean hasStartTime;
    private boolean hasEndTime;

    /** Creates an undone event with its start and end date or time. */
    public Event(String description, LocalDateTime from, boolean hasStartTime,
            LocalDateTime to, boolean hasEndTime) {
        super("E", description);
        this.from = from;
        this.to = to;
        this.hasStartTime = hasStartTime;
        this.hasEndTime = hasEndTime;
    }

    /** Returns the event's typed start date and time. */
    public LocalDateTime getFrom() {
        return from;
    }

    /** Returns the event's typed end date and time. */
    public LocalDateTime getTo() {
        return to;
    }

    /** Returns whether the event start was entered with an explicit time. */
    public boolean hasStartTime() {
        return hasStartTime;
    }

    /** Returns whether the event end was entered with an explicit time. */
    public boolean hasEndTime() {
        return hasEndTime;
    }

    /** Returns this event in Miku's task-list display format. */
    @Override
    public String toString() {
        return super.toString() + " (from: " + DateTimeParser.format(from, hasStartTime)
                + " to: " + DateTimeParser.format(to, hasEndTime) + ")";
    }
}
