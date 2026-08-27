# UI test plan

Each case is an independent process and must run with Java 25. Expected output
is compared exactly, apart from a final newline difference.

## Test case: Load saved tasks

Aim: Verify that a previously saved task is restored when Miku starts.

### Saved tasks
```json
[
  {"type": "T", "description": "saved task", "isDone": false},
  {"type": "D", "description": "saved deadline", "isDone": false,
   "datetime": "2019-12-02T18:00", "includesTime": true}
]
```

### Inputs
```text
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
Here are the tasks in your list ♫
1.[T][ ] saved task
2.[D][ ] saved deadline (by: Dec 02 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```

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

## Test case: Find tasks by description keyword

Aim: Verify that `find` returns case-insensitive description matches without changing the list.

### Inputs
```text
todo read book
deadline return BOOK /by 6/6/2026
todo buy groceries
find book
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
[D][ ] return BOOK (by: Jun 06 2026)
Now you have 2 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[T][ ] buy groceries
Now you have 3 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Here are the matching tasks Miku found ♪
1.[T][ ] read book
2.[D][ ] return BOOK (by: Jun 06 2026)
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```

## Test case: Add scheduled tasks with dates

Aim: Verify that date-only and date-time values are parsed, stored, and displayed.

### Inputs
```text
deadline return book /by 2/12/2019 1800
event study /from 2019-10-15 /to 2019-10-15 0930
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
[D][ ] return book (by: Dec 02 2019 6:00 PM)
Now you have 1 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Got it! I've added this task for you ✨
[E][ ] study (from: Oct 15 2019 to: Oct 15 2019 9:30 AM)
Now you have 2 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Here are the tasks in your list ♫
1.[D][ ] return book (by: Dec 02 2019 6:00 PM)
2.[E][ ] study (from: Oct 15 2019 to: Oct 15 2019 9:30 AM)
____________________________________________________________
____________________________________________________________
Bye bye! Miku hopes to see you again soon! ✨
____________________________________________________________
```

## Test case: Reject invalid scheduled date

Aim: Verify that an impossible date is rejected without adding a task.

### Inputs
```text
deadline return book /by 2019-02-29
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
 OOPS!!! Please use yyyy-MM-dd or d/M/yyyy, optionally followed by HHmm (24-hour time) ♪
____________________________________________________________
____________________________________________________________
Here are the tasks in your list ♫
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
event project meeting /from 2026-08-06 1400 /to 2026-08-06 1600
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
[E][ ] project meeting (from: Aug 06 2026 2:00 PM to: Aug 06 2026 4:00 PM)
Now you have 2 task(s) in the list! ☆
____________________________________________________________
____________________________________________________________
Noted ♪ I've removed this task for you!
[E][ ] project meeting (from: Aug 06 2026 2:00 PM to: Aug 06 2026 4:00 PM)
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
