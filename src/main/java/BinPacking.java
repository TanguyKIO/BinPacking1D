public class BinPacking{

    static int infBorne (int[] items, Bin bin){
        int length = bin.getLength();
        int sum = 0;
        for(int i =0; i<items.length; i++){
            sum+=items[i];
        }
        float borne = sum/length;
        return borne;
    }

    public static void main(String args[]){

    }

}
