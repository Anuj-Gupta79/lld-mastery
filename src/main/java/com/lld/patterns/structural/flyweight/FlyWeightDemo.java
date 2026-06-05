package com.lld.patterns.structural.flyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface TreeType {
    String getName();

    String getColor();

    String getTexture();

    // LEARNING: render() uses intrinsic state (name/color/texture) + extrinsic
    // state (x, y, size) passed at call time.
    default void render(int x, int y, int size) {
        System.out.println("Rendering a " + getName() + " with color " + getColor() + " and texture " + getTexture()
                + " at position (" + x + ", " + y + ") with size " + size);
    }
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

// LEARNING: Factory caches shared TreeType instances — new object created only
// on cache miss.
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
                default:
                    throw new IllegalArgumentException("Unknown tree type: " + name);
            }
            treeTypes.put(name, treeType);
        }
        return treeType;
    }
}

// LEARNING: Tree holds extrinsic state (x, y, size); intrinsic state lives in
// shared TreeType.
// WHY: Thousands of trees share one TreeType instance instead of duplicating
// name/color/texture.
class Tree {
    private TreeType type;
    private int x, y, size;

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
        forest.add(new Tree(type, x, y, size));
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

        // LEARNING: Same name returns same cached instance — reference equality proves
        // sharing.
        TreeType pine1 = TreeFactory.getTreeType("Pine");
        TreeType pine2 = TreeFactory.getTreeType("Pine");
        System.out.println("Are pine1 and pine2 the same instance? " + (pine1 == pine2));
    }
}