package labs4;

import java.util.Scanner;

/**
 * Реализовать замену символов по первому правилу
 * Шифра Плейффера для английского алфавита (размер таблицы 5*5)
 */
public class Cipher {
    private static final char[][] TABLE = new char[5][5];

    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ключевое слово (key): ");
        String key = scanner.nextLine().toUpperCase();

        System.out.println("Введите текст для шифрования: ");
        String text = scanner.nextLine().toUpperCase();

        generateTable(key);
        printTable();

        String encrypted = encrypt(text);
        System.out.println("Зашифрованный текст: " + encrypted);
    }

    private static void generateTable(String key) {
        StringBuilder used = new StringBuilder();
        String keyString = (key + ALPHABET).toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");

        int index = 0;
        for (char c : keyString.toCharArray()) {
            if (used.indexOf(String.valueOf(c)) == -1) {
                TABLE[index / 5][index % 5] = c;
                used.append(c);
                index++;
            }
        }
    }

    private static void printTable() {
        System.out.println("Ключевая таблица:");
        for (char[] row : TABLE) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private static String encrypt(String text) {
        text = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");

        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int[] pos = findPosition(c);
            int row = pos[0];
            int col = (pos[1] + 1) % 5;

            result.append(TABLE[row][col]);
        }
        return result.toString();
    }

    private static int[] findPosition(char c) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (TABLE[row][col] == c) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
}
