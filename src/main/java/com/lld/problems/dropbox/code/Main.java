package com.lld.problems.dropbox.code;

import com.lld.problems.dropbox.code.models.Directory;
import com.lld.problems.dropbox.code.models.File;
import com.lld.problems.dropbox.code.models.User;
import com.lld.problems.dropbox.code.models.Version;

public class Main {
    public static void main(String[] args) {
        // ---- Build tree: root -> sub -> file1 ----
        Directory root = new Directory("root");
        Directory sub = new Directory("sub");
        User owner = new User("owner1");
        Version v1 = new Version(1);
        File file1 = new File("file1.txt", 100, owner, v1);

        root.add(sub);
        sub.add(file1);

        // ---- Users ----
        User rootSubscriber = new User("rootSubscriber"); // subscribed at root only
        User fileSubscriber = new User("fileSubscriber"); // subscribed directly to file1

        root.addObserver(rootSubscriber);
        file1.addObserver(fileSubscriber);

        System.out.println("=== File1 gets a new version ===");
        Version v2 = new Version(2);
        file1.updateVersion(v2);
        // Expect: BOTH rootSubscriber (ancestor) and fileSubscriber (direct) notified,
        // exactly once each.

        System.out.println();
        System.out.println("=== Sub directory is renamed ===");
        sub.rename("sub-renamed");
        // Expect: rootSubscriber notified (ancestor of sub).
        // fileSubscriber should NOT be notified — they subscribed to file1, not sub or
        // root.

        System.out.println();
        System.out
                .println("=== Query: who is watching the whole root subtree? (getObservers, unrelated to notify) ===");
        System.out.println(root.getObservers().stream().map(User::getName).toList());
        // Expect: [rootSubscriber, fileSubscriber] — union of root's own + everything
        // below it.

        System.out.println();
        System.out.println("=== Delete sub (cascades to file1) ===");
        sub.delete();
        System.out.println("root's children after delete: " + root.getChildren().size());
        // Expect: 0 — sub removed from root, file1 removed from sub via cascading
        // delete.
    }
}
