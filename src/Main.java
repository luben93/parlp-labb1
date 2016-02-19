import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private ForkJoinPool pool=new ForkJoinPool();
    private int size = 50000; // 50000000 MAX MEMORY SIZE
    private float[] arr=new float[size];

    public static void main(String[] args) throws InterruptedException {
        Main m = new Main();
        for (int i = 0; i < 10; i++) {
            m.quick();
        }
        System.out.println("-----------------------------------------------");
        for (int i = 0; i < 10; i++) {
            m.merge();
        }
    }

    public Main() throws InterruptedException {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            float val = rand.nextFloat();
            arr[i] = val;
        }
        this.quick();
        System.out.println("dry run done quick");
        this.merge();
        System.out.println("dry run done merge");
    }

    public void quick() throws InterruptedException {
        System.gc();
        Thread.sleep(5000);


        QuickSortTask quick = new QuickSortTask(arr, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        }, 0, size - 1);

        long start = System.nanoTime();
        pool.invoke(quick);
        long elapsed = System.nanoTime() - start;

        float[] QuickArr = quick.getResult();

        for (int i = 1; i < size; i++) {
            if (QuickArr[i - 1] > QuickArr[i]) {
                System.out.print("error");
                return;
            }
        }
        System.out.println(elapsed + " ns, quick success");
    }

    public void merge() throws InterruptedException {
        System.gc();
        Thread.sleep(5000);

        MergeSortTask merge = new MergeSortTask(arr, 0, size - 1);

        long start = System.nanoTime();
        pool.invoke(merge);
        long elapsed = System.nanoTime() - start;

        float[] MergeArr = merge.getResult();
        for (int i = 1; i < size; i++) {
            if (MergeArr[i - 1] > MergeArr[i]) {
                System.out.print("error");
                return;
            }
        }
        System.out.println(elapsed + " ns, merge success");

    }
}