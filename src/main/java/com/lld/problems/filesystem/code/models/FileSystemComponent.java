package com.lld.problems.filesystem.code.models;

public interface FileSystemComponent {
    String getName();

    int getSize();

    void rename(String name);

    void delete();

    void setParent(Directory parent);

    Directory getParent();
}
