import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Switch implements NeightberhoodFunction {
    private Item item;
    private Item itemSwitch;
    private Bin bin;
    private Bin binSwitch;

    public Switch(Item item, Item itemSwitch, Bin bin, Bin binSwitch){
        this.item = item;
        this.itemSwitch = itemSwitch;
        this.bin = bin;
        this.binSwitch = binSwitch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Switch aSwitch = (Switch) o;
        return (item.equals(aSwitch.item) && itemSwitch.equals(aSwitch.itemSwitch) && bin.equals(aSwitch.bin) && binSwitch.equals(aSwitch.binSwitch)) || (item.equals(aSwitch.itemSwitch) && itemSwitch.equals(aSwitch.item) && bin.equals(aSwitch.binSwitch) && binSwitch.equals(aSwitch.bin)) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, itemSwitch, bin, binSwitch);
    }

    @Override
    public void apply(List<Bin> bins){
        Bin bin1 = item.getBin();
        Bin bin2 = itemSwitch.getBin();
        bin1.removeItem(item);
        bin2.removeItem(itemSwitch);
        bin2.addItem(item);
        bin1.addItem(itemSwitch);
    }

    @Override
    public Switch reverse(){
        return this;
    }

    @Override
    public String toString() {
        return "Switch{" +
                "item=" + item +
                ", itemSwitch=" + itemSwitch +
                '}';
    }
}
