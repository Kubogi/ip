# UI test plan

Each case is an independent process and must run with Java 25. Expected output
is compared exactly, apart from a final newline difference.

## Test case: Add and list tasks

Aim: Verify that a todo is added and displayed by `list`.

### Inputs
```text
todo read book
list
bye
```

### Expected output
```text
____________________________________________________________
z
Hello! I'm Hatsune Miku ♪
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[T][ ] read book
Now you have 1 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Here are the tasks in your list ♫
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```

## Test case: Mark a task as done

Aim: Verify that `mark <number>` changes the selected task to done.

### Inputs
```text
todo read book
todo return book
mark 2
list
bye
```

### Expected output
```text
____________________________________________________________
z
Hello! I'm Hatsune Miku ♪
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[T][ ] read book
Now you have 1 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[T][ ] return book
Now you have 2 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Okay ★ I've marked this task as done!
[T][★] return book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list ♫
1.[T][ ] read book
2.[T][★] return book
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```

## Test case: Unmark a task

Aim: Verify that `unmark <number>` changes a completed task back to not done.

### Inputs
```text
todo read book
mark 1
unmark 1
list
bye
```

### Expected output
```text
____________________________________________________________
z
Hello! I'm Hatsune Miku ♪
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[T][ ] read book
Now you have 1 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Okay ★ I've marked this task as done!
[T][★] read book
____________________________________________________________
____________________________________________________________
Oops... I've marked this task as not done yet ♪
[T][ ] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list ♫
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```

## Test case: Delete a task

Aim: Verify that `delete <number>` removes the selected task and reports the updated count.

### Inputs
```text
todo read book
event project meeting /from Aug 6th 2pm /to 4pm
delete 2
list
bye
```

### Expected output
```text
____________________________________________________________
z
Hello! I'm Hatsune Miku ♪
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[T][ ] read book
Now you have 1 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 2 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Noted ♪ I've removed this task for you!
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 1 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Here are the tasks in your list ♫
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```
