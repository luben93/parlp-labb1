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
    //private int cores = 4;
    //private int size = 1000;
    private int size = (int) 1E8;//1E8;
    public static int mergeThreshold = (int) 1E7;
    public static int quickThreshold = (int) 1E5;
    private static boolean quick = true;
    private static boolean all = false;
    private static boolean runQuick = quick;
    private static boolean runMerge = true;
    private static boolean parl = false;
    private static boolean Asort=false;


    private static ForkJoinPool pool = new ForkJoinPool();
    private float[] arr = new float[size];
    private float[] MergeArr = new float[size];
    private float[] QuickArr = new float[size];
    private float[] SortArr = new float[size];
    private Path fileMerge, fileQuick, fileSort;

    /**
     * run with:
     * /usr/lib/jvm/java-8-openjdk/bin/java -Xmx8000m -Didea.launcher.port=7540 -Didea.launcher.bin.path=/usr/share/intellij-idea-ultimate-edition/bin -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/java-8-openjdk/jre/lib/charsets.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/jaccess.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/nashorn.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/sunec.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/jce.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/jsse.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/management-agent.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/resources.jar:/usr/lib/jvm/java-8-openjdk/jre/lib/rt.jar:/home/luben/Code/KTH/funksprak/parlp/labb1/out/production/labb1:/usr/share/intellij-idea-ultimate-edition/lib/idea_rt.jar com.intellij.rt.execution.application.AppMain Main
     *
     * @param m
     * @throws Exception
     */

    public static void mainRunner(Main m) throws Exception {
//        Main m = new Main();

        if (all) {


            pool = new ForkJoinPool(1);
            if (runMerge) {
                m.writeMerge("result below, " + m.toString());


                for (int i = 0; i < 21; i++) {
                    try {
                        m.merge();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("-----------------------------------------------");
            if (runQuick) {
                m.writeQuick("result below, " + m.toString());

                for (int i = 0; i < 21; i++) {
                    try {
                        m.quick();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("-----------------------------------------------");

            pool = new ForkJoinPool(2);
            if (runQuick) {
                m.writeQuick("result below, " + m.toString());

                for (int i = 0; i < 21; i++) {
                    try {
                        m.quick();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("-----------------------------------------------");

            if (runMerge) {
                m.writeMerge("result below, " + m.toString());

                for (int i = 0; i < 21; i++) {
                    try {
                        m.merge();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("-----------------------------------------------");
            }
        }
        pool = new ForkJoinPool(4);

        if (runMerge) {
            m.writeMerge("result below, " + m.toString());

            for (int i = 0; i < 21; i++) {
                try {
                    m.merge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("-----------------------------------------------");
        if (runQuick) {
            m.writeQuick("result below, " + m.toString());

            for (int i = 0; i < 21; i++) {
                try {
                    m.quick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("-----------------------------------------------");


    }

    public static void main(String[] args) {
        try {
            Main m = new Main();
            if(Asort) {
                m.writeSort(m.toString());
                for (int i = 0; i < 21; i++) {
                    m.sort();
                }

                System.out.println("-----------------------------------------------");
                parl = true;
                m.writeSort(m.toString());

                for (int i = 0; i < 21; i++) {
                    m.sort();
                }

                System.out.println("-----------------------------------------------");
            }
            for (int i = 100; i < 1000000; i = i * 5) {
                quickThreshold = i;
                mergeThreshold = i;
                mainRunner(m);
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Main() throws Exception {
        fileMerge = Paths.get("testdata_merge.txt");
        //writeMerge("result below, " + this.toString());

        fileQuick = Paths.get("testdata_quick.txt");
        //writeQuick("result below, " + this.toString());
        fileSort = Paths.get("testdata_sort.txt");

        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            float val = rand.nextFloat();
            arr[i] = val;
        }


    }


    public void writeSort(String str) {
        System.out.print(str);

        try {
            Files.write(fileSort, Arrays.<CharSequence>asList(str), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeQuick(String str) {
        System.out.print(str);

        try {
            Files.write(fileQuick, Arrays.<CharSequence>asList(str), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMerge(String str) {
        System.out.print(str);

        try {
            Files.write(fileMerge, Arrays.<CharSequence>asList(str), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quick() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("quick ");


        System.arraycopy(arr, 0, QuickArr, 0, size);
        QuickSortTask quick = new QuickSortTask(QuickArr, (o1, o2) -> o1.compareTo(o2), 0, size - 1);

        long start = System.nanoTime();
        pool.invoke(quick);
        long elapsed = System.nanoTime() - start;

        float[] QuickArr = quick.getResult();

        for (int i = 1; i < size; i++) {
            if (QuickArr[i - 1] > QuickArr[i]) {
                float index1 = QuickArr[i - 1];
                float index2 = QuickArr[i];
                String str = "error i1:" + index1 + " i2:" + index2 + " i:" + i + " i1-i2:" + (index1 - index2);
                System.err.println(str);
                throw new Exception("not sorted " + str);
            }
        }
        writeQuick(elapsed + "");
        System.out.println(" ns, quick success");

    }

    public void merge() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("merge ");

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
        writeMerge(elapsed + "");
        System.out.println(" ns, merge success");

    }


    public void sort() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("sort ");

        System.arraycopy(arr, 0, SortArr, 0, size);

//        MergeSortTask merge = new MergeSortTask(MergeArr, 0, size - 1);

        long start = System.nanoTime();
        //      pool.invoke(merge);
        if (!parl) {
            Arrays.sort(SortArr);
        } else {
            Arrays.parallelSort(SortArr);
        }
        long elapsed = System.nanoTime() - start;

        //MaergeArr = merge.getResult();
        for (int i = 1; i < size; i++) {
            if (SortArr[i - 1] > SortArr[i]) {
                float index1 = SortArr[i - 1];
                float index2 = SortArr[i];
                String str = "error i1:" + index1 + " i2:" + index2 + " i:" + i + " i1-i2:" + (index1 - index2);
                System.err.println(str);
                throw new Exception("not sorted " + str);
            }
        }
        writeSort(elapsed + "");
        System.out.println(" ns, sort success");

    }

    @Override
    public String toString() {
        String str = ", mergeSort";
        if (quick) {
            str = ", quickSort";
            if (runMerge) {
                str = str + ", mergeSort";
            }
            if (parl) {
                str = str + ", parllell";

            } else {
                str = str + ", seriell";
            }
        }
        return "Main{" +
                "cores=" + pool.getParallelism() +
                ", size=" + size +
                ", mergeThreshold=" + mergeThreshold +
                ", quickThreshold=" + quickThreshold +
                str + '}';
    }


}
