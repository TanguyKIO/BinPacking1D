import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if(item.getBin() != null){
            item.removeBin();
        }
        item.setBin(this);
        item.setBin(this);
        items.add(item);
        spaceLeft -= item.getSize();
    }

    public void removeItem(Item item){
        item.unBind();
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

    public int getFitness() {
        int f = 0;
        for(Item i : items) f += i.getSize();
        return f;
    }

    public Item getItem(int index){
        return items.get(index);
    }

    public List<Item> getItems(){
        return items;
    }
}
