import java.util.*;

public class Bin {
    private final int length;
    private int spaceLeft;
    private List<Item> items;

    public Bin(int length){
        this.length = length;
        this.spaceLeft = length;
        items = new ArrayList<>();
    }

    public boolean canAdd(Item item){
        return  item.getSize()<=spaceLeft;
    }

    public void addItem(Item item){
        item.setBin(this);
        items.add(item);
        spaceLeft -= item.getSize();
        Collections.sort(items); // Permet au equal de ne pas tenir compte de l'ordre
    }

    public void removeItem(Item item){
        items.remove(item);
        spaceLeft += item.getSize();
        item.setBin(null);
    }

    public int getLength() {
        return length;
    }

    public int getSpaceLeft(){
        return spaceLeft;
    }

    public boolean isBinValid(){
        return spaceLeft >= 0;
    }

    public boolean isBinEmpty(){
        return items.isEmpty();
    }

    public String toString(){
        if(items.size() == 0) return "empty";
        String concat = "";
        for(Item item: items){ concat += item.toString() + '-'; }
        return concat.substring(0, concat.length() - 1);
    }

    public int getFitness() {
        int f = 0;
        for(Item i : items) f += i.getSize();
        return f*f;
    }

    public Item getItem(int index){
        try {
            return items.get(index);
        }catch (IndexOutOfBoundsException i ){
            return items.get(0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bin)) return false;
        Bin bin = (Bin) o;
        return length == bin.length && spaceLeft == bin.spaceLeft && items.equals(bin.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, spaceLeft, items);
    }

    public List<Item> getItems(){
        return items;
    }

    /**
     *
     * @return DeepCopy of bin
     */
    protected Bin deepCopy() {
        Bin clone = new Bin(length);
        for (Item i: items) {
            clone.addItem(new Item(i.getSize()));
        }
        return clone;
    }
}
