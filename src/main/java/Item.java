import java.util.Objects;

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

    public void setBin(Bin bin) {
        this.bin = bin;
   }

    public void removeBin(){ bin.removeItem(this);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return size == item.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, bin);
    }

    public void unBind() { this.bin = null; }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(this.size, o.getSize());
    }
}
