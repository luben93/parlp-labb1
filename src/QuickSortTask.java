import java.util.Comparator;
import java.util.concurrent.RecursiveAction;

/**
 * Created by luben on 2/18/16.
 */


public class QuickSortTask extends RecursiveAction {

    public static final int THRESHOLD = Main.quickThreshold;
    private float[] S;
    private Comparator<Float> comp;
    private int a, b;

    public float[] getResult() {
        return S;
    }

    public QuickSortTask(float[] s, Comparator<Float> c, int start, int stop) {
        S = s;
        comp = c;
        a = start;
        b = stop;

    }

    @Override
    protected void compute() {
        if(b-a < THRESHOLD){
            sort(S,a,b);
        }else {
            int left = sort(S,comp,a,b,false);

            QuickSortTask worker1 = new QuickSortTask(S, comp, a, left - 1);
            QuickSortTask worker2 = new QuickSortTask(S, comp, left + 1, b);
            worker1.fork();
            worker2.compute();
            worker1.join();
        }
    }

    public static void sort(float[] S,int a,int b){
        sort(S, (o1, o2) -> o1.compareTo(o2),a,b,true);
    }

    private static int sort(float[] S,Comparator<Float> comp,int a,int b,boolean recuse){
        if (a >= b) return 0; // subarray is trivially sorted
        int left = a;
        int right = b - 1;
        float pivot = S[b];
        float temp; // temp object used for swapping
        while (left <= right) {
            while (left <= right && comp.compare(S[left], pivot) < 0)
                left++;            // scan until reaching value equal or larger than pivot (or right marker)
            while (left <= right && comp.compare(S[right], pivot) > 0)
                right--;            // scan until reaching value equal or smaller than pivot (or left marker)
            if (left <= right) { // indices did not strictly cross
                temp = S[left];                // so swap values and shrink range
                S[left] = S[right];
                S[right] = temp;
                left++;
                right--;
            }
        }
        temp = S[left];        // put pivot into its final place (currently marked by left index)
        S[left] = S[b];
        S[b] = temp;
        if(recuse){
            sort(S, comp, a, left - 1,true);
            sort(S, comp, left + 1, b,true);
        }
        return left;
    }

}
