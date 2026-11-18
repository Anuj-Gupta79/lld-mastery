package com.lld.problems.googledocs.code.models;

import java.util.ArrayDeque;
import java.util.Deque;

import com.lld.problems.googledocs.code.operations.Operation;

public class EditHistory {
    private final Deque<Operation> undoStack = new ArrayDeque<>();
    private final Deque<Operation> redoStack = new ArrayDeque<>();

    public void pushUndo(Operation op) {
        this.undoStack.push(op);
    }

    public Operation popUndo() {
        return this.undoStack.pop();
    }

    public void pushRedo(Operation op) {
        this.redoStack.push(op);
    }

    public Operation popRedo() {
        return this.redoStack.pop();
    }

    public boolean canUndo() {
        return !this.undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !this.redoStack.isEmpty();
    }

    public void clearRedo() {
        this.redoStack.clear();
    }
}
