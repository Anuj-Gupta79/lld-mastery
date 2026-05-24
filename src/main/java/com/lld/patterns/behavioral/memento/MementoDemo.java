package com.lld.patterns.behavioral.memento;

import java.util.Stack;

// Snapshot
class EditorMemento {
    // LEARNING: Snapshot should be immutable, mutability create data inconsistency
    // in restoring state.
    private String content;

    public EditorMemento(String content) {
        this.content = content;
    }

    // LEARNING: Package-private getter — only TextEditor (same package) can read content, Caretaker cannot.
    String getContent() {
        return this.content;
    }
}

// Originator
class TextEditor {
    private String content;
    private EditorHistory history;

    public TextEditor(EditorHistory editorHistory) {
        this.content = "";
        this.history = editorHistory;
    }

    public void type(String newString) {
        content += newString;
    }

    public void delete(int chars) {
        content = content.substring(0, content.length() - chars);
    }

    // LEARNING: CreateMemento is something which is different from typing, it store the current content, so that caller controls when to checkpoint, not the editor itself.
    public void createMemento() {
        history.save(new EditorMemento(this.content));
    }

    // LEARNING: while restoring we have to update the this.content so that client
    // always get updated content.
    public void restore() {
        this.content = history.undo().getContent();
    }

    public String getContent() {
        return this.content;
    }
}

// CareTaker: It only care about snapshots never read it.
class EditorHistory {
    // LEARNING: Using stack provide LIFO operation which would be ideal behavior
    // for undo.
    Stack<EditorMemento> mementoStack = new Stack<>();

    public void save(EditorMemento memento) {
        mementoStack.push(memento);
    }

    public EditorMemento undo() {
        return mementoStack.pop();
    }
}

public class MementoDemo {

    public static void main(String[] args) {
        EditorHistory editorHistory = new EditorHistory();
        TextEditor textEditor = new TextEditor(editorHistory);

        textEditor.type("Let's write something.");
        // LEARNING: Save funtionality is explicitly provided to client so that client would decide when to save.
        textEditor.createMemento();
        textEditor.type("Typing in second phase");
        textEditor.createMemento();

        textEditor.delete(10);

        textEditor.restore();
        String restoredContent1 = textEditor.getContent();
        System.out.println("Content after first undo: " + restoredContent1);

        textEditor.restore();
        String restoredContent2 = textEditor.getContent();
        System.out.println("Content after second undo: " + restoredContent2);
    }
}
