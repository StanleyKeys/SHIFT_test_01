import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class MergeSortApp {
    public static void main(String[] args) throws IOException {
        MergeSortApp msa = new MergeSortApp();

        String[] userArgs = {"-i", "-a", "out3.txt", "inn1.txt", "inn2.txt", "inn3.txt"};

        msa.chooseCommand(userArgs);


    }

    public String searchTypeCommand(String[] args) {
        /*
            Проверяем параметр типа данных.
            -i = int
            -s = str
         */
        for (String arg : args) {
            if (arg.equals("-i")) {
                return arg;
            } else if (arg.equals("-s")) {
                return arg;
            }
        }
        return null;
    }

    public String searchSortCommand(String[] args) {
        /*
            Проверяем параметр сортировки.
            -a = ascending
            -d = descending
         */
        for (String arg : args) {
            if (arg.contains("-d")) {
                return "-d";
            }
        }
        return "-a";
    }

    public void chooseCommand(String[] args) throws IOException {
        /*
            Основной метод, который:
            1. Создает списки для команд, и файлов.
            2. Заполняет commandList параметрами полученными от пользователя.
            3. Заполняет fileList названиями файлов.
            4. Проверяет ввел ли пользователь Выходной и Входные файлы.
            5. Проверяет наличие параметра для типа данных.
         */

        ArrayList<String> commandList = new ArrayList<>();
        ArrayList<String> fileList = new ArrayList<>();
        commandList.add(searchTypeCommand(args));
        commandList.add(searchSortCommand(args));


        for (String arg : args) {
            if (arg.contains(".txt")) {
                fileList.add(arg);
            }
        }
        if (fileList.size() == 0) {
            System.out.println("Необходимо ввести в параметрах название выходного файла. \nНапример: out.txt");
        } else if (fileList.size() == 1) {
            System.out.println("В параметрах должен быть хотя бы один входной файл. \nНапример: in.txt");
        } else {
            if (commandList.get(0) == null) {
                System.out.println("Вы не ввели параметр для типа данных. \nДопустимые: -i Integer, -s String");
            } else if (commandList.get(0).equals("-i")) {
                fillList(commandList.get(1), fileList);
            }
        }
    }

    public void fillList(String sortCommand, ArrayList<String> fileList) throws IOException {

        ArrayList<String> strList = new ArrayList<>();

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.printf("Max heap memory: %.2f GB%n",
                (double) memoryMXBean.getHeapMemoryUsage().getMax() / 1073741824);

        // Проверяем допустимое кол-во памяти, затем используем только часть его.
        double maxUseMemory = Math.ceil((((double) memoryMXBean.getHeapMemoryUsage().getMax() / 1073741824) / (fileList.size() - 1)) * 10);
        maxUseMemory = maxUseMemory / 10 - 0.5;

        System.out.println(maxUseMemory);

        for (int i = 1; i < fileList.size(); i++) {
            BufferedReader reader = new BufferedReader(new FileReader(fileList.get(i)));
            String line = reader.readLine();
            double usedMemory = (double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1073741824;
            System.out.printf("Used heap memory: %.2f GB%n", usedMemory);

            /*
                Если используемая память превышает допустимую (usedMemory), то программа прервыает чтение файлов,
                затем полученный список сортирует и записывает в Выходной файл.
                Освобождает память и снова читает файлы.
            */
            if (usedMemory >= maxUseMemory) {
                System.out.printf("Used Memory is higher than %,.1f GB \n", maxUseMemory);
                int[] array = mergeSort(strList, sortCommand);
                saveToFile(array, fileList.get(0));
                strList.clear();
                System.out.println("successfully cleared List");
            }
            while (line != null) {
                if (!line.contains(" ")) {
                    strList.add(line);
                    line = reader.readLine();
                }
            }

            System.out.printf("%s successfully saved in List \n", fileList.get(i));
            if (i == fileList.size() - 1) {
                int[] array = mergeSort(strList, sortCommand);
                saveToFile(array, fileList.get(0));
                strList.clear();
                System.out.println("finished the program");
            }

        }
    }


    public static int[] mergeSort(ArrayList<String> strList, String sortCommand) {
        int[] array = new int[strList.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(strList.get(i));
        }
        if (sortCommand.equals("-a")) {
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

            }
            return currentSrc;
        } else {
            Integer[] integerArray = IntStream.of(array).boxed().toArray(Integer[]::new);
            Arrays.sort(integerArray, Collections.reverseOrder());
            return Arrays.stream(integerArray).mapToInt(i -> i).toArray();
        }
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

    public void saveToFile(int[] array, String outFile) throws IOException {
        FileWriter writer = new FileWriter(outFile, true);
        for (int i = 0; i < array.length; i++) {
            writer.write(array[i] + System.getProperty("line.separator"));
        }
        writer.close();
        System.out.println("successfully saved the out file");

    }


}
