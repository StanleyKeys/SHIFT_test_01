public class Test {
    public static void main(String[] args) {
        String[] strArray = {"-i", "-a", "out.txt", "in.txt"};

        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i].contains(".txt")) {
                System.out.println(Boolean.TRUE);
            }
            else {
                System.out.println(Boolean.FALSE);
            }
        }
    }
}
