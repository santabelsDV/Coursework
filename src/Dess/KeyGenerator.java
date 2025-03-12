package Dess;

import java.security.SecureRandom;

public class KeyGenerator {

    public static byte[] generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[8]; // 64 bits
        secureRandom.nextBytes(key);
        return key;
    }

    public static byte[][] generateTripleDESKeys() {
        byte[][] keys = new byte[3][8]; // 3 keys of 64 bits each
        for (int i = 0; i < 3; i++) {
            keys[i] = generateKey();
        }
        System.out.println(keys.length);
        return keys;
    }

    public static void main(String[] args) {
        // Generate a single DES key
        byte[] singleKey = generateKey();
        System.out.println("Single DES Key: " + bytesToHex(singleKey));

        // Generate Triple DES keys
        byte[][] tripleDESKeys = generateTripleDESKeys();
        System.out.println("Triple DES Keys:");
        for (int i = 0; i < 3; i++) {
            System.out.println("Key " + (i + 1) + ": " + bytesToHex(tripleDESKeys[i]));
        }
    }

    // Helper function to convert byte array to hex string
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}