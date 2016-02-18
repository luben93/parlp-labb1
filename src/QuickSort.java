
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;
/**
 * created by luben
 */
public class QuickSort {

    public static void main(String[] args) {
        int size =500;
        ArrayDeque<Integer> que = new ArrayDeque<Integer>(size);
        Integer[] arr = new Integer[size];
        int v;
        for (int i = 0; i < size; i++) {
            v = (int) (Math.random() * 100) + 1;
            que.add(v);
            arr[i] = v;
        }

        quickSort(que, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        quickSortInPlace(arr,  new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        }, 0, size-1);
       // System.out.println(que.pollFirst()+"   "+arr[0]);
        for (int i = 1; i < size; i++) {
           // System.out.println(que.pollFirst()+"   "+arr[i]);
            if(arr[i-1]>arr[i]){
                System.out.print("error");
                return;
            }
        }
        System.out.print("success");
    }

    public static <K> void quickSort(Queue<K> S, Comparator<K> comp) {
        int n = S.size();
        if (n < 2) { return; } // queue is trivially sorted
        // divide
        K pivot = S.peek(); // using first as arbitrary pivot
        Queue<K> L = new ArrayDeque<K>();
        Queue<K> E = new ArrayDeque<K>();
        Queue<K> G = new ArrayDeque<K>();
        while (!S.isEmpty()) { // divide original into L, E, and G
            K element = S.poll();
            int c = comp.compare(element, pivot);
            if (c < 0) // element is less than pivot
                L.add(element);
            else if (c == 0) // element is equal to pivot
                E.add(element);
            else // element is greater than pivot
                G.add(element);
        }        // conquer
        quickSort(L, comp); // sort elements less than pivot
        quickSort(G, comp); // sort elements greater than pivot
        while (!L.isEmpty())        // concatenate results
            S.add(L.poll());
        while (!E.isEmpty())
            S.add(E.poll());
        while (!G.isEmpty())
            S.add(G.poll());
    }


    private static <K> void quickSortInPlace(K[] S, Comparator<K> comp, int a, int b) {
        if (a >= b) return; // subarray is trivially sorted
        int left = a;
        int right = b - 1;
        K pivot = S[b];
        K temp; // temp object used for swapping
        while (left <= right) {
            while (left <= right && comp.compare(S[left], pivot) < 0) left++;            // scan until reaching value equal or larger than pivot (or right marker)
            while (left <= right && comp.compare(S[right], pivot) > 0) right--;            // scan until reaching value equal or smaller than pivot (or left marker)
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
        quickSortInPlace(S, comp, a, left - 1);        // make recursive calls
        quickSortInPlace(S, comp, left + 1, b);
    }

}
