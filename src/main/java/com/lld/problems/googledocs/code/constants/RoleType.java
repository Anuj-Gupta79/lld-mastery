package com.lld.problems.googledocs.code.constants;

public enum RoleType {
    OWNER(true, true, true, true),
    EDITOR(true, true, true, false),
    COMMENTER(true, true, false, false),
    READER(true, false, false, false);

    private final boolean read;
    private final boolean comment;
    private final boolean edit;
    private final boolean manage;

    RoleType(boolean read, boolean comment, boolean edit, boolean manage) {
        this.read = read;
        this.comment = comment;
        this.edit = edit;
        this.manage = manage;
    }

    public boolean canEdit() {
        return this.edit;
    }

    public boolean canRead() {
        return this.read;
    }

    public boolean canComment() {
        return this.comment;
    }

    public boolean canManage() {
        return this.manage;
    }
}
