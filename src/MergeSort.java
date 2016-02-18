
public class MergeSort {
    public static void main(String[] args) {
        float[] a = {14, 41, 63, 12, 4, 53, 78, 24, 74, 32, 167, 36, 56, 95, 43};
        float[] c = {14, 41, 63, 12, 4, 53, 78, 24, 74, 32, 167, 36, 56, 95, 43};
        float[] b = mergeSort(a);
        MergeSort_Recursive(c, 0, c.length - 1);
        System.out.println("min   online");

        for (int i = 0; i < a.length; i++) {
            System.out.println(b[i] + "     " + c[i]);

        }
    }


    private static float[] mergeSort(float[] a) {
        float[] b, c;
        //int ci;
        if (a.length == 1) {
            return a;
        }
        b = new float[a.length / 2];
        c = new float[a.length-b.length];
        for (int i = 0; i < (a.length / 2); i++) {
            //System.out.println("b"+i);
            b[i] = a[i];
        }
        //int j = 0;
        for (int i = 0; i < a.length - c.length; i++) {
            //System.out.println("c"+i);
            c[i] = a[i+a.length/2];
        }
       // float[] b2 = mergeSort(b);
       // float[] c2 = mergeSort(c);
        return merge(mergeSort(b), mergeSort(c), a);
    }


    private static float[] merge(float[] a, float[] b, float[] c) {
        int indexa = 0, indexb = 0, indexc = 0;
        int n = a.length, m = b.length;
        while (indexa < n && indexb < m) {
            if (a[indexa] <= b[indexb]) {
                c[indexc++] = a[indexa];
                indexa++;
            } else {
                c[indexc++] = b[indexb];
                indexb++;
            }
        }
        while (indexa < n) {
            c[indexc++] = a[indexa];
            indexa++;
        }
        while (indexb < m) {
            c[indexc++] = b[indexb];
            indexb++;
        }
        return c;
    }
//*/
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
