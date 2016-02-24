import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    //parameters, test different
    private int cores = 4;
    //private int size = 1000;
    private int size = (int) 1E8;//1E8;
    public static int mergeThreshold = (int)1E7;
    public static int quickThreshold = (int)1E6;
    private static boolean quick=true;
    private static boolean runQuick = quick;
    private static boolean runMerge = !quick;


    private ForkJoinPool pool;
    private float[] arr = new float[size];
    private float[] MergeArr = new float[size];
    private float[] QuickArr = new float[size];
    private Path fileMerge;
    private Path fileQuick;

    public static void main(String[] args) throws Exception {
        Main m =  new Main();

        if (runMerge) {
            for (int i = 0; i < 20; i++) {
                try {
                    m.merge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("-----------------------------------------------");
            //TODO test 1 and 2 and threshold -1
        }
        if (runQuick) {
            for (int i = 0; i < 20; i++) {
                try {
                    m.quick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //TODO test 1 and 2 and threshold -1

        }
    }

    public Main() throws Exception {
        fileMerge= Paths.get("testdata_merge.txt");
        writeMerge("result below, "+this.toString());

        fileQuick= Paths.get("testdata_quick.txt");
        writeQuick("result below, "+this.toString());


        Random rand = new Random();
        pool = new ForkJoinPool(cores);
        for (int i = 0; i < size; i++) {
            float val = rand.nextFloat();
            arr[i] = val;
        }
        if (runQuick) {
            this.quick();
            System.out.println("dry run done quick");
        }
        if (runMerge) {
            this.merge();
            System.out.println("dry run done merge");
        }
        int seq = 0;
        for (int i = 1; i < size; i++) {
            if (arr[i - 1] < arr[i]) {
                //System.out.println("sequential");
                seq++;
            }
        }
        System.out.println("found i-1 < i " + seq + " times in a array with the size " + size);
        System.out.println(pool.toString());

    }


    public void writeQuick(String str){
        try {
            Files.write(fileQuick , Arrays.<CharSequence>asList(str), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMerge(String str){
        try {
            Files.write(fileMerge, Arrays.<CharSequence>asList(str), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quick() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("morning ");


        System.arraycopy(arr, 0, QuickArr, 0, size);
        QuickSortTask quick = new QuickSortTask(QuickArr, (o1, o2) -> o1.compareTo(o2), 0, size - 1);

        long start = System.nanoTime();
        pool.invoke(quick);
        long elapsed = System.nanoTime() - start;

        float[] QuickArr = quick.getResult();

        for (int i = 1; i < size; i++) {
            if (QuickArr[i - 1] > QuickArr[i]) {
                System.out.print("error");
                float index1 = QuickArr[i - 1];
                float index2 = QuickArr[i];
                String str = "error i1:" + index1 + " i2:" + index2 + " i:" + i + " i1-i2:" + (index1 - index2);
                System.err.println(str);
                throw new Exception("not sorted " + str);
            }
        }
        writeQuick(elapsed+"");
        System.out.println(elapsed + " ns, quick success");
    }

    public void merge() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("morning ");

        System.arraycopy(arr, 0, MergeArr, 0, size);

        MergeSortTask merge = new MergeSortTask(MergeArr, 0, size - 1);

        long start = System.nanoTime();
        pool.invoke(merge);
        long elapsed = System.nanoTime() - start;

        MergeArr = merge.getResult();
        for (int i = 1; i < size; i++) {
            if (MergeArr[i - 1] > MergeArr[i]) {
                float index1 = MergeArr[i - 1];
                float index2 = MergeArr[i];
                String str = "error i1:" + index1 + " i2:" + index2 + " i:" + i + " i1-i2:" + (index1 - index2);
                System.err.println(str);
                throw new Exception("not sorted " + str);
            }
        }
        writeMerge(elapsed+"");
        System.out.println(elapsed + " ns, merge success");

    }

    @Override
    public String toString() {
        String str=", mergeSort";
        if(quick){
            str=", quickSort";
            if(runMerge){
                str=str+", mergeSort";
            }
        }
        return "Main{" +
                "cores=" + cores +
                ", size=" + size +
                ", mergeThreshold="+mergeThreshold+
                ", quickThreshold="+quickThreshold+
                str+ '}';
    }


}
