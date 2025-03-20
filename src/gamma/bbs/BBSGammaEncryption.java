package gamma.bbs;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class BBSGammaEncryption {

    private BigInteger p, q, n, x;

    public BBSGammaEncryption(int bitLength) {
        SecureRandom random = new SecureRandom();

        this.p = getPrime(bitLength, random);
        this.q = getPrime(bitLength, random);
        this.n = p.multiply(q);

        do {
            this.x = new BigInteger(bitLength, random);
        } while (x.gcd(n).compareTo(BigInteger.ONE) != 0);
    }

    private BigInteger getPrime(int bitLength, SecureRandom random) {
        BigInteger prime;

        do {
            prime = BigInteger.probablePrime(bitLength, random);
        } while (!prime.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)));

        return prime;
    }

    public List<Integer> generateGamma(int length) {
        List<Integer> gamma = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            x = x.modPow(BigInteger.TWO, n);
            gamma.add(x.mod(BigInteger.TWO).intValue());
        }

        return gamma;
    }

    public List<Integer> encrypt(List<Integer> plaintext, List<Integer> gamma) {
        List<Integer> cipherText = new ArrayList<>();

        for (int i = 0; i < plaintext.size(); i++) {
            cipherText.add(plaintext.get(i) ^ gamma.get(i));
        }

        return cipherText;
    }

    public List<Integer> decrypt(List<Integer> cipherText, List<Integer> gamma) {
        return encrypt(cipherText, gamma);
    }

    public static void main(String[] args) {
        BBSGammaEncryption bbs = new BBSGammaEncryption(16);

        List<Integer> plainText = List.of(1, 0, 1, 1, 0, 1, 0, 0);
        List<Integer> gamma = bbs.generateGamma(plainText.size());

        List<Integer> cipherText = bbs.encrypt(plainText, gamma);
        List<Integer> decrypted = bbs.decrypt(cipherText, gamma);

        System.out.println("=========================== Результат ============================");

        System.out.println("Открытый текст: " + plainText);
        System.out.println("Гамма (BBS): " + gamma);
        System.out.println("Зашифрованный текст: " + cipherText);
        System.out.println("Дешифрованный текст: " + decrypted);

        System.out.println("================================================================");
    }
}
