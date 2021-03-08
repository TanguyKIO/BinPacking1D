import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import java.io.PrintWriter;
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
        int bin_length = context.getBinLength();
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
                Bin bin = new Bin(bin_length);
                bin.addItem(item);
                bins.add(bin);
            }
        }
        return bins;
    }

    public static void optimalLPSolve(ProblemContext context){
        Item[] items = context.getItems();
        int numberItem = items.length;
        int numberVar = (int) Math.pow(numberItem, 2);
        int binLength = context.getBinLength();
        try {
            LpSolve lp = LpSolve.makeLp(0, numberVar);

            double row[] = new double[numberItem];
            int col[] = new int[numberItem];

            lp.setAddRowmode(true);
            for(int i=0; i<numberItem; i++) {
                for (int j = 0; i < numberItem; i++) {
                    col[i] = i*numberItem + j; //Variable x_01, x_02 ...
                    row[i] = items[i].getSize();
                    lp.addConstraintex(numberItem, row, col, LpSolve.LE, binLength);
                }
            }

        } catch (LpSolveException e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        ProblemContext[] contexts = FileManager.getContexts();
        for(int i=0;i<contexts.length; i++){
            System.out.println(contexts[i].getBinLength()+" "+contexts[i].getItems().length);
        }
        PrintWriter writerQ1 = FileManager.getWriter("question1");
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
        writerQ2.close();
    }


}
