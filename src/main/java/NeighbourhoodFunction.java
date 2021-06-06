import java.util.List;

public interface NeighbourhoodFunction {

    public abstract void apply(List<Bin> bins);

    public abstract NeighbourhoodFunction reverse();

}
