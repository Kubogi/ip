# Miku project behavior

Miku is a command-line Java 25 task tracker with a cheerful, idol-like personality.

Miku emits user-facing text as UTF-8. When launching from an IDE or shell, configure stdout and stderr as UTF-8 if the terminal does not detect it automatically (for example, `-Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8`).

## Commands

* todo <description> creates a task without a date or time.
* deadline <description> /by <date or time> creates a task with a due date or time.
* event <description> /from <start> /to <end> creates a task with a start and end date or time.
* list displays all tasks.
* mark <number> and unmark <number> update completion status.
* delete <number> removes the selected task from the list.
* bye exits the application.

Dates use `yyyy-MM-dd` (for example, `2019-10-15`) or `d/M/yyyy` (for example,
`2/12/2019`) and may include a 24-hour time as `yyyy-MM-dd HHmm` or
`d/M/yyyy HHmm` (for example, `2019-12-02 1800` or `2/12/2019 1800`). Dates are stored
as `LocalDateTime`; date-only values use midnight internally and display without
that implied time. Invalid or impossible dates and times are rejected. Miku
displays dates as `MMM dd yyyy` and date-times as `MMM dd yyyy h:mm a`, such as
`Dec 02 2019 6:00 PM`.

## Persistence

Miku automatically saves task-list changes to `data/miku.json` as UTF-8 JSON.
Each task object uses its code field names: `type`, `description`, `isDone`, plus
`datetime` for deadlines or `from` and `to` for events. Date values are saved
in ISO-8601 `LocalDateTime` form. Loading saved tasks at
startup is not implemented yet.

* Empty commands, unknown commands, missing parameters, invalid task numbers, and empty task fields display a Miku-style error message. The application then continues reading input.

## Display conventions

Tasks are displayed with a type and completion marker:

* [T] — Todo
* [D] — Deadline
* [E] — Event
* [★] — completed
* [ ] — incomplete

Miku's user-facing messages should be warm, energetic, and use cheerful symbols such as ★, ☆, ♪, ♫, and ✨ where they improve the presentation without obscuring task information.

## Important files

* src/main/java/Ui.java - command-line messages, task displays, and UTF-8 console configuration.

* src/main/java/Miku.java — command parsing, task storage, and interaction.
* src/main/java/Task.java — abstract task base class and completion state.
* src/main/java/Todo.java — Todo task type.
* src/main/java/Deadline.java — Deadline task type.
* src/main/java/Event.java — Event task type.
* test/ui-test-plan.md — black-box UI test cases.

* src/main/java/DateTimeParser.java — strict date parsing and display formatting.

Update this document when commands, formats, or user-facing behavior change.
