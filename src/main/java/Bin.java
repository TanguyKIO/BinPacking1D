package main.java;

import java.util.HashSet;
import java.util.Set;

public class Bin {
    private final int length;
    private int spaceLeft;
    private Set<Item> items;

    public Bin(int length){
        this.length = length;
        this.spaceLeft = length;
        items = new HashSet<Item>();
    }

    public void addItem(Item item) {
        items.add(item);
        spaceLeft -= item.getSize();
    }

    public void removeItem(Item item){
        items.remove(item);
        spaceLeft += item.getSize();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSpaceLeft(){
        return spaceLeft;
    }

    public boolean isBinValid(){
        return spaceLeft >= 0;
    }
}
