package password;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Количественная оценка стойкости парольной защиты
 */
public class VerifyPassword {

    /**
     * Размер английского алфавита для заглавных букв
     */
    private static final int ALPHABET_UPPERCASE_LETTERS = 26;

    /**
     * Размер английского алфавита для прописных букв
     */
    private static final int ALPHABET_LOWERCASE_LETTERS = 26;

    /**
     * Размер цифр от 0 до 9
     */
    private static final int DIGITS = 10;

    /**
     * V - скорость перебора паролей злоумышленника
     * 10 вариант - 10 паролей в минуту
     */
    private static final int V = 10;

    /**
     * T - максимальный срок действия пароля
     * 10 вариант - 1 неделя, переводим в минуты = 10080
     */
    private static final int T = 10080;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================================================================");
        System.out.println("Введите ваш пароль: ");
        String password = scanner.nextLine();

        int alphabetSize = calculateAlphabetSize(password);

        int passwordLength = password.length();
        System.out.println("Длина пароля: " + passwordLength);

        BigDecimal totalCombinations = BigDecimal.valueOf(alphabetSize).pow(passwordLength);
        System.out.println("Число возможных паролей: " + formatBigDecimal(totalCombinations));

        int attempts = V * T;
        System.out.println(attempts + " паролей может перебрать злоумышленник за " + T + " минут (1 неделя)");

        BigDecimal P = BigDecimal.valueOf(attempts)
                .divide(totalCombinations, 20, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        System.out.println("Вероятность подбора пароля: " + formatBigDecimal(P) + "%");

        if (P.compareTo(BigDecimal.valueOf(50)) < 0) { // 50%
            System.out.println("Пароль является надежным");
        } else {
            System.out.println("Пароль не является надежным");
        }
        System.out.println("=========================================================================================");

        scanner.close();
    }

    /**
     * Метод нужен для проверки содержит ли пароль заглавные буквы, прописные буквы и цифры
     */
    private static int calculateAlphabetSize(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigits = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isDigit(c)) hasDigits = true;
        }

        int size = 0;
        if (hasUppercase) size += ALPHABET_UPPERCASE_LETTERS;
        if (hasLowercase) size += ALPHABET_LOWERCASE_LETTERS;
        if (hasDigits) size += DIGITS;

        return size;
    }

    /**
     * Метод нужен для форматирования BigDecimal
     */
    private static String formatBigDecimal(BigDecimal value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(value);
    }
}
