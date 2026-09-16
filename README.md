# Miku ★

Miku is a Java 25 desktop task tracker with a cheerful JavaFX chat interface.
Type task commands into the message field, then press Enter or select `Send ♪`.

## Running Miku

Use Java 25, then start the GUI from Windows PowerShell:

```powershell
.\gradlew.bat run
```

To build the self-contained distribution JAR:

```powershell
.\gradlew.bat shadowJar
java -jar build/libs/miku.jar
```

On macOS or Linux, use `./gradlew run` and `./gradlew shadowJar` instead. ☆

Gradle development tasks run with Java assertions enabled. When diagnosing a
development build launched directly from the JAR, use
`java -ea -jar build/libs/miku.jar`. The standard `java -jar` command remains
suitable for end users. ♪

## Chat window

Miku shows your submitted commands in compact rows and gives replies the width
needed for task lists. The command word is highlighted, while errors use a
distinct warning style. The input stays ready for the next command. ♪

## Commands

- `todo <description>`
- `deadline <description> /by <date or time>`
- `event <description> /from <start> /to <end>`
- `reschedule <number> /by <new date or time>` for deadlines
- `reschedule <number> /from <new start> /to <new end>` for events
- `list`, `find <keyword>`, `mark <number>`, `unmark <number>`, and `delete <number>`
- `bye` shows a farewell, then closes the window.
