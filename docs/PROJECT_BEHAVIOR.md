# Miku project behavior

Miku is a Java 25 JavaFX task tracker with a cheerful, idol-like chat interface.

## Launching and distribution

Run `./gradlew run` on macOS/Linux or `gradlew.bat run` on Windows with Java 25
to open the GUI. Run the corresponding `shadowJar` task to create
`build/libs/miku.jar`, then launch it with `java -jar build/libs/miku.jar`. The
Shadow plugin includes Miku's runtime dependencies in this file. ★

## GUI behavior

Users submit commands with Enter or `Send ♪`. Miku places the user message on the
right and its response on the left, automatically scrolling to the newest
dialog. Empty commands display only Miku's existing validation error. `bye`
shows Miku's farewell, disables input, and closes the window after one second.

The interface remains usable without avatar files. Place optional PNG avatars at
`src/main/resources/images/DaUser.png` and
`src/main/resources/images/DaMiku.png`; they are loaded on the next launch.

## Commands

- `todo <description>` creates a task without a date or time.
- `deadline <description> /by <date or time>` creates a task with a due date or time.
- `event <description> /from <start> /to <end>` creates a task with a start and end date or time.
- `list` displays all tasks.
- `find <keyword>` displays tasks whose descriptions contain the keyword, ignoring letter case.
- `mark <number>` and `unmark <number>` update completion status.
- `delete <number>` removes the selected task from the list.
- `bye` closes the application after its farewell is displayed.

Dates use `yyyy-MM-dd` (for example, `2019-10-15`) or `d/M/yyyy` (for example,
`2/12/2019`) and may include a 24-hour time as `yyyy-MM-dd HHmm` or
`d/M/yyyy HHmm`. Date-only values display as `MMM dd yyyy`; date-times display
as `MMM dd yyyy h:mm a`.

## Persistence and messages

Miku automatically saves task-list changes to `data/miku.json` as UTF-8 JSON and
loads it at startup. An unreadable or invalid save file produces a friendly chat
warning and starts with an empty task list. Invalid commands and task fields show
a Miku-style chat error while leaving the app ready for the next command.

Tasks display type and completion markers: `[T]`, `[D]`, `[E]`, `[★]`, and `[ ]`.
User-facing messages remain warm and energetic, using symbols such as ★, ☆, ♪,
♫, and ✨ where they improve clarity.

## Verification

Run `./gradlew test checkstyleMain checkstyleTest` on macOS/Linux or
`gradlew.bat test checkstyleMain checkstyleTest` on Windows with Java 25. The
JUnit suite covers core task, parsing, date-time, and GUI-facing response logic.

## Important files

- `src/main/java/miku/Miku.java` — command coordination and response API.
- `src/main/java/miku/ui/MainWindow.java` — GUI controller.
- `src/main/resources/view/` — FXML chat layouts.
- `src/main/resources/css/` — responsive Miku styling.
- `src/main/resources/images/` — optional user-provided avatars.
