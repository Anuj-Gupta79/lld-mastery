package com.lld.problems.dropbox.code.models;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractFileSystemComponent implements FileSystemComponent {
    protected String name;
    protected Directory parent;

    protected AbstractFileSystemComponent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Directory getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public abstract int getSize();

    public abstract void delete();

    protected Set<User> collectAncestorObservers() {
        Set<User> result = new HashSet<>();

        Directory current = this.parent;

        while (current != null) {
            result.addAll(current.getDirectObservers());
            current = current.getParent();
        }

        return result;
    }
}
