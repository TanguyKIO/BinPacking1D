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
        int numberVar = (int) Math.pow(numberItem, 2) + numberItem; //One var per Item per Bin + one boolean var for empty bin or not
        int startYIndice = (int) Math.pow(numberItem, 2);
        int binLength = context.getBinLength();
        try {
            LpSolve lp = LpSolve.makeLp(0, numberVar); // 0 constraint and numberVar variable

            double row[] = new double[numberItem+1];
            int col[] = new int[numberItem+1];

            double row2[] = new double[numberItem+1];
            int col2[] = new int[numberItem+1];

            double y[] = new double[numberItem]; // Boolean 1 if bin is used 0 else
            int ycol[] = new int[numberItem];

            for(int i=(int) Math.pow(numberItem, 2); i<numberVar; i++){
                lp.setBinary(i, true);
            }

            lp.setAddRowmode(true);
            for(int i=0; i<numberItem; i++) {
                for(int j=0; j<numberItem; j++){
                    col[j] = i*numberItem + j; // Var x_01, x_02, ... x_i*numberItem-j
                    row[j] = items[j].getSize(); // taille de l'item j

                    col2[j] = j*numberItem + i;
                    row2[j] = 1;
                }
                col[numberItem+1] = startYIndice + i;
                row[numberItem+1] = -binLength;
                lp.addConstraintex(numberItem, row, col, LpSolve.LE, 0); // a1*x_i*numberItem-1 + a2*x_i*numberItem-2 + ... - binLength*y <= 0 Contrainte de la taille du bin
                lp.addConstraintex(numberItem, row2, col2, LpSolve.LE, 1); // x_0*numberItem-0 + a2*x_1*numberItem-0 + ... <= 1 Item utiliser
                y[i] = startYIndice + i;
                ycol[i] = 1;
            }

            lp.setObjFnex(numberItem, y, ycol);
            lp.setMinim();

            /* I only want to see important messages on screen while solving */
            lp.setVerbose(LpSolve.IMPORTANT);

            /* Now let lpsolve calculate a solution */
            int ret = lp.solve();
            ret = lp.solve();
            if(ret == LpSolve.OPTIMAL)
                ret = 0;
            else
                ret = 5;

            if(ret == 0) {
                /* a solution is calculated, now lets get some results */

                /* objective value */
                System.out.println("Objective value: " + lp.getObjective());

                double[] results = new double[numberVar];

                /* variable values */
                lp.getVariables(results);
                for(int j = 0; j < numberVar; j++)
                    System.out.println(lp.getColName(j + 1) + ": " + results[j]);
                /* we are done now */
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
            optimalLPSolve(context);
            for(int i=0; i< bins.size(); i++){
                concat += bins.get(i).toString()+";";
            }
            writerQ2.println(concat);
        }
        writerQ1.close();
        writerQ2.close();
    }


}
