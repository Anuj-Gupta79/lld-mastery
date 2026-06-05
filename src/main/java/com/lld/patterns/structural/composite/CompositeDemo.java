package com.lld.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

// LEARNING: Component interface — defines operations for both leaf (File) and composite (Folder).
// WHY: Lets client treat individual objects and compositions uniformly — no instanceof needed.
interface FileSystemComponent {
    String getName();

    long getSize();

    void display(String indent);
}

class File implements FileSystemComponent {
    private String name;
    private long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "File: " + name + " (Size: " + size + " bytes)");
    }
}

class Folder implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> components;

    public Folder(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    public void addComponent(FileSystemComponent component) {
        components.add(component);
    }

    @Override
    public String getName() {
        return name;
    }

    // LEARNING: Recursion works here because File and Folder share the same
    // interface.
    // WHY: Folder delegates to each child's getSize() — no type checking, tree
    // depth doesn't matter.
    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent component : components) {
            totalSize += component.getSize();
        }
        return totalSize;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Folder: " + name);
        for (FileSystemComponent component : components) {
            component.display(indent + "  ");
        }
    }
}

public class CompositeDemo {
    public static void main(String[] args) {
        File resume = new File("resume.pdf", 120);
        File notes = new File("notes.txt", 30);

        File lld = new File("lld.java", 80);
        File dsa = new File("dsa.java", 60);

        // WHY: Declared as Folder (not FileSystemComponent) to access addComponent
        // during construction.
        Folder projects = new Folder("Projects");
        projects.addComponent(lld);
        projects.addComponent(dsa);

        Folder rootFolder = new Folder("Root");
        rootFolder.addComponent(resume);
        rootFolder.addComponent(notes);
        rootFolder.addComponent(projects);

        rootFolder.display("");
        System.out.println("Total size of Root folder: " + rootFolder.getSize() + " bytes");
    }
}