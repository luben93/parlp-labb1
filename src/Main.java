import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World! cores avalible: ");
        Random rand = new Random();
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println(cores);
        int size = 50000;
        float[] QuickArr = new float[size];
        float[] MergeArr = new float[size];
        for (int i = 0; i < size; i++) {
            float val = rand.nextFloat();
            QuickArr[i] = val;
            MergeArr[i] = val;
        }
        QuickSortTask quick = new QuickSortTask(QuickArr, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        }, 0, size - 1);
        ForkJoinPool pool = new ForkJoinPool(cores);

        pool.invoke(quick);
        QuickArr = quick.getResult();// TODO reassasing quickarr
        /*
        quickSortInPlace(QuickArr, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        }, 0, size - 1);
        */
//        MergeSort_Recursive(MergeArr, 0, size - 1);
        MergeSortTask merge = new MergeSortTask(MergeArr, 0, size - 1);
        pool.invoke(merge);
        MergeArr = merge.getResult();

        for (int i = 1; i < size; i++) {
            if (QuickArr[i - 1] > QuickArr[i]) {
                System.out.print("error");
                return;
            }
            if (MergeArr[i - 1] > MergeArr[i]) {
                System.out.print("error");
                return;
            }

        }
        System.out.print("success");

    }
}