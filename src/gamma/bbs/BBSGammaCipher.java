package gamma.bbs;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class BBSGammaCipher {

    /**
     * Простое число p
     */
    private final BigInteger p;

    /**
     * Простое число q
     */
    private final BigInteger q;

    /**
     * Число Блюма, M = p * q
     */
    private final BigInteger M;

    /**
     * Стартовое число генератора
     */
    private BigInteger state;

    public BBSGammaCipher(BigInteger p, BigInteger q, BigInteger seed) {
        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) ||
                !q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            throw new IllegalArgumentException("p и q должны быть ≡ 3 mod 4");
        }

        this.p = p;
        this.q = q;
        this.M = p.multiply(q);

        if (!seed.gcd(M).equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("seed должно быть взаимно простым с m");
        }

        this.state = seed.modPow(BigInteger.TWO, M);
    }

    private int getLowBit() {
        state = state.modPow(BigInteger.TWO, M);

        String binary = String.format("%8s", state.toString(2)).replace(' ', '0');
        int lowBit = state.testBit(0) ? 1 : 0;

        System.out.println("Текущее число (state): " + state);
        System.out.println("Двоичное представление: " + binary);
        System.out.println("Младший бит: " + lowBit);
        System.out.println("=================================================");

        return lowBit;
    }

    private byte[] generateGamma(int length) {
        byte[] gamma = new byte[length];
        for (int i = 0; i < length; i++) {
            int b = 0;
            for (int j = 0; j < 8; j++) {
                b = (b << 1) | getLowBit();
            }
            gamma[i] = (byte) b;
        }
        return gamma;
    }

    public byte[] encrypt(byte[] message) {
        byte[] gamma = generateGamma(message.length);
        byte[] result = new byte[message.length];

        System.out.println("===== Пошаговое шифрование =====");
        for (int i = 0; i < message.length; i++) {
            byte original = message[i];
            byte gammaByte = gamma[i];
            byte encrypted = (byte) (original ^ gammaByte);

            System.out.printf("Символ: '%c': %8s\n", (char) original, String.format("%8s", Integer.toBinaryString(original & 0xFF)).replace(' ', '0'));
            System.out.printf("Гамма байт: %8s\n", String.format("%8s", Integer.toBinaryString(gammaByte & 0xFF)).replace(' ', '0'));
            System.out.printf("XOR результат: %8s\n", String.format("%8s", Integer.toBinaryString(encrypted & 0xFF)).replace(' ', '0'));

            System.out.println("-----------------------------");

            result[i] = encrypted;
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите текст для шифрования: ");
        String input = scanner.nextLine();

        System.out.println("Введите простое число (например 383): ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.println("Введите простое число (например 503): ");
        BigInteger q = new BigInteger(scanner.nextLine());

        System.out.println("Введите seed (например 847): ");
        BigInteger seed = new BigInteger(scanner.nextLine());

        BBSGammaCipher cipher = new BBSGammaCipher(p, q, seed);

        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = cipher.encrypt(inputBytes);

        System.out.println("Изначальный текст: " + input);
        System.out.print("Зашифрованный текст (в hex): ");
        for (byte b : encrypted) {
            System.out.printf("%02X ", b);
        }

        System.out.println();

        System.out.print("Зашифрованный текст (в десятичном виде): ");
        for (byte b : encrypted) {
            System.out.print((b & 0xFF) + " ");
        }

        System.out.println();

        System.out.print("Зашифрованный текст (в двоичном виде): ");
        for (byte b : encrypted) {
            System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0') + " ");
        }

        System.out.println();
        System.out.println("-----------------------------");
    }
}
