package com.lld.problems.filesystem.code.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.lld.problems.filesystem.code.exceptions.InvalidDirectoryException;

public class Directory extends AbstractFileSystemComponent {
    private List<FileSystemComponent> children;

    public Directory(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    @Override
    public int getSize() {
        int totalSize = 0;

        for (FileSystemComponent child : this.children) {
            totalSize += child.getSize();
        }

        return totalSize;
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
