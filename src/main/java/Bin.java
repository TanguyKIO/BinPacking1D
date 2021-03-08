import java.util.HashSet;
import java.util.Set;

public class Bin {
    private final int length;
    private int spaceLeft;
    private Set<Item> items;

    public Bin(int length){
        this.length = length;
        this.spaceLeft = length;
        items = new HashSet<>();
    }

    public boolean canAdd(Item item){
        return  item.getSize()<=spaceLeft;
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

    public int getSpaceLeft(){
        return spaceLeft;
    }

    public boolean isBinValid(){
        return spaceLeft >= 0;
    }

    public String toString(){
        String concat = "";
        for(Item item: items){ concat += item.toString() + '-'; }
        return concat.substring(0, concat.length() - 1);
    }
}
