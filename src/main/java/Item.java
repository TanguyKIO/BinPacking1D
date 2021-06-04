public class Item implements Comparable<Item> {
    private int size;
    private Bin bin;

    public Item(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toString() {
        return String.valueOf(this.size);
    }

    public Bin getBin() { return bin; }

    public void setBin(Bin bin) { this.bin = bin; }

    public void removeBin(){
        bin.removeItem(this);
        this.bin = null;
    }

    private void unBind() { this.bin = null; }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(this.size, o.getSize());
    }
}
