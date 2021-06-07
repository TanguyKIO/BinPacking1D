import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabouThread implements Runnable{
    private Thread t;
    private Item[] items;
    private int binLength;
    private String contextName;
    private PrintWriter writer;
    private List<Integer> iterationNumber;

    public TabouThread(Item[] items, int binLength, String contextName) {
        this.items = items;
        this.binLength = binLength;
        this.contextName = contextName;
        iterationNumber = Arrays.asList(0, 5, 10, 20, 30, 50, 100, 200, 500, 750, 1000);
        try {
            writer = new PrintWriter("results/Question7_"+contextName+ ".csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        writer.println("Paramètres;;0;5;10;20;30;50;100;200;500;750;1000");
        int [] paramsTabouSize = {1, 2, 3, 4};
        for(int paramTabouSize : paramsTabouSize){
            writer.print(paramTabouSize+";simpleBinPacking;");
            tabouSearch(BinPacking.simpleBinPacking(items, binLength), paramTabouSize, 1000);
            writer.println();
            writer.print(";firstFitDecreasing;");
            tabouSearch(BinPacking.firstFitDecreasing(items, binLength), paramTabouSize, 1000);
            writer.println();
        }
        writer.close();
        System.out.println("Finishing " +  contextName);
    }

    private ArrayList<Bin> tabouSearch(ArrayList<Bin> solution, int memorySize, int attempt) {
        List<NeighbourhoodFunction> tabouList = new ArrayList<>();
        int previousFitness = BinPacking.getFitness(solution);
        writer.print(solution.size() + ";");
        for(int i =0; i<attempt; i++){
            NeighbourhoodFunction bestNeighbourhoodFunction = BinPacking.getBestNeighberhoodFunction(solution, tabouList);
            if(bestNeighbourhoodFunction != null) {
                bestNeighbourhoodFunction.apply(solution);
                int newFitness = BinPacking.getFitness(solution);
                if (newFitness <= previousFitness) {
                    tabouList.add(bestNeighbourhoodFunction.reverse());
                    if (tabouList.size() > memorySize) tabouList.remove(0);
                }
                previousFitness = newFitness;
            }
            if (iterationNumber.contains(i + 1)) writer.print(solution.size() + ";");
        }
        return solution;
    }

    public void start() {
        System.out.println("Starting " +  contextName );
        if(t == null){
            t = new Thread(this, contextName);
            t.start();
        }
    }
}
