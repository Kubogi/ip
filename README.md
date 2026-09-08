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

## Avatar images

Miku runs without images. To display avatars, add these PNG files yourself:

- `src/main/resources/images/DaUser.jpg`
- `src/main/resources/images/DaMiku.jpg`

The next launch loads either image automatically. ♪

## Commands

- `todo <description>`
- `deadline <description> /by <date or time>`
- `event <description> /from <start> /to <end>`
- `reschedule <number> /by <new date or time>` for deadlines
- `reschedule <number> /from <new start> /to <new end>` for events
- `list`, `find <keyword>`, `mark <number>`, `unmark <number>`, and `delete <number>`
- `bye` shows a farewell, then closes the window.
