package main.java;

public class ProblemContext {
    private int binLength;
    private Item[] items;

    public ProblemContext(int length, Item[] items){
        this.binLength = length;
        this.items = items;
    }

    public int getBinLength() { return binLength; }

    public void setBinLength(int binLength) { this.binLength = binLength; }

    public Item[] getItems() { return items; }

    public void setItems(Item[] items) { this.items = items; }
}