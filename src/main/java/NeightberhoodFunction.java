import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface NeightberhoodFunction {

    public abstract void apply(List<Bin> bins);

    public abstract NeightberhoodFunction reverse();

}
