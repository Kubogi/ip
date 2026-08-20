/** A task that must be completed before a specified date or time. */
public class Deadline extends Task {
    private String datetime;

    /** Creates an undone deadline with its completion date or time. */
    public Deadline(String description, String datetime) {
        super("D", description);
        this.datetime = datetime;
    }

    /** Returns the deadline's date or time as entered by the user. */
    public String getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + datetime + ")";
    }
}
