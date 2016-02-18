import java.util.concurrent.RecursiveAction;

/**
 * Created by luben on 2/18/16.
 */
public class MergeSortTask extends RecursiveAction {
    private float[] numbers;
    private int left, right;

    public MergeSortTask(float[] arr, int a, int b) {
        numbers = arr;
        left = a;
        right = b;
    }

    @Override
    public void compute() {
        int mid;

        if (right > left) {
            mid = (right + left) / 2;
            //MergeSort_Recursive(numbers, left, mid);
            //MergeSort_Recursive(numbers, (mid + 1), right);
            MergeSortTask w1 = new MergeSortTask(numbers, left, mid);
            MergeSortTask w2 = new MergeSortTask(numbers, (mid + 1), right);
            w1.fork();
            w2.compute();
            w1.join();
            DoMerge(numbers, left, (mid + 1), right);
        }
    }

    public float[] getResult() {
        return numbers;
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
