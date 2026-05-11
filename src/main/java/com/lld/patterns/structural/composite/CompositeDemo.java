package com.lld.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

// LEARNING: FileSystemComponent is the interface that defines the common operations for both File and Folder.
// WHY? It allows us to treat individual files and folders uniformly, enabling us to build a tree structure of files and folders without worrying about their specific types.
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

    // LEARNING: We can recursively calculate the total size.
    // WHY? same interface on File and Folder means recursion just works..
    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent component : components) {
            totalSize += component.getSize();
        }
        return totalSize;
    }

    // LEARNING: We can recursively display the folder structure.
    // WHY? Shared interface means each child handles its own display — no instanceof needed.
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
        Folder projects = new Folder("Projects");
        projects.addComponent(lld);
        projects.addComponent(dsa);

        // WHY? Declared as Folder (not FileSystemComponent) to access addComponent during construction.
        Folder rootFolder = new Folder("Root");
        rootFolder.addComponent(resume);
        rootFolder.addComponent(notes);
        rootFolder.addComponent(projects);

        rootFolder.display("");
        System.out.println("Total size of Root folder: " + rootFolder.getSize() + " bytes");
    }
}
