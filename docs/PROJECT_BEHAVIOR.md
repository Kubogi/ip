# Miku project behavior

Miku is a command-line Java 25 task tracker with a cheerful, idol-like personality.

## Commands

* todo <description> creates a task without a date or time.
* deadline <description> /by <date or time> creates a task with a due date or time.
* event <description> /from <start> /to <end> creates a task with a start and end date or time.
* list displays all tasks.
* mark <number> and unmark <number> update completion status.
* bye exits the application.

Date and time values are currently retained as user-provided strings. Unrecognized commands are ignored.

## Display conventions

Tasks are displayed with a type and completion marker:

* [T] — Todo
* [D] — Deadline
* [E] — Event
* [★] — completed
* [ ] — incomplete

Miku's user-facing messages should be warm, energetic, and use cheerful symbols such as ★, ☆, ♪, ♫, and ✨ where they improve the presentation without obscuring task information.

## Important files

* src/main/java/Miku.java — command parsing, task storage, and interaction.
* src/main/java/Task.java — abstract task base class and completion state.
* src/main/java/Todo.java — Todo task type.
* src/main/java/Deadline.java — Deadline task type.
* src/main/java/Event.java — Event task type.
* test/ui-test-plan.md — black-box UI test cases.

Update this document when commands, formats, or user-facing behavior change.
