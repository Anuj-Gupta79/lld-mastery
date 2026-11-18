package com.lld.problems.googledocs.code.core;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.problems.googledocs.code.constants.RoleType;
import com.lld.problems.googledocs.code.exceptions.DocumentNotFoundException;
import com.lld.problems.googledocs.code.exceptions.DuplicateUserException;
import com.lld.problems.googledocs.code.exceptions.PermissionDeniedException;
import com.lld.problems.googledocs.code.exceptions.UserNotFoundException;
import com.lld.problems.googledocs.code.models.Document;
import com.lld.problems.googledocs.code.models.User;
import com.lld.problems.googledocs.code.operations.InsertOperation;
import com.lld.problems.googledocs.code.operations.Operation;
import com.lld.problems.googledocs.code.operations.OperationEngine;

public class GoogleDocs {
    private final ConcurrentHashMap<String, User> users;
    private final ConcurrentHashMap<String, Document> documents;
    private final OperationEngine engine;

    public GoogleDocs() {
        this.users = new ConcurrentHashMap<>();
        this.documents = new ConcurrentHashMap<>();
        this.engine = new OperationEngine();
    }

    public void registerUser(User user) {
        if (!Objects.isNull(this.users.putIfAbsent(user.getUserId(), user))) {
            throw new DuplicateUserException("User already exist!");
        }
    }

    public void removeUser(String userId) {
        if (!this.users.containsKey(userId)) {
            throw new UserNotFoundException("User not found!");
        }

        this.users.remove(userId);
    }

    public Document createDocument(String ownerId, String name) {
        if (!this.users.containsKey(ownerId)) {
            throw new UserNotFoundException("User not found!");
        }

        Document document = new Document(name, ownerId);
        this.documents.put(document.getDocumentId(), document);

        return document;
    }

    public Document uploadDocument(String ownerId, String text, String name) {
        Document document = createDocument(ownerId, name);

        if (!text.isEmpty()) {
            document.submit(new InsertOperation(ownerId, 0, 0, text), engine);
        }

        return document;
    }

    public Document getDocument(String documentId) {
        Document document = this.documents.get(documentId);
        if (Objects.isNull(document)) {
            throw new DocumentNotFoundException("Document not found!");
        }

        return document;
    }

    public void deleteDocument(String userId, String documentId) {
        User user = this.users.get(userId);

        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found!");
        }

        Document document = this.documents.get(documentId);
        if (Objects.isNull(document)) {
            throw new DocumentNotFoundException("Document not found!");
        }

        if (!Objects.equals(userId, document.getOwnerId())) {
            throw new PermissionDeniedException("Only owner can delete document!");
        }

        this.documents.remove(documentId);
    }

    public void shareDocument(String ownerId, String documentId, String userId, RoleType role) {
        if (Objects.equals(role, RoleType.OWNER)) {
            throw new IllegalArgumentException("Owner role cannot be assigned!");
        }

        User user = this.users.get(userId);

        if (Objects.isNull(user)) {
            throw new UserNotFoundException("User not found!");
        }

        Document document = this.documents.get(documentId);
        if (Objects.isNull(document)) {
            throw new DocumentNotFoundException("Document not found!");
        }

        if (!Objects.equals(ownerId, document.getOwnerId())) {
            throw new PermissionDeniedException("Only owner can share document!");
        }

        document.addMember(userId, role);
    }

    public void edit(String documentId, Operation op) {
        getDocument(documentId).submit(op, this.engine);
    }

    public void undo(String documentId, String userId) {
        getDocument(documentId).undo(userId, this.engine);
    }

    public void redo(String documentId, String userId) {
        getDocument(documentId).redo(userId, this.engine);
    }

    public void restoreVersion(String documentId, String userId, int number) {
        getDocument(documentId).restoreVersion(userId, number, this.engine);
    }
}
