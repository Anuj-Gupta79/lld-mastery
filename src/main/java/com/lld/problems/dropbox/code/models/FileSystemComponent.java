package com.lld.problems.dropbox.code.models;

public interface FileSystemComponent {
    public String getName();

    public int getSize();

    public void rename(String name);

    public Directory getParent();

    public void setParent(Directory parent);

    public void delete();
}
