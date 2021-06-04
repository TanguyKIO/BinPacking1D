import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Move implements NeightberhoodFunction{
    private Item item;
    private Bin newBin;
    private Bin previousBin;

    public Move(Item item, Bin newBin, Bin previousBin) {
        this.item = item;
        this.newBin = newBin;
        this.previousBin = previousBin;
    }

    @Override
    public Move reverse(){
        return new Move(item, previousBin, newBin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return item.equals(move.item) && newBin.equals(move.newBin) && previousBin.equals(move.previousBin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, newBin, previousBin);
    }

    @Override
    public void apply(List<Bin> bins){
        item.removeBin();
        newBin.addItem(item);
        if(previousBin.isBinEmpty()) bins.remove(previousBin);
    }

    @Override
    public String toString() {
        return "Move{" +
                "item=" + item +
                ", newBin=" + newBin +
                ", previousBin=" + previousBin +
                '}';
    }
}
