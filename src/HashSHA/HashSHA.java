package HashSHA;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HashSHA {

    private final static int BLOCK_SIZE = 64;

    private final static int[] H = {0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};

    private final static int K = 0x6ED9EBA1;

    public static int[] processBlock(byte[] block) {
        int[] w = new int[80];

        for (int i = 0; i < 16; i++) {
            w[i] = ByteBuffer.wrap(block, i * 4, 4).getInt();
        }

        for (int i = 16; i < 80; i++) {
            w[i] = Integer.rotateLeft(w[i - 3] ^ w[i - 8] ^ w[i - 14] ^ w[i - 16], 1);
        }

        int a = H[0], b = H[1], c = H[2], d = H[3], e = H[4];

        for (int i = 20; i < 40; i++) {
            int temp = Integer.rotateLeft(a, 5) + (b ^ c ^ d) + e + w[i] + K;
            e = d;
            d = c;
            c = Integer.rotateLeft(b, 30);
            b = a;
            a = temp;
        }

        return new int[]{a, b, c, d, e};
    }

    public static void main(String[] args) {
        String input = "Hello world";
        byte[] passedBlock = padMessage(input.getBytes(StandardCharsets.UTF_8));
        int[] hash = processBlock(passedBlock);

        System.out.printf("Hash after rounds 20-39: %08X %08X %08X %08X %08X\n",
                hash[0], hash[1], hash[2], hash[3], hash[4]);
    }

    private static byte[] padMessage(byte[] message) {
        int newLength = BLOCK_SIZE;
        byte[] padded = new byte[newLength];
        System.arraycopy(message, 0, padded, 0, message.length);
        padded[message.length] = (byte) 0x80;
        ByteBuffer.wrap(padded, newLength - 8, 8).putLong(message.length * 8);
        return padded;
    }
}
