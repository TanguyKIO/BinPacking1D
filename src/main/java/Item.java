public class Item implements Comparable<Item>{
    private int size;

    public Item(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toString(){
        return String.valueOf(this.size);
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(this.size, o.getSize());
    }
}
