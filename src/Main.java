import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private ForkJoinPool pool;
    private int size = 10000; // 50000000 MAX MEMORY SIZE
    private float[] arr=new float[size];
    private float[] MergeArr=new float[size];
    private float[] QuickArr=new float[size];

    public static void main(String[] args)  {
        Main m = null;
        try {
            m = new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            try {
                m.merge();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("-----------------------------------------------");
        for (int i = 0; i < 10; i++) {
            try {
                m.quick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public Main() throws Exception {
        Random rand = new Random();
        pool=new ForkJoinPool();
        for (int i = 0; i < size; i++) {
            float val = rand.nextFloat();
            arr[i] = val;
        }
        this.quick();
        System.out.println("dry run done quick");
        this.merge();
        System.out.println("dry run done merge");
        int seq=0;
        for (int i = 1; i < size; i++) {
            if (arr[i - 1] < arr[i]) {
                //System.out.println("sequential");
                seq++;
            }
        }
        System.out.println("found i-1 < i "+seq+" times in a array with the size "+size);
        System.out.println(pool.toString());

    }

    public void quick() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("morning ");


        System.arraycopy(arr,0,QuickArr,0,size);
        QuickSortTask quick = new QuickSortTask(QuickArr, new Comparator<Float>() {
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
                throw new Exception("not sorted");
            }
        }
        System.out.println(elapsed + " ns, quick success");
    }

    public void merge() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("morning ");

        System.arraycopy(arr,0,MergeArr,0,size);

        MergeSortTask merge = new MergeSortTask(MergeArr, 0, size - 1);

        long start = System.nanoTime();
        pool.invoke(merge);
        long elapsed = System.nanoTime() - start;

        MergeArr = merge.getResult();
        for (int i = 1; i < size; i++) {
            if (MergeArr[i - 1] > MergeArr[i]) {
                float index1=MergeArr[i - 1];
                float index2= MergeArr[i];
                String str="error i1:"+index1+" i2:"+ index2+ " i:"+i +" i1-i2:"+(index1-index2);
                System.err.println(str);
                throw new Exception("not sorted " +str);
            }
        }
        System.out.println(elapsed + " ns, merge success");

    }
}