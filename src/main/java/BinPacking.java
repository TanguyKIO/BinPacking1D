import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    public static void main(String args[]) {
        ProblemContext[] contexts = FileManager.getContexts();
        for(int i=0;i<contexts.length; i++){
            System.out.println(contexts[i].getBinLength()+" "+contexts[i].getItems().length);
            linearBinPacking(contexts[i]);
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


}
