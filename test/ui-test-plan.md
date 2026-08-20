# UI test plan

Each case is an independent process and must run with Java 25. Expected output
is recorded after the application’s final UI format is available.

## Test case: Add and list tasks

Aim: Verify that ordinary input is added as an undone task and displayed by `list`.

### Inputs
```text
read book
list
bye
```

### Expected output
```text
TODO: capture the Java 25 application output after the UI format is finalized.
```

## Test case: Mark a task as done

Aim: Verify that `mark <number>` changes the selected task to done.

### Inputs
```text
read book
return book
mark 2
list
bye
```

### Expected output
```text
TODO: capture the Java 25 application output after task-numbering behavior is finalized.
```

## Test case: Unmark a task

Aim: Verify that `unmark <number>` changes a completed task back to not done.

### Inputs
```text
read book
mark 1
unmark 1
list
bye
```

### Expected output
```text
TODO: capture the Java 25 application output after the UI format is finalized.
```
