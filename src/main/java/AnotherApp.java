import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;

public class AnotherApp {
    public static void main(String[] args) throws IOException {
        AnotherApp anotherApp = new AnotherApp();

        System.out.println(Arrays.toString(args));

        String[] userArgs = {"-i", "-d", "out2.txt", "inn1.txt", "inn2.txt", "inn3.txt"};
        anotherApp.chooseCommand(userArgs);
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
                fillList(fileList, commandList.get(1));
            }
        }
    }

    public void fillList(ArrayList<String> fileList, String sortCommand) throws IOException {
        /*
            1. Создаем список.
            2. Создаем переменную, которая будет проверять ограничение используемой памяти.
            3. Заполняем список и отправляем на запись в файл.
        */

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
                saveToFile(strList, sortCommand, fileList.get(0));
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
                saveToFile(strList, sortCommand, fileList.get(0));
                strList.clear();
                System.out.println("finished the program");
            }

        }
    }


    public ArrayList<Integer> sortTheList(ArrayList<String> strList, String command) {
        /*
            1. Получаем список и команду по сортировке.
            2. Конвертируем и сортируем (стандартными библиотеками).
        */
        System.out.println("Converting String to Integer");
        ArrayList<Integer> numList = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            int temp = Integer.parseInt(strList.get(i));
            numList.add(temp);
        }
        if (command.equals("-a")) {
            Collections.sort(numList);
        } else {
            numList.sort(Collections.reverseOrder());
        }
        return numList;
    }

    public void saveToFile(ArrayList<String> strList, String sortCommand, String outFile) throws IOException {
        /*
            1. Создаем список для чисел.
            2. Конвертируем из String в int и наоборот.

            P.S. Если сразу делать запись strList, то по непонятным причинам идет запись в кодировке UTF-16 (иероглифы),
            хотя везде стоит UTF-8.
        */

        ArrayList<Integer> numList = sortTheList(strList, sortCommand);

        FileWriter writer = new FileWriter(outFile, true);
        System.out.println("Saving out file");
        for (int value : numList) {
            writer.write(value + System.getProperty("line.separator"));
        }
        writer.close();
        numList.clear();
        System.out.println("successfully saved the out file");
    }


    public void fillFiles() throws IOException {
        /*
            Метод создания и заполения файлов БОЛЬШИМ количеством данных (чисел).
            Необходим для работы с оперативной памятью (проверки нагрузки).
        */
        FileWriter writer = new FileWriter("in1.txt", true);
        Random r = new Random();
        System.out.println("filling the file");
        for (int i = 0; i < 10000000; i++) {
            String s = Integer.toString(r.nextInt(100000));
            writer.write(s);
            writer.append("\n");
        }
        writer.close();
    }

    void showMemoryUsage() {
        /*
            Метод для проверки памяти.
        */
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        System.out.println(String.format("Initial memory: %.2f GB",
                (double) memoryMXBean.getHeapMemoryUsage().getInit() / 1073741824));

        System.out.println(String.format("Used heap memory: %.2f GB",

                (double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1073741824));

        System.out.println(String.format("Max heap memory: %.2f GB",

                (double) memoryMXBean.getHeapMemoryUsage().getMax() / 1073741824));

        System.out.println(String.format("Committed memory: %.2f GB",

                (double) memoryMXBean.getHeapMemoryUsage().getCommitted() / 1073741824));
    }
}
