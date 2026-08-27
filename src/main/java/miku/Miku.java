package miku;

import java.io.IOException;
import java.util.Scanner;

import miku.command.Command;
import miku.parser.Parser;
import miku.storage.Storage;
import miku.task.TaskList;
import miku.ui.Ui;

/** Runs Miku's command-line task tracker and coordinates command execution. */
public class Miku {
    /** Starts Miku and continues reading commands until the user says goodbye or input ends. */
    public static void main(String[] args) {
        Ui.configureUtf8Output();
        Ui ui = new Ui();
        Parser parser = new Parser();
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage();
        TaskList tasks = loadTasks(storage, ui);
        while (scanner.hasNextLine()) {
            boolean shouldExit;
            try {
                shouldExit = processCommand(tasks, storage, ui, parser, scanner.nextLine().trim());
            } catch (MikuException exception) {
                ui.showError(exception.getMessage());
                continue;
            }
            if (shouldExit) {
                break;
            }
        }
    }

    /** Loads persisted tasks and starts with an empty list if the save data is unavailable or invalid. */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException | MikuException exception) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Parses and executes one command, returning whether the application should exit. */
    private static boolean processCommand(TaskList tasks, Storage storage, Ui ui, Parser parser, String input)
            throws MikuException {
        ui.showSeparator();
        Command command = parser.parse(input);
        command.execute(tasks, ui, storage);
        ui.showSeparator();
        return command.isExit();
    }
}
