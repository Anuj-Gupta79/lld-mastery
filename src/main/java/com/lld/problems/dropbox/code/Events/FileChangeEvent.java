package com.lld.problems.dropbox.code.Events;

import com.lld.problems.dropbox.code.models.Version;

public class FileChangeEvent extends ChangeEvent {
    private final Version version;

    public FileChangeEvent(String name, Version version) {
        super(name);
        this.version = version;
    }

    public Version getVersion() {
        return this.version;
    }

    public String toString() {
        return "There is an updated to file: " + getComponentName() + ", version= " + getVersion().getVersionNumber();
    }
}
