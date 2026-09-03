package com.lld.problems.dropbox.code.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.lld.problems.dropbox.code.Events.DirectoryChangeEvent;
import com.lld.problems.dropbox.code.exceptions.InvalidDirectoryException;
import com.lld.problems.dropbox.code.observer.Subject;

public class Directory extends AbstractFileSystemComponent implements Subject {
    private Set<User> directObservers;
    private List<FileSystemComponent> children;

    public Directory(String name) {
        super(name);
        this.directObservers = new HashSet<>();
        this.children = new ArrayList<>();
    }

    @Override
    public int getSize() {
        int size = 0;
        for (FileSystemComponent child : this.children) {
            size += child.getSize();
        }
        return size;
    }

    @Override
    public void delete() {
        if (Objects.isNull(getParent())) {
            throw new InvalidDirectoryException("[Error Delete Directory] Root Directory Cannot be deleted!");
        }

        List<FileSystemComponent> childrenCopy = new ArrayList<>(this.children);
        for (FileSystemComponent child : childrenCopy) {
            child.delete();
        }

        getParent().remove(this);
    }

    @Override
    public void notifyObservers() {
        Set<User> observers = collectAncestorObservers();
        observers.addAll(getDirectObservers());

        for (User observer : observers) {
            observer.update(new DirectoryChangeEvent(name));
        }
    }

    @Override
    public void addObserver(User user) {
        this.directObservers.add(user);
    }

    @Override
    public void removeObserver(User user) {
        this.directObservers.remove(user);
    }

    @Override
    public Set<User> getObservers() {
        Set<User> result = new HashSet<>();

        for (FileSystemComponent child : this.children) {
            if (child instanceof Subject subject) {
                result.addAll(subject.getObservers());
            }
        }

        result.addAll(getDirectObservers());

        return result;
    }

    @Override
    public Set<User> getDirectObservers() {
        return this.directObservers;
    }

    @Override
    public void rename(String name) {
        this.name = name;
        notifyObservers();
    }

    public void add(FileSystemComponent child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void remove(FileSystemComponent child) {
        this.children.remove(child);
        child.setParent(null);
    }

    public List<FileSystemComponent> getChildren() {
        return this.children;
    }

}
