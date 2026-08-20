/** A task that starts and ends at specified dates or times. */
public class Event extends Task {
    private String from;
    private String to;

    /** Creates an undone event with its start and end date or time. */
    public Event(String description, String from, String to) {
        super("E", description);
        this.from = from;
        this.to = to;
    }

    /** Returns the event's start date or time. */
    public String getFrom() {
        return from;
    }

    /** Returns the event's end date or time. */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
