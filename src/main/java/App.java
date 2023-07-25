
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class App {
    public static void main(String[] args) throws IOException {
        App app = new App();
        System.out.println(Arrays.toString(args));

        // String userArgument = app.userInput();

        app.chooseCommand(args);

    }

    public String userInput() {
        /*
            Метод ввода параметров пользователем.
         */
        System.out.println("Input your command: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
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
            1. Создает списки для чисел, строк, команд, и файлов.
            2. Заполняет commandList параметрами полученными от пользователя.
            3. Заполняет fileList названиями файлов.
            4. Проверяет ввел ли пользователь Выходной и Входные файлы.
            5. Проверяет наличие параметра для типа данных.
            6. Заполняет необходимый список и сортирует его.
            7. Записывает список(или массив) в выходной файл.
         */
        ArrayList<String> strList;
        ArrayList<Integer> numList;
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
                numList = fillNumList(fileList);

                // Основное решение:
                if (commandList.get(1).equals("-a")) {
                    Collections.sort(numList);
                } else {
                    numList.sort(Collections.reverseOrder());
                }
                saveToFile(numList, fileList.get(0));
                System.out.println(numList);

                // Альтернативное решение:
//                fill_sort_save_Array(numList, fileList, commandList);

            } else if (commandList.get(0).equals("-s")) {
                strList = fillStrList(fileList);
                if (commandList.get(1).equals("-a")) {
                    Collections.sort(strList);
                } else {
                    strList.sort(Collections.reverseOrder());
                }
                saveToFile(strList, fileList.get(0));
                System.out.println(strList);
            }
        }
    }

    public ArrayList<Integer> fillNumList(ArrayList<String> fileList) throws IOException {
        /*
            Функция:
            1. Принимает список файлов *.txt и проходится циклом. (лучше абсолютный путь к файлу)
            2. Проверяет на наличие строк с пробелами.
            3. Проверяет символы на цифры.
            4. Возвращает список чисел.
         */
        ArrayList<Integer> numList = new ArrayList<>();
        for (int i = 1; i < fileList.size(); i++) {
            String filePath = fileList.get(i);
            //String filePath = String.format("T:\\Important\\Projects\\IdeaProjects\\SHIFT_test_01\\%s", fileList.get(i));
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                if (!line.contains(" ")) {
                    boolean b = isNumeric(line);
                    if (b) {
                        numList.add(Integer.parseInt(line));
                    }
                }
                line = reader.readLine();
            }
        }

        return numList;
    }

    public ArrayList<String> fillStrList(ArrayList<String> fileList) throws IOException {
        /*
            Функция:
            1. Принимает список файлов *.txt и проходится циклом. (лучше абсолютный путь к файлу)
            2. Проверяет на наличие строк с пробелами.
            3. Проверяет символы на строки.
            4. Возвращает список строк.
         */
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 1; i < fileList.size(); i++) {
            String filePath = fileList.get(i);
            // String filePath = String.format("T:\\Important\\Projects\\IdeaProjects\\SHIFT_test_01\\%s", fileList.get(i));
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                if (!line.contains(" ")) {
                    boolean b = isNumeric(line);
                    if (!b) {
                        strList.add(line);
                    }
                }
                line = reader.readLine();
            }
        }
        return strList;
    }

    public Boolean isNumeric(String s) {
        /*
            Метод проверяет символы в строке.
         */
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public <T> void saveToFile(ArrayList<T> list, String fileName) throws IOException {
        /*
            Функция записывает список любого типа в файл.
         */
        FileWriter writer = new FileWriter(fileName);
        for (T item : list) {
            writer.write(item + System.getProperty("line.separator"));
        }
        writer.close();
    }


    public void fill_sort_save_Array(ArrayList<Integer> numList, ArrayList<String> fileList, ArrayList<String> commandList) throws IOException {
        /*
            1. Функция преобразует список чисел в массив.
            2. Массив вручную сортируется пузырьковой сортировкой (BubbleSort).
            3. Полученный массив записывается в файл.
         */
        int[] tempArray = new int[numList.size()];
        if (commandList.get(1).equals("-a")) {
            for (int i = 0; i < numList.size(); i++) {
                tempArray[i] = numList.get(i);
            }
        } else {
            for (int i = numList.size() - 1; i >= 0; i--) {
                tempArray[i] = numList.get(i);
            }
        }
        BubbleSort(tempArray);
        String fileName = fileList.get(0);
        System.out.println(Arrays.toString(tempArray));
        FileWriter writer = new FileWriter(fileName);
        for (int item : tempArray) {
            writer.write(item + System.getProperty("line.separator"));
        }
        writer.close();
    }

    public void BubbleSort(int[] myArray) {
        /*
            Пузырьковая сортировка.
         */
        int n = myArray.length;
        int temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (myArray[j - 1] > myArray[j]) {
                    temp = myArray[j - 1];
                    myArray[j - 1] = myArray[j];
                    myArray[j] = temp;
                }
            }
        }
    }
}
