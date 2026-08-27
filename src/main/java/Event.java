import java.time.LocalDateTime;

/** A task that starts and ends at specified dates or times. */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private boolean fromIncludesTime;
    private boolean toIncludesTime;

    /** Creates an undone event with its start and end date or time. */
    public Event(String description, LocalDateTime from, boolean fromIncludesTime,
                 LocalDateTime to, boolean toIncludesTime) {
        super("E", description);
        this.from = from;
        this.to = to;
        this.fromIncludesTime = fromIncludesTime;
        this.toIncludesTime = toIncludesTime;
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
    public boolean fromIncludesTime() {
        return fromIncludesTime;
    }

    /** Returns whether the event end was entered with an explicit time. */
    public boolean toIncludesTime() {
        return toIncludesTime;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + DateTimeParser.format(from, fromIncludesTime)
                + " to: " + DateTimeParser.format(to, toIncludesTime) + ")";
    }
}
