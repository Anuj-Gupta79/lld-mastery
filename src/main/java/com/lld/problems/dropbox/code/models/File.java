package com.lld.problems.dropbox.code.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lld.problems.dropbox.code.Events.FileChangeEvent;
import com.lld.problems.dropbox.code.observer.Subject;

public class File extends AbstractFileSystemComponent implements Subject {
    private int size;
    private Version currentVersion;
    private List<Version> versions;
    private User owner;
    private Set<User> directObservers;

    public File(String name, int size, User owner, Version version) {
        super(name);
        this.size = size;
        this.owner = owner;
        this.versions = new ArrayList<>();
        this.directObservers = new HashSet<>();
        updateVersion(version);
    }

    @Override
    public void notifyObservers() {
        Set<User> observers = collectAncestorObservers();
        observers.addAll(getObservers());

        for (User observer : observers) {
            observer.update(new FileChangeEvent(name, currentVersion));
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
        return this.directObservers;
    }

    @Override
    public Set<User> getDirectObservers() {
        return this.directObservers;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void delete() {
        getParent().remove(this);
    }

    @Override
    public void rename(String name) {
        this.name = name;
        notifyObservers();
    }

    public void updateVersion(Version version) {
        this.currentVersion = version;
        this.versions.add(version);
        notifyObservers();
    }

    public User getOwner() {
        return this.owner;
    }

    public List<Version> getAllVersions() {
        return this.versions;
    }

    public Version getCurrentVersion() {
        return this.currentVersion;
    }

}
