package com.lld.patterns.behavioral.command;

import java.util.Stack;

// LEARNING: Having interface because an action as an object lets you store, queue, and reverse it.
interface Command {
    void execute();

    void undo();
}

class WriteCommand implements Command {
    private StringBuilder content;
    private TextEditor textEditor;

    // LEARNING: Storing content given to WriteCommand to enable undo functionality.
    public WriteCommand(TextEditor textEditor, StringBuilder content) {
        this.content = content;
        this.textEditor = textEditor;
    }

    @Override
    public void execute() {
        textEditor.write(content);
    }

    @Override
    public void undo() {
        textEditor.delete(content.length());
    }
}

class DeleteCommand implements Command {
    private TextEditor textEditor;
    private String deletedText;
    private int count;

    public DeleteCommand(TextEditor textEditor, int count) {
        this.textEditor = textEditor;
        this.count = count;
    }

    // LEARNING: Storing the deleted text to enable undo functionality.
    @Override
    public void execute() {
        deletedText = textEditor.getText().substring(Math.max(0, textEditor.getText().length() - count));
        textEditor.delete(count);
    }

    @Override
    public void undo() {
        if (deletedText != null) {
            textEditor.write(new StringBuilder(deletedText));
        }
    }
}

// LEARNING: TextEditor won't have any knowledge of commands. It is purely responsible for managing text and providing methods to manipulate it.
class TextEditor {
    private StringBuilder text = new StringBuilder();

    public void write(StringBuilder content) {
        this.text.append(content);
    }

    public void delete(int count) {
        int start = Math.max(0, text.length() - count);
        text.delete(start, text.length());
    }

    public String getText() {
        return text.toString();
    }
}

// LEARNING: Invoker is responsible for executing commands and maintaining command history for undo functionality.
class EditorInvoker {
    // LEARNING: Using Stack to maintain command history for undo functionality as Stack follow LIFO order.
    private Stack<Command> commandHistory = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undo() {
        if (!commandHistory.isEmpty()) {
            Command command = commandHistory.pop();
            command.undo();
        }
    }
}

public class CommandDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        EditorInvoker invoker = new EditorInvoker();

        Command writeCommand1 = new WriteCommand(editor, new StringBuilder("Hello, "));
        Command writeCommand2 = new WriteCommand(editor, new StringBuilder("World!"));
        Command deleteCommand = new DeleteCommand(editor, 6);
        invoker.executeCommand(writeCommand1);
        invoker.executeCommand(writeCommand2);
        System.out.println("Current Text: " + editor.getText());
        invoker.executeCommand(deleteCommand);
        System.out.println("After Deletion: " + editor.getText());
        invoker.undo();
        System.out.println("After Undo: " + editor.getText());
    }
}
