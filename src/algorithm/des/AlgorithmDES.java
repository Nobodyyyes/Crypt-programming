package algorithm.des;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AlgorithmDES {

    /**
     * Матрица начальной перестановки IP
     */
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    /**
     * Матрица обратной перестановки IP -1
     */
    private static final int[] IP1 = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    /**
     * Е - блок
     */
    private static final int[] E_BLOCK = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    /**
     * S - блок
     */
    private static final int[][][] S_BLOCK = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    /**
     * P - блок
     */
    private static final int[] P_BLOCK = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    /**
     * Матрица G первоначальной подготовки ключа (формирование ключа)
     */
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    /**
     * Матрица H завершающей обработки ключа
     */
    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    /**
     * Таблица сдвигов для ключа
     */
    private static final int[] SHIFTS = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    public int[] encrypt(int[] block, List<int[]> roundKeys) {
        int[] permuted = permute(block, IP);
        int[] left = Arrays.copyOfRange(permuted, 0, 32);
        int[] right = Arrays.copyOfRange(permuted, 32, 64);

        for (int round = 0; round < 16; round++) {
            int[] expandedRight = permute(right, E_BLOCK);
            int[] xorWithKey = xor(expandedRight, roundKeys.get(round));
            int[] substituted = substitute(xorWithKey);
            int[] permutedS = permute(substituted, P_BLOCK);
            int[] newRight = xor(left, permutedS);
            left = right;
            right = newRight;
        }

        int[] combined = concat(right, left);
        return permute(combined, IP1);
    }

    public List<int[]> generateRoundKeys(int[] key) {
        int[] permutedKey = permute(key, PC1);
        int[] C = Arrays.copyOfRange(permutedKey, 0, 28);
        int[] D = Arrays.copyOfRange(permutedKey, 28, 56);
        List<int[]> roundKeys = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            C = leftShift(C, SHIFTS[i]);
            D = leftShift(D, SHIFTS[i]);
            int[] combined = concat(C, D);
            int[] roundKey = permute(combined, PC2);
            roundKeys.add(roundKey);
        }

        return roundKeys;
    }

    public String encryptMessage(String message, String keyString) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        System.out.println("Байты сообщения: " + Arrays.toString(messageBytes));

        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        System.out.println("Байты ключа: " + Arrays.toString(keyBytes));

        int[] keyBits = bytesToBits(Arrays.copyOf(keyBytes, 8));
        System.out.println("Биты ключа: " + Arrays.toString(keyBits));

        List<int[]> roundKeys = generateRoundKeys(keyBits);
        int roundCount = 1;
        System.out.println("=======================================================");
        for (int[] roundKey : roundKeys) {
            System.out.println("Раунд: " + roundCount + ": " + Arrays.toString(roundKey));
            roundCount++;
        }
        System.out.println("=======================================================");

        List<byte[]> encryptedBlocks = new ArrayList<>();
        for (int i = 0; i < messageBytes.length; i += 8) {
            byte[] block = Arrays.copyOfRange(messageBytes, i, Math.min(i + 8, messageBytes.length));
            if (block.length < 8) block = Arrays.copyOf(block, 8);
            int[] blockBits = bytesToBits(block);
            int[] encryptedBits = encrypt(blockBits, roundKeys);
            encryptedBlocks.add(bitsToBytes(encryptedBits));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (byte[] block : encryptedBlocks) {
            try {
                outputStream.write(block);

                String base64Block = Base64.getEncoder().encodeToString(block);

                StringBuilder hex = new StringBuilder();
                for (byte b : block) {
                    hex.append(String.format("%02X ", b));
                }

                StringBuilder bin = new StringBuilder();
                for (byte b : block) {
                    bin.append(
                            String.format("%8s", Integer.toBinaryString(b & 0xFF))
                                    .replace(' ', '0')
                    ).append(" ");
                }

                System.out.println("Base64: " + base64Block);
                System.out.println("HEX   : " + hex);
                System.out.println("BIN   : " + bin);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private int[] permute(int[] input, int[] table) {
        int[] output = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            output[i] = input[table[i] - 1];
        }
        return output;
    }

    private int[] xor(int[] a, int[] b) {
        int[] res = new int[a.length];
        for (int i = 0; i < a.length; i++) res[i] = a[i] ^ b[i];
        return res;
    }

    private int[] leftShift(int[] bits, int n) {
        int[] shifted = new int[bits.length];
        for (int i = 0; i < bits.length; i++) {
            shifted[i] = bits[(i + n) % bits.length];
        }
        return shifted;
    }

    private int[] concat(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private int[] substitute(int[] input) {
        int[] output = new int[32];
        for (int i = 0; i < 8; i++) {
            int row = (input[i * 6] << 1) + input[i * 6 + 5];
            int col = 0;
            for (int j = 1; j <= 4; j++) {
                col = (col << 1) + input[i * 6 + j];
            }
            int val = S_BLOCK[i][row][col];
            for (int j = 0; j < 4; j++) {
                output[i * 4 + (3 - j)] = (val >> j) & 1;
            }
        }
        return output;
    }

    private int[] bytesToBits(byte[] bytes) {
        int[] bits = new int[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = (bytes[i] >> (7 - j)) & 1;
            }
        }
        return bits;
    }

    private byte[] bitsToBytes(int[] bits) {
        byte[] bytes = new byte[bits.length / 8];
        for (int i = 0; i < bytes.length; i++) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                b = (byte) ((b << 1) | bits[i * 8 + j]);
            }
            bytes[i] = b;
        }
        return bytes;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите сообщение: ");
        String message = scanner.nextLine();

        System.out.print("Введите ключ: ");
        String key = scanner.nextLine();

        AlgorithmDES des = new AlgorithmDES();
        String encrypted = des.encryptMessage(message, key);

        System.out.println("====================================================");
        System.out.println("Исходное сообщение: " + message);
        System.out.println("Зашифрованное (Base64): " + encrypted);
        System.out.println("====================================================");
    }
}
