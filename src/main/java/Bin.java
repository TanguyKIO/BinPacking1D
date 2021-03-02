package main.java;

import java.util.Set;

public class Bin {
    private int length;
    private int spaceLeft;
    private Set<Item> items;

    public Bin(int length){
        this.length = length;
        this.spaceLeft = length;
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
        return spaceLeft >= 0 ? true : false;
    }
}