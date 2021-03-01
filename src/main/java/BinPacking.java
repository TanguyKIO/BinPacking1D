public class BinPacking {

    static int infBorne(int[] items, Bin bin) {
        double length = bin.getLength();
        double sum = 0;
        for (int i = 0; i < items.length; i++) {
            sum += items[i];
        }
        int borne = (int) Math.ceil(sum / length);
        return borne;
    }

    public static void main(String args[]) {
        Bin bin = new Bin(5);
        int[] items1 = new int[]{5, 5, 5, 5, 6};
        int[] items2 = new int[]{5, 5, 5, 5, 5};
        int[] items3 = new int[]{5, 5, 5, 5, 4};

        int result1 = infBorne(items1, bin);
        int result2 = infBorne(items2, bin);
        int result3 = infBorne(items3, bin);

        System.out.println(result1 + " " + result2 + " " + result3);
    }
}
