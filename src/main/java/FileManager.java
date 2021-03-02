package main.java;
import java.io.*;

public class FileManager{

    public static ProblemContext[] getContexts(){
        String repertory = "data/";
        File file = new File(repertory);
        System.out.println(file.getAbsolutePath());
        File[] files = file.listFiles();
        ProblemContext[] contexts = new ProblemContext[files.length];
        for(int i=0; i<files.length; i++){
            contexts[i] = FileManager.readFile(files[i]);
        }
        return contexts;
    }

    public static ProblemContext readFile(File file){
        BufferedReader lecteur;
        try {
            lecteur = new BufferedReader(new InputStreamReader( new FileInputStream(file.getAbsolutePath()), "UTF8"));
            String firstLine = lecteur.readLine();
            String[] values = firstLine.split("\\s+");
            int binLenght = Integer.parseInt(values[0]);
            int nbItems = Integer.parseInt(values[1]);
            Item[] items = new Item[nbItems];
            for(int i=0; i<nbItems; i++) {
                String itemRead = lecteur.readLine();
                int itemValue = Integer.parseInt(itemRead);
                items[i] = new Item(itemValue);
            }
            return new ProblemContext(binLenght, items);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrintWriter getWriter(String fileName) {
        PrintWriter writer = null;
        try {
        writer = new PrintWriter("/results/"+fileName + ".csv", "UTF-8");
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        }
        return writer;
    }
}