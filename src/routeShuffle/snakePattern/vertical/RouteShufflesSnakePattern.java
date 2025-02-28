package routeShuffle.snakePattern.vertical;

import java.util.Scanner;

/**
 * 10 - вариант
 * Шифрование с использованием маршрутной перестановки
 * (текст записывается по горизонтали справа налево в матрицу 4 на 4,
 * шифрование по вертикальной змейке). Ключевое слово УГОЛ
 */
public class RouteShufflesSnakePattern {

    /**
     * Размер строки (горизонтально)
     */
    private static final int ROW = 4;

    /**
     * Размер колонки (вертикально)
     */
    private static final int COLUMN = 4;

    /**
     * Таблица 4*4
     */
    private static final char[][] TABLE = new char[ROW][COLUMN];

    /**
     * Длина слова (учитывая таблицу 4*4)
     */
    private static final int LENGTH = 16;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=========================================================================================");
        System.out.println("Введите текст (16 символов или меньше): ");
        String input = scanner.nextLine();
        scanner.close();

        input = padOrTrim(input, LENGTH);

        fillTable(TABLE, input);

        System.out.println("=========================================================================================");
        System.out.println("Таблица: ");
        printTable(TABLE);

        System.out.println("Зашифрованные текст: ");
        String encryptedText = encryptBySnakePattern(TABLE);
        System.out.println(encryptedText);
        System.out.println("=========================================================================================");
    }

    /**
     * Метод нужен для проверки, содержит ли 16 символов или нет
     */
    private static String padOrTrim(String text, int length) {
        if (text.length() > length) {
            return text.substring(0, length);
        }
        return String.format("%-" + length + "s", text); // дополнение пробелами
    }

    /**
     * Метод нужен для заполнения таблицы справа налево
     */
    private static void fillTable(char[][] table, String input) {
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 3; col >= 0; col--) {
                table[row][col] = input.charAt(index++);
            }
        }
    }

    /**
     * Метод нужен для отображения таблицы
     */
    private static void printTable(char[][] table) {
        for (char[] row : table) {
            for (char ch : row) {
                System.out.print(ch + " ");
            }
            System.out.println();
        }
    }

    /**
     * Метод шифррует по вертикальной змейке
     */
    private static String encryptBySnakePattern(char[][] table) {
        StringBuilder encrypted = new StringBuilder();
        int row = 0, col = 3; // начинаем с правого вверхнего угла
        boolean movingDown = true; // флаг, что идем вниз

        while (col >= 0) {
            encrypted.append(table[row][col]);

            if (movingDown) {
                if (row < 3) {
                    row++; // двигаем вниз
                } else {
                    col--; // дошли до низа - идем влево
                    movingDown = false;
                }
            } else {
                if (row > 0) {
                    row--; // двигаемся вверх
                } else {
                    col--; // дошли до верха - идем влево
                    movingDown = true;
                }
            }
        }

        return encrypted.toString();
    }
}
