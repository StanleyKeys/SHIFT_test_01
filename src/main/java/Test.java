import java.io.*;
import java.util.Arrays;


public class Test {
    public static void main(String[] args) {
        int[] a1 = new int[]{21, 23355345, 24, 40, 75, 76, 78, 77, 900, 2100, 2200, 2300, 2400, 2500};
        int[] a2 = new int[]{10, 11, 41, 50, 65, 86, 98, 101, 190, 1100, 1200, 3000, 5000};
        // mergeArrays(a1, a2);

        System.out.println(Arrays.toString(a1));
    }

    public static int[] mergeSort(int[] array) {
        int[] tmp;
        int[] currentSrc = array;
        int[] currentDest = new int[array.length];

        int size = 1;
        while (size < array.length) {
            for (int i = 0; i < array.length; i += 2 * size) {
                merge(currentSrc, i, currentSrc, i + size, currentDest, i, size);
            }

            tmp = currentSrc;
            currentSrc = currentDest;
            currentDest = tmp;

            size = size * 2;

            // System.out.println(Arrays.toString(currentSrc));
        }
        return currentSrc;
    }

    private static void merge(int[] src1, int src1Start, int[] src2, int src2Start, int[] dest,
                              int destStart, int size) {
        int index1 = src1Start;
        int index2 = src2Start;

        int src1End = Math.min(src1Start + size, src1.length);
        int src2End = Math.min(src2Start + size, src2.length);

        if (src1Start + size > src1.length) {
            for (int i = src1Start; i < src1End; i++) {
                dest[i] = src1[i];
            }
            return;
        }

        int iterationCount = src1End - src1Start + src2End - src2Start;

        for (int i = destStart; i < destStart + iterationCount; i++) {
            if (index1 < src1End && (index2 >= src2End || src1[index1] < src2[index2])) {
                dest[i] = src1[index1];
                index1++;
            } else {
                dest[i] = src2[index2];
                index2++;
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
