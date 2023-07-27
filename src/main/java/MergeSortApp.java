import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


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

    private void chooseCommand(String[] args) throws IOException {
        /*
            Основной метод, который:
            1. Создает списки для команд, и файлов.
            2. Заполняет commandList параметрами полученными от пользователя.
            3. Заполняет fileList названиями файлов.
            4. Проверяет, ввел ли пользователь Выходной и Входные файлы.
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
            } else if (commandList.get(0).equals("-s")) {
                System.out.println("Sorry. This feature in development");
            }
        }
    }

    private void fillList(String sortCommand, ArrayList<String> fileList) throws IOException {

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
                Если используемая память превышает допустимую (usedMemory), то программа прерывает чтение файлов,
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


    private int[] mergeSort(ArrayList<String> strList, String sortCommand) {
        /*
            1. Получаем список, создаем массив и заполняем его.
            2. Проверяем команду сортировки.
            3. Сортируем слиянием.
        */
        int[] array = new int[strList.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(strList.get(i));
        }
        if (sortCommand.equals("-a")) {
            int[] temp;
            int[] sourceArray = array;
            int[] destArray = new int[array.length];

            int size = 1;
            while (size < array.length) {
                for (int i = 0; i < array.length; i += 2 * size) {
                    merge(sourceArray, i, sourceArray, i + size, destArray, i, size);
                }

                temp = sourceArray;
                sourceArray = destArray;
                destArray = temp;

                size = size * 2;

            }
            return sourceArray;
        } else {
/*
              Integer[] integerArray = IntStream.of(array).boxed().toArray(Integer[]::new);
              Arrays.sort(integerArray, Collections.reverseOrder());
              return Arrays.stream(integerArray).mapToInt(i -> i).toArray();
*/
            return Arrays.stream(array).boxed().sorted(Collections.reverseOrder()).mapToInt(Integer::intValue)
                    .toArray();
        }
    }

    private void merge(int[] sourceArray1, int source1Start, int[] sourceArray2,
                       int source2Start, int[] destArray, int destStart, int size) {
        int index1 = source1Start;
        int index2 = source2Start;

        int source1End = Math.min(source1Start + size, sourceArray1.length);
        int source2End = Math.min(source2Start + size, sourceArray2.length);

        if (source1Start + size > sourceArray1.length) {
            for (int i = source1Start; i < source1End; i++) {
                destArray[i] = sourceArray1[i];
            }
            return;
        }

        int iterationCount = source1End - source1Start + source2End - source2Start;

        for (int i = destStart; i < destStart + iterationCount; i++) {
            if (index1 < source1End && (index2 >= source2End || sourceArray1[index1] < sourceArray2[index2])) {
                destArray[i] = sourceArray1[index1];
                index1++;
            } else {
                destArray[i] = sourceArray2[index2];
                index2++;
            }
        }
    }

    public void saveToFile(int[] array, String outFile) throws IOException {
        FileWriter writer = new FileWriter(outFile, true);
        for (int value : array) {
            writer.write(value + System.getProperty("line.separator"));
        }
        writer.close();
        System.out.println("successfully saved the out file");

    }


}
