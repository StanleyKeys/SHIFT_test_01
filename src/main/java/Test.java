
import java.util.Arrays;


public class Test {
    public static void main(String[] args) {
        int[] a1 = new int[]{21, 23355345, 24, 40, 75, 76, 78, 77, 900, 8, 123123, 2100, 2200, 2300, 15, 2400, 71, 2500};
        int[] a2 = new int[]{10, 11, 41, 50, 65, 86, 98, 101, 190, 1100, 1200, 3000, 5000};

        Test t = new Test();
        t.mergeSort(a1, 0, a1.length - 1);
        System.out.println(Arrays.toString(a1));

    }

    public void mergeSort(int[] array, int left, int right) {
        if (right <= left) return;
        int mid = (left + right) / 2;
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    void merge(int[] array, int left, int mid, int right) {
        // вычисляем длину
        int lengthLeft = mid - left + 1;
        int lengthRight = right - mid;

        // создаем временные подмассивы
        int[] leftArray = new int[lengthLeft];
        int[] rightArray = new int[lengthRight];

        // копируем отсортированные массивы во временные
        System.arraycopy(array, left, leftArray, 0, lengthLeft);
        for (int i = 0; i < lengthRight; i++)
            rightArray[i] = array[mid + i + 1];

        // итераторы содержат текущий индекс временного подмассива
        int leftIndex = 0;
        int rightIndex = 0;

        // копируем из leftArray и rightArray обратно в массив
        for (int i = left; i < right + 1; i++) {
            // если остаются нескопированные элементы в R и L, копируем минимальный
            if (leftIndex < lengthLeft && rightIndex < lengthRight) {
                if (leftArray[leftIndex] < rightArray[rightIndex]) {
                    array[i] = leftArray[leftIndex];
                    leftIndex++;
                } else {
                    array[i] = rightArray[rightIndex];
                    rightIndex++;
                }
            }
            // если все элементы были скопированы из rightArray, скопировать остальные из leftArray
            else if (leftIndex < lengthLeft) {
                array[i] = leftArray[leftIndex];
                leftIndex++;
            }
            // если все элементы были скопированы из leftArray, скопировать остальные из rightArray
            else if (rightIndex < lengthRight) {
                array[i] = rightArray[rightIndex];
                rightIndex++;
            }
        }
    }


    public static void mergeArrays(int[] array1, int[] array2) {

        int[] resultArray = new int[array1.length + array2.length];

        int i = 0, j = 0;
        for (int k = 0; k < resultArray.length; k++) {

            if (i > array1.length - 1) {
                int a = array2[j];
                resultArray[k] = a;
                j++;
            } else if (j > array2.length - 1) {
                int a = array1[i];
                resultArray[k] = a;
                i++;
            } else if (array1[i] < array2[j]) {
                int a = array1[i];
                resultArray[k] = a;
                i++;
            } else {
                int b = array2[j];
                resultArray[k] = b;
                j++;
            }
        }
        System.out.println(Arrays.toString(resultArray));
    }
}
