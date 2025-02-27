package password;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Количественная оценка стойкости парольной защиты
 */
public class CheckPassword {

    /**
     * Размер английского алфавита заглавных букв
     */
    private static final int ALPHABET_UPPERCASE_LETTERS = 26;

    /**
     * Размер английского алфавита прописных букв
     */
    private static final int ALPHABET_LOWERCASE_LETTERS = 26;

    /**
     * Размер цифр от 0 до 9
     */
    private static final int DIGITS = 10;

    /**
     * Скорость перебора паролей злоумышленника
     * 10 вариант - 10 паролей в минуту
     */
    private static final int ATTEMPTS_PER_MINUTE = 10;

    /**
     * Максимальный срок действия пароля
     * 10 вариант - 1 неделя, переводим в минуты = 10080
     */
    private static final int TIME_IN_MINUTES = 10080;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите ваш пароль: ");
        String password = scanner.nextLine();

        int alphabetSize = calculateAlphabetSize(password);

        int passwordLength = password.length();
        System.out.println("Длина пароля: " + passwordLength);

        BigDecimal totalCombinations = BigDecimal.valueOf(alphabetSize).pow(passwordLength);
        System.out.println("Число возможных паролей: " + formatBigDecimal(totalCombinations));

        int attempts = ATTEMPTS_PER_MINUTE * TIME_IN_MINUTES;
        System.out.println(attempts + " паролей может проверить злоумышленник за " + TIME_IN_MINUTES + " минут");

        BigDecimal probability = BigDecimal.valueOf(attempts)
                .divide(totalCombinations, 20, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        System.out.println("Вероятность подбора пароля: " + formatBigDecimal(probability) + "%");

        if (probability.compareTo(BigDecimal.valueOf(50)) < 0) {
            System.out.println("Пароль является надежным");
        } else {
            System.out.println("Пароль не является надежным");
        }

        scanner.close();
    }

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

    private static String formatBigDecimal(BigDecimal value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(value);
    }
}
