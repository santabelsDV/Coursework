package main;

import Dess.DES; // Імпортуй свій клас DES

import javax.crypto.KeyGenerator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import static Dess.KeyGenerator.bytesToHex;

public class Server {

    public static void main(String[] args) throws Exception {
        // Ініціалізація генератора ключів
        javax.crypto.KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        // Ініціалізація генератора ключів з розміром 56 біт
        keyGenerator.init(56);


        PatternPrinter();
        // Генерація ключа
        byte[] key = keyGenerator.generateKey().getEncoded();
        // Генерація наступного ключа
        byte[] key2 = keyGenerator.generateKey().getEncoded();
        // Генерація наступного ключа
        byte[] key3 = keyGenerator.generateKey().getEncoded();

        byte[][] Tkey = {key, key2, key3};
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Використати зегеровані ключі ведіть 1, або введіть текст для генерації ключів введіть 2: ");
        String inputText = scanner1.nextLine();

        // використовуємо зегеровані ключі
        if (inputText.equals("1")) {
        }
        // генеруємо ключі з текстом
        else {
            for (int i = 0; i <= 2; i++) {

                System.out.println("Введіть текст для генурaції ключа №" + (i + 1) + ": ");
                inputText = scanner1.nextLine();

                // генеруємо 64-бітний ключ з текстом
                Tkey[i] = generate64BitKey(inputText);
            }
        }


        // Збереження ключів у файл (заміни на свій метод)
        // Збережи ключі у файл (заміни на свій метод)
        KeyFileManager.saveKeysToFile(Tkey, "my_key.txt");
        // Створення серверного сокета на порту 8080
        ServerSocket serverSocket = new ServerSocket(8080);
        // Вивод повідомлення про запуск сервера
        System.out.println("Сервер запущено. \n Для зупинки сервера пропишіть q");

        // Створення змінної для сокета
        Socket socket = null;

        // Створення потоку для читання з консолі
        Thread consoleThread = new Thread(() -> {
            // Створення сканера для читання з консолі
            Scanner scanner = new Scanner(System.in);
            // Безкінечний цикл
            while (true) {
                // Читання з консолі
                if (scanner.nextLine().equals("q")) {
                    try {
                        // Закриття серверного сокета
                        serverSocket.close();
                        // Вивод повідомлення про зупинку сервера
                        System.out.println("Сервер зупинено.");
                        // Вихід з циклу
                        break;
                    } catch (Exception e) {
                        // Вивод помилки
                        e.printStackTrace();
                    }
                }
            }
        });


        for (int i = 0; i < Tkey.length; i++) {
            System.out.println("Key " + (i + 1) + ": " + bytesToHex(Tkey[i]));
        }

        // Запуск потоку для читання з консолі
        consoleThread.start();


        while (!serverSocket.isClosed()) {
            try {
                // Очікування з\'\єднання з клієнтом
                socket = serverSocket.accept();

                DataInputStream in = new DataInputStream(socket.getInputStream());
                String encodedEncryptedData = in.readUTF();

                // Розшифруй дані за допомогою DES.TripleDES_Decrypt
                byte[] decryptedData = DES.TripleDES_Decrypt(Base64.getDecoder().decode(encodedEncryptedData), Tkey);
                String decryptedString = new String(decryptedData);


                String[] parts = decryptedString.split(":");

                long receivedTimestamp = Long.parseLong(parts[0]);


                String receivedNickname = parts[1];

                long currentTime = System.currentTimeMillis();
                boolean isValid = (currentTime - receivedTimestamp) < 1000;

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                if (isValid && receivedNickname.equals("user")) {
                    System.out.println("Користувача " + receivedNickname + " автентифіковано.");
                    out.writeUTF("Вас автентифіковано!!!");
                } else {
                    System.out.println("Помилка автентифікації.");
                    out.writeUTF("Помилка автентифікації.!!!");
                }
            } catch (SocketException e) {
                // Обробка помилки, якщо сокет закрито
            } catch (NumberFormatException e) {
                System.out.println("Помилка автентифікації.");
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("Помилка автентифікації.!!!");
                continue;
            }
        }

        if (socket != null) {
            socket.close();
        }
    }


    public static byte[] generate64BitKey(String input) throws NoSuchAlgorithmException {
        // Використовуємо SHA-1 для хешування рядка
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] hash = sha.digest(input.getBytes());

        // Беремо перші 8 байтів хешу, щоб отримати 64 біти (8 байт)
        byte[] keyBytes = Arrays.copyOf(hash, 8);

        // Встановлюємо останній байт парності (можна використовувати 0 або будь-яке інше значення)
        keyBytes[7] = 0; // або будь-яке інше значення

        return keyBytes;
    }

    public static void PatternPrinter() {

        int start = 1; // Початкове число
        int increment = 2; // Збільшення числа
        int rows = 4; // Кількість рядків

        // Зовнішній цикл для рядків
        for (int i = 1; i <= rows; i++) {
            // Внутрішній цикл для чисел у кожному рядку
            for (int j = 1; j <= i; j++) {
                System.out.print(start + " ");
                start += increment; // Збільшуємо число
            }
            System.out.println(); // Перехід на новий рядок
        }

    }
}