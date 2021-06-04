import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.PrintWriter;
import java.util.*;

public class BinPacking {

    // Question 1
    static int infBorne(Item[] items, int bin_length) {
        int sum = 0;
        for (int i = 0; i < items.length; i++) {
            sum += items[i].getSize();
        }
        int borne = (int) Math.ceil(sum / bin_length);
        return borne;
    }

    // Question 2
    public static ArrayList<Bin> firstFitDecreasing(ProblemContext context){
        Item[] items = context.getItems();
        int binLength = context.getBinLength();
        Arrays.sort(items, Collections.reverseOrder());
        return firstFit(items, binLength);
    }


    public static ArrayList<Bin> firstFit(Item[] items, int binLength){
        ArrayList<Bin> bins = new ArrayList<>();
        for(Item item: items){
            boolean added = false;
            for(Bin bin : bins){
                if(bin.canAdd(item)){
                    bin.addItem(item);
                    added = true;
                    break;
                }
            }
            if(!added){
                Bin bin = new Bin(binLength);
                bin.addItem(item);
                bins.add(bin);
            }
        }
        return bins;
    }

    static void linearBinPacking(ProblemContext context){
        Item[] items = context.getItems();
        int numItems = items.length;
        int numBins = items.length;
        int binLength = context.getBinLength();
        Loader.loadNativeLibraries();

        // Create the linear solver with the SCIP backend.
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            return;
        }

        MPVariable[][] x = new MPVariable[numItems][numBins];

        for (int i = 0; i < numItems; ++i) {
            for (int j = 0; j < numBins; ++j) {
                x[i][j] = solver.makeIntVar(0, 1, "");
            }
        }
        MPVariable[] y = new MPVariable[numBins];
        for (int j = 0; j < numBins; ++j) {
            y[j] = solver.makeIntVar(0, 1, "");
        }
        // [END variables]

        // [START constraints]
        double infinity = java.lang.Double.POSITIVE_INFINITY;
        for (int i = 0; i < numItems; ++i) {
            MPConstraint constraint = solver.makeConstraint(1, 1, "");
            for (int j = 0; j < numItems; ++j) {
                constraint.setCoefficient(x[i][j], 1);
            }
        }
        // The bin capacity contraint for bin j is
        //   sum_i w_i x_ij <= C*y_j
        // To define this constraint, first subtract the left side from the right to get
        //   0 <= C*y_j - sum_i w_i x_ij
        //
        // Note: Since sum_i w_i x_ij is positive (and y_j is 0 or 1), the right side must
        // be less than or equal to C. But it's not necessary to add this constraint
        // because it is forced by the other constraints.

        for (int j = 0; j < numItems; ++j) {
            MPConstraint constraint = solver.makeConstraint(0, infinity, "");
            constraint.setCoefficient(y[j], binLength);
            for (int i = 0; i < numItems; ++i) {
                constraint.setCoefficient(x[i][j], -items[i].getSize());
            }
        }
        // [END constraints]

        // [START objective]
        MPObjective objective = solver.objective();
        for (int j = 0; j < numBins; ++j) {
            objective.setCoefficient(y[j], 1);
        }
        objective.setMinimization();
        // [END objective]

        // [START solve]
        final MPSolver.ResultStatus resultStatus = solver.solve();
        // [END solve]

        // [START print_solution]
        // Check that the problem has an optimal solution.
        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Nombre de bins utilisées: " + objective.value());
            double totalWeight = 0;
            for (int j = 0; j < numBins; ++j) {
                if (y[j].solutionValue() == 1) {
                    //System.out.println("\nBin " + j + "\n");
                    double binWeight = 0;
                    for (int i = 0; i < numItems; ++i) {
                        if (x[i][j].solutionValue() == 1) {
                            //System.out.println("Item " + i + " - weight: " + items[i].getSize());
                            binWeight += items[i].getSize();
                        }
                    }
                    //System.out.println("Packed bin weight: " + binWeight);
                    totalWeight += binWeight;
                }

            }
            System.out.println("\nTotal packed weight: " + totalWeight);
        } else {
            System.err.println("The problem does not have an optimal solution.");
        }
    }

    static ArrayList<Bin> simpleBinPacking(ProblemContext context) {
        Item[] items = context.getItems();
        int numItems = items.length;
        int binLength = context.getBinLength();
        int totalWeight = 0;
        ArrayList<Bin> pack = new ArrayList<Bin>();
        for (int i = 0; i < numItems; i++) {
            totalWeight += items[i].getSize();
            Bin bin = new Bin(binLength);
            bin.addItem(items[i]);
            pack.add(bin);
        }
        return pack;
    }

    // Question 4 b
    public static ArrayList<Bin> firstFitRandom(ProblemContext context){
        Item[] items = context.getItems();
        int binLength = context.getBinLength();
        Collections.shuffle(Arrays.asList(items));
        return firstFit(items, binLength);
    }

    //Question 5 a
    public static boolean moveItem(Item item, Bin bin){
        if(bin.canAdd(item)){
            item.removeBin();
            bin.addItem(item);
            return true;
        }
        return false;
    }


    public static boolean moveItem2(Item item, Bin bin, ArrayList<Bin> bins){
        if(bin.canAdd(item)){
            Bin pastBin = item.getBin();
            item.removeBin();
            bin.addItem(item);
            if(pastBin.isBinEmpty()) {
                bins.remove(pastBin);
            }
            return true;
        } else return false;
    }

    //Question 5 b
    static boolean switchItem(Item item1, Item item2){
        Bin bin1  = item1.getBin();
        Bin bin2  = item2.getBin();
        bin1.removeItem(item1);
        bin2.removeItem(item2);
        if(bin1.canAdd(item2) && bin2.canAdd(item1)){
            bin1.addItem(item2);
            bin2.addItem(item1);
            return true;
        } else {
            bin1.addItem(item1);
            bin2.addItem(item2);
            return false;
        }
    }

    static NeightberhoodFunction getBestVoisin(List<Bin> originalBins, List<NeightberhoodFunction> tabouList){
        int bestFitness = 0;
        List<Bin> bestSol = originalBins;
        NeightberhoodFunction bestNeightberhoodFunction = null;
        for(int indexFirstBin=0; indexFirstBin<originalBins.size(); indexFirstBin++){
            for(int indexFirstItem=0; indexFirstItem<originalBins.get(indexFirstBin).getItems().size(); indexFirstItem++){
                double r = Math.random()*2;
                for(int indexSecondBin = 0; indexSecondBin<originalBins.size(); indexSecondBin++){
                    if(indexSecondBin==indexFirstBin) continue;
                    Bin previousBin = originalBins.get(indexFirstBin);
                    Bin newBin = originalBins.get(indexSecondBin);
                    Item item = previousBin.getItem(indexFirstItem);
                    if(r<1){ // Add
                        NeightberhoodFunction neightberhoodFunction = new Move(item, newBin, previousBin);
                        if(!tabouList.contains(neightberhoodFunction) && moveItem(item, newBin)){
                            int fitness = getFitness(originalBins);
                            moveItem(item, previousBin);
                            if(fitness > bestFitness){
                                bestFitness = fitness;
                                bestNeightberhoodFunction = neightberhoodFunction;
                            };
                        }
                    }else{ // Switch
                        for(int indexSecondItem=0; indexSecondItem<originalBins.get(indexSecondBin).getItems().size(); indexSecondItem++){
                            Item switchItem = newBin.getItem(indexSecondItem);
                            NeightberhoodFunction neightberhoodFunction = new Switch(item, switchItem, previousBin, newBin);
/*
                            System.out.println("TabouList: "+tabouList +" F :"+ neightberhoodFunction);
*/
                            if(item.getSize() != switchItem.getSize() && !tabouList.contains(neightberhoodFunction) && switchItem(item, switchItem) ){
                                int fitness = getFitness(originalBins);
                                switchItem(switchItem, item);
                                if(fitness > bestFitness){
                                    bestFitness = fitness;
                                    bestNeightberhoodFunction =  neightberhoodFunction;
                                }
                            }
                        }
                    }
                }

            }
        }
        return bestNeightberhoodFunction;
    }

    static ArrayList<Bin> tabouSearch(List<Bin> solution, int memorySize, int attempt){
        List<NeightberhoodFunction> tabouList = new ArrayList<>();
        Map<List<Bin>, List<NeightberhoodFunction>> previousStates = new HashMap<>();
        int previousFitness = getFitness(solution);
        for(int i =0; i<attempt; i++){
            NeightberhoodFunction bestNeightberhoodFunction = getBestVoisin(solution, tabouList);
            /*List<Bin> solutionCopy = new ArrayList<>();
            System.out.println("solutionCopy not working ?");
            solution.get(0).deepCopy();
            System.out.println("work on 1");
            for(int j=0; j<solution.size(); j++){
                solutionCopy.add(solution.get(j).deepCopy());
            }
            previousStates.put(solutionCopy, tabouList);
            System.out.println("solutionCopy work");*/
            bestNeightberhoodFunction.apply();
            int newFitness = getFitness(solution);
            if(newFitness<=previousFitness) {
                tabouList.add(bestNeightberhoodFunction.reverse());
                if(tabouList.size()>memorySize) tabouList.remove(0);
                if(tabouList.size()==0)
                    System.out.println("chat");
            }
            previousFitness = newFitness;
            /*System.out.println("fitness work");*/
            System.out.println("attempt number :"+ i+" fitness :" + newFitness);
            System.out.println("TabouList: "+tabouList +" Fbest :"+ bestNeightberhoodFunction);
/*            for (Map.Entry<List<Bin>, List<NeightberhoodFunction>> entry : previousStates.entrySet()) {
                List<Bin> previousSolution = entry.getKey();
                List<NeightberhoodFunction> previousTabou = entry.getValue();
                if(previousSolution.equals(solution) && tabouList.equals(previousTabou)) break;
            }
            System.out.println("test works");*/
        }
        return (ArrayList)solution;
    }


    public static void main(String args[]) {
        ProblemContext[] contexts = FileManager.getContexts();
        ArrayList<Bin> solution = new ArrayList<>();
        for(int i=0;i<contexts.length; i++){
            //System.out.println(contexts[i].getBinLength()+" "+contexts[i].getItems().length);
            //simpleBinPacking(contexts[i]);
            solution = simulatedAnnealing(firstFitDecreasing(contexts[i]), 1000, 200, 100, 0.59);
            System.out.println("Nb de bins utilisés : "+ solution.size());
        }

        /*PrintWriter writerQ1 = FileManager.getWriter("question1");
        PrintWriter writerQ2 = FileManager.getWriter("question2");
        for (ProblemContext context: contexts) {
            writerQ1.println(context.getName()+';'+infBorne(context.getItems(), context.getBinLength())+';');
            ArrayList<Bin> bins = firstFitDecreasing(context);
            String concat = context.getName()+';';
            for(int i=0; i< bins.size(); i++){
                concat += bins.get(i).toString()+";";
            }
            writerQ2.println(concat);
        }
        writerQ1.close();
        writerQ2.close();*/
    }


    static int getFitness(List<Bin> bins){
        int f = 0;
        for (Bin b: bins) {
            f += b.getFitness();
        }
        return f;
    }

    public static ArrayList<Bin> simulatedAnnealing(ArrayList<Bin> x0, double t0, int n1, int n2, double mu){
        ArrayList<Bin> xmax = x0;
        int fmax = getFitness(x0);
        int i = 0;
        ArrayList<ArrayList<Bin>> xi = new ArrayList<>();
        ArrayList<Double> tk = new ArrayList<>();
        tk.add(t0);
        xi.add(x0);
        List<Item> items;
        List<Item> items2;
        int random;
        int random2;
        int deltaf;
        boolean found;
        double p;
        for(int k=0; k<n1; k++){
            for(int l=0; l<n2; l++){
                ArrayList<Bin> y = (ArrayList<Bin>) xi.get(i).clone();
                found = false;
                while(!found){
                    found = false;
                    random = 0;
                    random2 = 0;
                    if(Math.random() > 0.50){
                        while(random==random2){
                            random = (int) (Math.random() * (y.size()));
                            random2 = (int) (Math.random() * (y.size()));
                        }
                        items = y.get(random).getItems();
                        items2 = y.get(random2).getItems();
                        random = (int) (Math.random() * (items.size()));
                        random2 = (int) (Math.random() * (items2.size()));
                        Item item1 = items.get(random);
                        Item item2 = items2.get(random2);
                        if(switchItem(item1, item2)) {
                            found = true;
                        }
                    } else {
                        random = (int) (Math.random() * (y.size()));
                        Bin bin = y.get(random);
                        do {
                            random2 = (int) (Math.random() * (y.size()));
                        } while(random==random2);
                        random = (int) (Math.random() * (y.get(random2).getItems().size()));
                        Item item = y.get(random2).getItems().get(random);
                        if(moveItem(item, bin, y)) {
                            found = true;
                        }
                    }
                }
                deltaf = getFitness(y) - getFitness(xi.get(i));
                if(deltaf<=0){
                    xi.add(y);
                    if(fmax<getFitness(y)) {
                        xmax = y;
                        fmax = getFitness(y);
                    }
                } else {
                    p = Math.random();
                    if(p <= Math.exp(-deltaf/tk.get(k))) xi.add(y);
                    else xi.add(xi.get(i));
                }
                i++;
            }
            tk.add(mu*tk.get(k));
        }
        return xmax;
    }
}
