public class ProblemContext {
    private int binLength;
    private Item[] items;
    private String name;

    public ProblemContext(int length, Item[] items, String name){
        this.binLength = length;
        this.items = items;
        this.name = name;
    }

    public int getBinLength() { return binLength; }

    public Item[] getItems() { return items; }

    public String getName() {
        return name;
    }
}