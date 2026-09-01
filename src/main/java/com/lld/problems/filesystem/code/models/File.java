package com.lld.problems.filesystem.code.models;

import java.util.Objects;

public class File extends AbstractFileSystemComponent {
    private int size;

    public File(String name, int size) {
        super(name);
        this.size = size;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void delete() {
        if (Objects.isNull(getParent())) {
            throw new IllegalStateException("[File Deletion] Parent can never be null!");
        }

        getParent().remove(this);
    }

}
