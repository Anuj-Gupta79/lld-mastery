package com.lld.problems.filesystem.code;

import com.lld.problems.filesystem.code.exceptions.InvalidDirectoryException;
import com.lld.problems.filesystem.code.models.Directory;
import com.lld.problems.filesystem.code.models.File;

public class Main {
    public static void main(String[] args) {

        // ---- Build tree ----
        // root
        // ├── file1.txt (439)
        // ├── file2.txt (100)
        // └── sub2
        // └── file3.txt (320)
        File file1 = new File("file1.txt", 439);
        File file2 = new File("file2.txt", 100);
        File file3 = new File("file3.txt", 320);

        Directory root = new Directory("root");
        Directory sub2 = new Directory("sub2");

        sub2.add(file3);
        root.add(file1);
        root.add(file2);
        root.add(sub2);

        // ---- Scenario 1: getSize() recursive sum ----
        int expectedTotal = 439 + 100 + 320; // 859
        int actualTotal = root.getSize();
        System.out.println("Scenario 1: getSize()");
        System.out.println("  Expected: " + expectedTotal + ", Actual: " + actualTotal);
        System.out.println("  PASS: " + (expectedTotal == actualTotal));
        System.out.println();

        // ---- Scenario 2: delete() on non-root child (a File) ----
        System.out.println("Scenario 2: delete() non-root File (file1.txt)");
        file1.delete();
        boolean stillInChildren = root.getChildren().contains(file1);
        int expectedAfterFileDelete = 100 + 320; // 420
        int actualAfterFileDelete = root.getSize();
        System.out.println("  file1 still in root.children? " + stillInChildren + " (expect false)");
        System.out.println(
                "  root.getSize() Expected: " + expectedAfterFileDelete + ", Actual: " + actualAfterFileDelete);
        System.out.println("  PASS: " + (!stillInChildren && expectedAfterFileDelete == actualAfterFileDelete));
        System.out.println();

        // ---- Scenario 3: delete() on non-root Directory (cascading) ----
        System.out.println("Scenario 3: delete() non-root Directory (sub2, cascades to file3)");
        sub2.delete();
        boolean sub2StillInChildren = root.getChildren().contains(sub2);
        int expectedAfterDirDelete = 100; // only file2 left
        int actualAfterDirDelete = root.getSize();
        System.out.println("  sub2 still in root.children? " + sub2StillInChildren + " (expect false)");
        System.out
                .println("  root.getSize() Expected: " + expectedAfterDirDelete + ", Actual: " + actualAfterDirDelete);
        System.out.println("  PASS: " + (!sub2StillInChildren && expectedAfterDirDelete == actualAfterDirDelete));
        System.out.println();

        // ---- Scenario 4: delete() on root -> InvalidDirectoryException ----
        System.out.println("Scenario 4: delete() on root (should throw InvalidDirectoryException)");
        try {
            root.delete();
            System.out.println("  FAIL: no exception thrown");
        } catch (InvalidDirectoryException e) {
            System.out.println("  PASS: caught InvalidDirectoryException -> " + e.getMessage());
        }
        System.out.println();

        // ---- Scenario 5: delete() on an orphaned File -> IllegalStateException ----
        // Not reachable through normal add()/delete() flow -- constructed directly
        // and never added to any Directory, to simulate the invariant-violation case.
        System.out.println("Scenario 5: delete() on orphaned File (should throw IllegalStateException)");
        File orphan = new File("orphan.txt", 50);
        try {
            orphan.delete();
            System.out.println("  FAIL: no exception thrown");
        } catch (IllegalStateException e) {
            System.out.println("  PASS: caught IllegalStateException -> " + e.getMessage());
        }
    }
}