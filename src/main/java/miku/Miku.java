package miku;

import java.io.IOException;
import java.util.Optional;

import miku.command.Command;
import miku.parser.Parser;
import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.Ui;

/** Coordinates Miku's command-line task tracker. */
public class Miku {
    private final Ui ui;
    private final Parser parser;
    private final Storage storage;
    private TaskList tasks;

    /** Creates Miku's application collaborators. */
    public Miku() {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage();
    }

    /** Starts Miku and continues handling commands until goodbye or end-of-input. */
    public void run() {
        ui.showWelcome();
        tasks = loadTasks();
        boolean isExit = false;
        while (!isExit) {
            Optional<String> input = ui.readCommand();
            if (input.isEmpty()) {
                break;
            }
            try {
                ui.showSeparator();
                Command command = parser.parse(input.get());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (MikuException exception) {
                ui.showError(exception.getMessage());
            } finally {
                ui.showSeparator();
            }
        }
    }

    /** Loads persisted tasks and starts with an empty list if the save data is unavailable or invalid. */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException | MikuException exception) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Configures output encoding and starts a Miku session. */
    public static void main(String[] args) {
        Ui.configureUtf8Output();
        new Miku().run();
    }
}
