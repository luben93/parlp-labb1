import java.util.Comparator;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Random rand = new Random();
        int size = 5000000;
        float[] QuickArr = new float[size];
        float[] MergeArr = new float[size];
        for (int i = 0; i < size ; i++) {
            float val = rand.nextFloat();
            QuickArr[i]=val;
            MergeArr[i]=val;
        }
        quickSortInPlace(QuickArr, new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        }, 0, size - 1);
        MergeSort_Recursive(MergeArr, 0, size - 1);


        for (int i = 1; i < size; i++) {
           // System.out.println(que.pollFirst() + "   " + arr[i]);
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

    private static  void quickSortInPlace(float[] S, Comparator<Float> comp, int a, int b) {
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
        quickSortInPlace(S, comp, a, left - 1);        // make recursive calls
        quickSortInPlace(S, comp, left + 1, b);
    }

    static public void MergeSort_Recursive(float[] numbers, int left, int right) {
        int mid;

        if (right > left) {
            mid = (right + left) / 2;
            MergeSort_Recursive(numbers, left, mid);
            MergeSort_Recursive(numbers, (mid + 1), right);

            DoMerge(numbers, left, (mid + 1), right);
        }
    }

    static public void DoMerge(float[] numbers, int left, int mid, int right) {
        float[] temp = new float[numbers.length];
        int i, left_end, num_elements, tmp_pos;

        left_end = (mid - 1);
        tmp_pos = left;
        num_elements = (right - left + 1);

        while ((left <= left_end) && (mid <= right)) {
            if (numbers[left] <= numbers[mid])
                temp[tmp_pos++] = numbers[left++];
            else
                temp[tmp_pos++] = numbers[mid++];
        }

        while (left <= left_end)
            temp[tmp_pos++] = numbers[left++];

        while (mid <= right)
            temp[tmp_pos++] = numbers[mid++];

        for (i = 0; i < num_elements; i++) {
            numbers[right] = temp[right];
            right--;
        }
    }
}
