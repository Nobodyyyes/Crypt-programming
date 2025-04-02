package HashSHA;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HashSHA {

    /**
     * Размер блока в байтах 64 (512 бит)
     */
    private final static int BLOCK_SIZE = 64;

    /**
     * Инициализация пяти 32-битовых переменных: a, b, c, d, c
     */
    private final static int[] H = {0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};

    /**
     * Раунды 20 по 39
     */
    private final static int K = 0x6ED9EBA1;

    public static String hashRounds20To39(String input) {
        byte[] paddedMessage = padMessage(input.getBytes(StandardCharsets.UTF_8));
        int[] H = HashSHA.H.clone();

        for (int i = 0; i < paddedMessage.length; i += BLOCK_SIZE) {
            processBlock(paddedMessage, i, H);
        }

        return toHex(H);
    }

    private static void processBlock(byte[] message, int offset, int[] H) {
        int[] w = new int[80];

        for (int i = 0; i < 16; i++) {
            w[i] = ByteBuffer.wrap(message, offset + i * 4, 4).getInt();
        }

        for (int i = 16; i < 80; i++) {
            w[i] = Integer.rotateLeft(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
        }

        int a = H[0], b = H[1], c = H[2], d = H[3], e = H[4];

        System.out.println("Раунд |     A      |     B      |     C      |     D      |     E      ");
        System.out.println("----------------------------------------------------------------------");

        for (int i = 20; i < 40; i++) {
            int xor = b ^ c ^ d;
            int temp = Integer.rotateLeft(a, 5) + xor + e + w[i] + K;
            e = d;
            d = c;
            c = Integer.rotateLeft(b, 30);
            b = a;
            a = temp;

            System.out.printf("  %2d  |  %08X  |  %08X  |  %08X  |  %08X  |  %08X %n", i, a, b, c, d, e);
        }

        System.out.println("======================================================================");

        H[0] = a;
        H[1] = b;
        H[2] = c;
        H[3] = d;
        H[4] = e;
    }

    private static byte[] padMessage(byte[] message) {
        int originalLength = message.length;
        int newLength = ((originalLength + 8) / BLOCK_SIZE + 1) * BLOCK_SIZE;
        byte[] padded = new byte[newLength];

        System.arraycopy(message, 0, padded, 0, originalLength);
        padded[originalLength] = (byte) 0x80;

        long bitLength = (long) originalLength * 8;
        ByteBuffer.wrap(padded, newLength - 8, 8).putLong(bitLength);

        return padded;
    }

    private static String toHex(int[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int h : hash) {
            hexString.append(String.format("%08X", h));
        }
        return hexString.toString().toLowerCase();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("======================================================================");
        System.out.println("Введите сообщение: ");
        String text = scanner.nextLine();
        System.out.println("======================================================================");
        System.out.println("Сумма хэша за раунды 20-39: " + hashRounds20To39(text));
        System.out.println("======================================================================");
    }
}
