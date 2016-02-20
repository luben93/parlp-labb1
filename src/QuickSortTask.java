import java.util.Arrays;
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
    protected void compute() {//TODO use THRESHOLD
        if(a-b < THRESHOLD){
            Arrays.sort(S);
        }else {
            if (a >= b) return; // subarray is trivially sorted
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
            //quickSortInPlace(S, comp, a, left - 1);        // make recursive calls
            //quickSortInPlace(S, comp, left + 1, b);
            QuickSortTask worker1 = new QuickSortTask(S, comp, a, left - 1);
            QuickSortTask worker2 = new QuickSortTask(S, comp, left + 1, b);
            worker1.fork();
            worker2.compute();
            worker1.join();
        }
    }
}
