package labs5;

/**
 * Шифрование с использованием маршрутной перестановки
 * (текст записывается по горизонтали справа налево в матрицу 4 на 4,
 * шифрование по вертикальной змейке). Ключевое слово УГОЛ
 */
public class Cipher4To4 {

    private static final int MATRIX_SIZE = 4;

    private static final String ALPHABET = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЫЭЮЯ";

    public static void main(String[] args) {
        String text = "ПРИВЕТМИР";
        String key = "УГОЛ";

        String encryptedText = encrypt(text, key);
        System.out.println("Защифрованный текст: " + encryptedText);
    }

    private static String encrypt(String text, String key) {
        String keyword = prepareKey(key);

        char[][] matrix = new char[MATRIX_SIZE][MATRIX_SIZE];
        fillMatrix(matrix, text);

        StringBuilder encryptedText = new StringBuilder();
        for (int col = 0; col < MATRIX_SIZE; col++) {
            for (int row = 0; row < MATRIX_SIZE; row++) {
                encryptedText.append(matrix[row][col]);
            }
        }

        return encryptedText.toString();
    }

    private static String prepareKey(String key) {
        StringBuilder preparedKey = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (preparedKey.indexOf(String.valueOf(c)) == -1) {
                preparedKey.append(c);
            }
        }

        return preparedKey.toString();
    }

    private static void fillMatrix(char[][] matrix, String text) {
        int textIndex = 0;
        for (int row = 0; row < MATRIX_SIZE; row++) {
            for (int col = MATRIX_SIZE - 1; col >= 0; col--) {
                if (textIndex < text.length()) {
                    matrix[row][col] = text.charAt(textIndex++);
                } else {
                    matrix[row][col] = ' ';
                }
            }
        }
    }
}
