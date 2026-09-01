package com.lld.problems.filesystem.code.models;

public abstract class AbstractFileSystemComponent implements FileSystemComponent {
    protected String name;
    protected Directory parent;

    AbstractFileSystemComponent(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void rename(String name) {
        this.name = name;
    }

    @Override
    public void setParent(Directory parent) {
        this.parent = parent;
    }
    
    @Override
    public Directory getParent() {
        return this.parent;
    }

    public abstract int getSize();

    public abstract void delete();
}
