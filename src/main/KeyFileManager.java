package main;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;

public class KeyFileManager {

    public static void saveKeyToFile(SecretKey key, String filename) throws IOException {
        byte[] keyBytes = key.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(encodedKey);
        }
    }

    public static SecretKey loadKeyFromFile(String filename, String algorithm) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String encodedKey = reader.readLine();
            byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
            return new SecretKeySpec(keyBytes, algorithm);
        }
    }
    public static void saveKeysToFile(byte[][] keys, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(keys);

        }
    }

    public static byte[][] loadKeysFromFile(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis))
        {
            return (byte[][]) ois.readObject();
        }
    }

    public static void main(String[] args) {
        try {
            // Приклад використання:
            // 1. Згенеруйте ключ (або використовуйте існуючий)
            // SecretKey key = KeyGenerator.getInstance("DESede").generateKey();

            // 2. Збережіть ключ у файл
            KeyGenerator keyGen = KeyGenerator.getInstance("DESede");
            keyGen.init(168);
            SecretKey secretKey = keyGen.generateKey();
             saveKeyToFile(secretKey, "my_key.txt");

            // 3. Завантажте ключ з файлу
            SecretKey loadedKey = loadKeyFromFile("my_key.txt", "DESede");

            // 4. Використовуйте завантажений ключ
            System.out.println("Завантажений ключ: " + loadedKey);



        } catch (Exception e) {
            System.err.println("Помилка: " + e.getMessage());
        }
    }
}