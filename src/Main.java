import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static int size = (int) 1E8;//1E8;
    public static int mergeThreshold ;//= (int) 1E7;
    public static int quickThreshold ;//= (int) 1E5;
    private static boolean quick = true;//ture
    private static boolean all = true;//ture
    private static boolean runQuick = quick;
    private static boolean runMerge = true;
    private static boolean parl = false;
    private static boolean Asort = true;//ture
    private static int start = 10;
    private static int stop = size;

    private static ArrayList<Long> mergeElapsed = new ArrayList<>();
    private static ArrayList<Long> quickElapsed = new ArrayList<>();


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


    public static void bigRunner(Main m, int core) {
        pool = new ForkJoinPool(core);
        if (runQuick) {
            m.writeQuick("result below, " + m.toString());

            for (int i = 0; i < 21; i++) {
                try {
                    quickElapsed.add(m.quick());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            quickElapsed.remove(0);

        }
        System.out.println("-----------------------------------------------");

        if (runMerge) {
            m.writeMerge("result below, " + m.toString());

            for (int i = 0; i < 21; i++) {
                try {
                    mergeElapsed.add(m.merge());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mergeElapsed.remove(0);

        }
        System.out.println("-----------------------------------------------");
        if (core < 4) {
            bigRunner(m, core + 1);
        }
    }

    public static void main(String[] args) {
        try {

            Main m = new Main();
            m.writeSort("Start new test");
            m.writeQuick("Start new test");
            m.writeMerge("Start new test");
            if (Asort) {
                System.out.println("Arrays.sort");
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
            int bestMerge = 0, bestQuick = 0;
            Double bestElapsedMerge = Double.MAX_VALUE;
            Double bestElapsedQuick = bestElapsedMerge;
            for (; start < stop; start = start * 10) {
                quickThreshold = start;
                mergeThreshold = start;
                bigRunner(m, 4);
                Double tmp =mergeElapsed.stream().mapToLong(Long::longValue).average().orElse(Long.MAX_VALUE);
                System.out.println("new merge:  "+tmp);
                System.out.println("best merge: "+bestElapsedMerge);
                if (tmp < bestElapsedMerge) {
                    bestMerge = start;
                    bestElapsedMerge=tmp;
                    System.out.println("new best merge" + start);
                }

                tmp = quickElapsed.stream().mapToLong(Long::longValue).average().orElse(Long.MAX_VALUE);
                if (tmp < bestElapsedQuick) {
                    bestQuick = start;
                    bestElapsedQuick=tmp;
                    System.out.println("new best quick" + start);

                }
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            }
            quickThreshold = bestQuick;
            mergeThreshold = bestMerge;
            System.out.println("new best merge=" + mergeThreshold+" quick= "+quickThreshold);
            System.out.println("/////////////////////////////////////////////////////");
            if (all) {
                bigRunner(m, 1);
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

    public long quick() throws Exception {
        System.gc();
        Thread.sleep(3000);
        System.out.print("quick ");


        System.arraycopy(arr, 0, QuickArr, 0, size);
        QuickSortTask quick = new QuickSortTask(QuickArr, Float::compareTo, 0, size - 1);

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
        return elapsed;

    }

    public long merge() throws Exception {
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
        return elapsed;

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
