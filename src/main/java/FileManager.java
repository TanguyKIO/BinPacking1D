public class FileManager{
    private String repertory = "/data/";
    private File[] files;

    public FileManager(){
        File file = new File(repertory);
        this.files = file.listFiles()
    }

    public Package readFile(file){
        BufferReader lecteur;
        lecteur = new BufferedReader(new InputStreamReader( new FileInputStream(chemin), "UTF8"));
        firsLine = lecteur.readLine();
        String[] values = firstLine.split('\\s+');
        int lenght = Integer.paseInt(values[0]);
        Bin bin = new Bin(lenght);
        int nbItems = Integer.paseInt(values[1]);
        Item[] items = new Item[];
        for(int i=0; i<nbItems; i++) {
            String itemRead = lecteur.readLine();
            int firstValue = Integer.paseInt(firstValu);
            items.append(firsValue);
        }
        return new Package(bin, items);
    }

    public PrintWriter getWriter(String fileName) {
        PrintWriter writer = null;
        try {
        writer = new PrintWriter("fileName" + ".csv", "UTF-8");
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        }
        return writer;
    }
}