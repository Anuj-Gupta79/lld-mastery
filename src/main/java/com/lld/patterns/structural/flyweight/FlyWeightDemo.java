package com.lld.patterns.structural.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface TreeType {
    String getName();

    String getColor();

    String getTexture();

    // LEARNING: The render method allows us to render the tree using its intrinsic state and extrinsic state without needing to know the specific type of tree.
    default void render(int x, int y, int size) {
        System.out.println("Rendering a " + getName() + " with color " + getColor() + " and texture " + getTexture()
                + " at position (" + x + ", " + y + ") with size " + size);
    };
}

class PineTreeType implements TreeType {

    @Override
    public String getName() {
        return "Pine Tree";
    }

    @Override
    public String getColor() {
        return "Green";
    }

    @Override
    public String getTexture() {
        return "Rough";
    }
}

class OakTreeType implements TreeType {

    @Override
    public String getName() {
        return "Oak Tree";
    }

    @Override
    public String getColor() {
        return "Dark Green";
    }

    @Override
    public String getTexture() {
        return "Rough";
    }
}

// LEARNING: TreeFactory is the flyweight factory that creates and manages flyweight objects (TreeType instances).It ensures that flyweight objects are shared properly.
class TreeFactory {
    private static Map<String, TreeType> treeTypes = new HashMap<>();

    public static TreeType getTreeType(String name) {
        TreeType treeType = treeTypes.get(name);
        if (treeType == null) {
            switch (name) {
                case "Pine":
                    treeType = new PineTreeType();
                    break;
                case "Oak":
                    treeType = new OakTreeType();
                    break;
            }
            treeTypes.put(name, treeType);
        }
        return treeType;
    }
}

// LEARNING: Flyweight object — contains intrinsic state (shared data) and
// extrinsic state (unique data).
class Tree {
    private TreeType type;
    private int x;
    private int y;
    private int size;

    public Tree(TreeType type, int x, int y, int size) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void display() {
        type.render(x, y, size);
    }
}

class Forest {
    private List<Tree> forest = new ArrayList<>();

    public void plantTree(String name, int x, int y, int size) {
        TreeType type = TreeFactory.getTreeType(name);
        Tree tree = new Tree(type, x, y, size);
        forest.add(tree);
    }

    public void renderAll() {
        for (Tree tree : forest) {
            tree.display();
        }
    }
}

public class FlyWeightDemo {
    public static void main(String[] args) {
        Forest forest = new Forest();
        forest.plantTree("Pine", 10, 20, 5);
        forest.plantTree("Pine", 15, 25, 5);
        forest.plantTree("Oak", 30, 40, 10);
        forest.plantTree("Oak", 35, 45, 10);

        forest.renderAll();

        // LEARNING: The TreeFactory ensures that only one instance of each TreeType is created and shared among all trees of that type.
        // WHY? This is the core of the Flyweight pattern — sharing common data to save memory. 
        TreeType pine1 = TreeFactory.getTreeType("Pine");
        TreeType pine2 = TreeFactory.getTreeType("Pine");
        System.out.println("Are pine1 and pine2 the same instance? " + (pine1 == pine2));

    }
}
