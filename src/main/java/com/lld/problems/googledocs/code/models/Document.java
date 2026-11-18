package com.lld.problems.googledocs.code.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.lld.problems.googledocs.code.constants.RoleType;
import com.lld.problems.googledocs.code.constants.TextStyle;
import com.lld.problems.googledocs.code.exceptions.DuplicateUserException;
import com.lld.problems.googledocs.code.exceptions.PermissionDeniedException;
import com.lld.problems.googledocs.code.exceptions.UserNotFoundException;
import com.lld.problems.googledocs.code.operations.DeleteOperation;
import com.lld.problems.googledocs.code.operations.FormatOperation;
import com.lld.problems.googledocs.code.operations.InsertOperation;
import com.lld.problems.googledocs.code.operations.Operation;
import com.lld.problems.googledocs.code.operations.OperationEngine;

public class Document {
    private final String documentId;
    private String documentName;
    private int revision;
    private final String ownerId;
    private StringBuilder content;
    private final List<Operation> appliedOperations;
    private final List<StyleSpan> spans;
    private final Map<String, RoleType> members;
    private final Map<String, Cursor> cursors;
    private final List<Comment> comments;
    private final List<Version> versions;
    private final Map<String, EditHistory> histories;
    private final List<DocumentListener> listeners;

    public Document(String name, String ownerId) {
        this.documentId = UUID.randomUUID().toString();
        this.documentName = name;
        this.ownerId = ownerId;
        this.revision = 0;
        this.content = new StringBuilder();
        this.appliedOperations = new ArrayList<>();
        this.spans = new ArrayList<>();
        this.members = new HashMap<>();
        this.cursors = new HashMap<>();
        this.histories = new HashMap<>();
        this.comments = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.versions = new ArrayList<>();

        this.members.put(ownerId, RoleType.OWNER);
    }

    public synchronized void submit(Operation op, OperationEngine engine) {
        checkCanEdit(op.getUserId());

        Operation fixed = applyOperation(op, engine);

        if (Objects.isNull(fixed)) {
            return;
        }

        EditHistory history = historyOf(op.getUserId());
        history.pushUndo(fixed.inverse().withBase(this.revision));
        history.clearRedo();
    }

    public synchronized void addMember(String userId, RoleType role) {
        if (this.members.containsKey(userId)) {
            throw new DuplicateUserException("User already exist!");
        }

        this.members.put(userId, role);
    }

    public synchronized void removeMember(String userId) {
        if (!this.members.containsKey(userId)) {
            throw new UserNotFoundException("User not found!");
        }

        if (Objects.equals(userId, this.ownerId)) {
            throw new PermissionDeniedException("Owner cannot be removed!");
        }

        this.members.remove(userId);
        this.cursors.remove(userId);
    }

    public synchronized void addComment(Comment comment) {
        String userId = comment.getUserId();

        RoleType type = this.members.get(userId);

        if (Objects.isNull(type)) {
            throw new UserNotFoundException("User not exist!");
        }

        if (!type.canComment()) {
            throw new PermissionDeniedException("User cannot comment!");
        }

        if (comment.getStart() < 0 || comment.getStart() >= comment.getEnd()
                || comment.getEnd() > this.content.length()) {
            throw new IllegalArgumentException("Comment position is not valid!");
        }

        this.comments.add(comment);
    }

    public synchronized void undo(String userId, OperationEngine engine) {
        checkCanEdit(userId);

        EditHistory history = historyOf(userId);

        if (!history.canUndo()) {
            return;
        }

        Operation fixed = applyOperation(history.popUndo(), engine);

        if (!Objects.isNull(fixed)) {
            history.pushRedo(fixed.inverse().withBase(this.revision));
        }
    }

    public synchronized void redo(String userId, OperationEngine engine) {
        checkCanEdit(userId);

        EditHistory history = historyOf(userId);

        if (!history.canRedo()) {
            return;
        }

        Operation fixed = applyOperation(history.popRedo(), engine);

        if (!Objects.isNull(fixed)) {
            history.pushUndo(fixed.inverse().withBase(this.revision));
        }
    }

    public synchronized void restoreVersion(String userId, int number, OperationEngine engine) {
        checkCanEdit(userId);

        if (number < 1 || number > this.versions.size()) {
            throw new IllegalArgumentException("Version not found: " + number);
        }

        Version target = this.versions.get(number - 1);
        String oldContent = target.getContent();
        List<StyleSpan> oldSpans = target.getSpans();

        if (this.content.length() > 0) {
            applyOperation(new DeleteOperation(userId, this.revision, 0, this.content.length()), engine);
        }

        if (!oldContent.isEmpty()) {
            applyOperation(new InsertOperation(userId, this.revision, 0, oldContent), engine);
        }

        for (StyleSpan span : oldSpans) {
            applyOperation(
                    new FormatOperation(userId, this.revision, span.getStart(), span.getEnd(), span.getStyle(), true),
                    engine);
        }
    }

    public synchronized void addListener(DocumentListener listener) {
        if (this.listeners.contains(listener)) {
            throw new DuplicateUserException("[Error Add Listener] Requested listener is already present!");
        }

        this.listeners.add(listener);
    }

    public synchronized void removeListener(DocumentListener listener) {
        if (!this.listeners.contains(listener)) {
            throw new UserNotFoundException("[Error Remove Listener] Requested listener not exist!");
        }

        this.listeners.remove(listener);
    }

    public synchronized void moveCursor(String userId, int position) {
        if (!this.members.containsKey(userId)) {
            throw new UserNotFoundException("User is not a member: " + userId);
        }
        if (position < 0 || position > this.content.length()) {
            throw new IllegalArgumentException("Invalid cursor position: " + position);
        }
        Cursor cursor = this.cursors.get(userId);
        if (cursor == null) {
            this.cursors.put(userId, new Cursor(position, userId));
        } else {
            cursor.setPosition(position);
        }
    }

    public synchronized void removeCursor(String userId) {
        if (!this.cursors.containsKey(userId)) {
            throw new UserNotFoundException("[Error Remove Cursor] Requested user does not exist!");
        }

        this.cursors.remove(userId);
    }

    public void insertText(int position, String text) {
        this.content.insert(position, text);
    }

    public void deleteText(int start, int end) {
        this.content.delete(start, end);
    }

    public String getText(int start, int end) {
        return this.content.substring(start, end);
    }

    public void applyStyle(int start, int end, TextStyle style, boolean on) {
        if (on) {
            this.spans.add(new StyleSpan(start, end, style));
            return;
        }

        List<StyleSpan> result = new ArrayList<>();

        for (StyleSpan span : this.spans) {
            boolean overlaps = Objects.equals(span.getStyle(), style)
                    && (span.getEnd() > start && span.getStart() < end);

            if (!overlaps) {
                result.add(span);
                continue;
            }

            if (span.getStart() < start) {
                result.add(new StyleSpan(span.getStart(), start, style));
            }

            if (span.getEnd() > end) {
                result.add(new StyleSpan(end, span.getEnd(), style));
            }
        }

        this.spans.clear();
        this.spans.addAll(result);
    }

    private Operation applyOperation(Operation op, OperationEngine engine) {
        int base = op.getBaseRevision();

        if (base < 0 || base > this.appliedOperations.size()) {
            throw new IllegalArgumentException("Invalid base revision: " + base);
        }

        List<Operation> pending = this.appliedOperations.subList(op.getBaseRevision(), this.appliedOperations.size());

        Operation fixed = engine.transform(op, pending);
        if (Objects.isNull(fixed)) {
            return null;
        }

        fixed = fixed.capture(this);
        fixed.apply(this);
        this.appliedOperations.add(fixed);
        this.revision++;

        moveAnchors(fixed);
        this.versions.add(new Version(this.revision, this.spans, getContent(), fixed.getUserId()));
        notifyListeners(fixed);

        return fixed;
    }

    private void moveAnchors(Operation op) {
        for (Cursor cursor : this.cursors.values()) {
            cursor.setPosition(op.shift(cursor.getPosition(), true));
        }

        for (Comment comment : this.comments) {
            comment.setStart(op.shift(comment.getStart(), true));
            comment.setEnd(op.shift(comment.getEnd(), false));
            comment.setEnd(Math.max(comment.getEnd(), comment.getStart()));
        }

        for (StyleSpan span : this.spans) {
            span.setStart(op.shift(span.getStart(), true));
            span.setEnd(op.shift(span.getEnd(), false));
        }

        this.spans.removeIf(span -> span.getEnd() <= span.getStart());
    }

    private void checkCanEdit(String userId) {
        if (!this.members.containsKey(userId)) {
            throw new UserNotFoundException(
                    "User is not member!");
        }

        if (!this.members.get(userId).canEdit()) {
            throw new PermissionDeniedException("User cannot edit!");
        }
    }

    private EditHistory historyOf(String userId) {
        return this.histories.computeIfAbsent(userId, id -> new EditHistory());
    }

    private void notifyListeners(Operation op) {
        for (DocumentListener listener : this.listeners) {
            listener.onChange(documentId, op);
        }
    }

    public synchronized String render() {
        StringBuilder out = new StringBuilder();
        String previous = "";
        for (int i = 0; i < this.content.length(); i++) {
            String current = stylesAt(i);
            if (!current.equals(previous)) {
                out.append(current.isEmpty() ? "[/]" : "[" + current + "]");
                previous = current;
            }
            out.append(this.content.charAt(i));
        }
        return out.toString();
    }

    private String stylesAt(int index) {
        StringBuilder names = new StringBuilder();
        for (TextStyle style : TextStyle.values()) {
            for (StyleSpan span : this.spans) {
                if (span.getStyle() == style && span.getStart() <= index && index < span.getEnd()) {
                    if (names.length() > 0) {
                        names.append("+");
                    }
                    names.append(style);
                    break;
                }
            }
        }
        return names.toString();
    }

    public synchronized int getCursorPosition(String userId) {
        Cursor cursor = this.cursors.get(userId);
        if (cursor == null) {
            throw new UserNotFoundException("No cursor for user: " + userId);
        }
        return cursor.getPosition();
    }

    public synchronized List<Comment> getComments() {
        return new ArrayList<>(this.comments);
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public synchronized int getRevision() {
        return this.revision;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public synchronized String getContent() {
        return this.content.toString();
    }

    public synchronized List<Operation> getAppliedOperation() {
        return new ArrayList<>(this.appliedOperations);
    }
}
