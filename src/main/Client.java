package main;

import Dess.DES; // Імпортуй свій клас DES

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        // Завантаж ключі з файлу
        byte[][] Tkey = KeyFileManager.loadKeysFromFile("my_key.txt");

        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Введіть ідентифікатор користувача: ");
        String nickname1 = scanner1.nextLine();
        String nickname = nickname1;
        // Генерація мітки часу та об'єднання з нікнеймом
        long timestamp = System.currentTimeMillis();
        String dataToEncrypt = timestamp + ":" + nickname;

        // Шифрування за допомогою DES.TripleDES_Encrypt
        byte[] encryptedData = DES.TripleDES_Encrypt(dataToEncrypt.getBytes(), Tkey);
        String encodedEncryptedData = Base64.getEncoder().encodeToString(encryptedData);
        //шuThread.sleep(1000);

        // Надсилання на сервер
        Socket socket = new Socket("localhost", 8080);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(encodedEncryptedData);
        System.out.println("Дані надіслано на сервер.");

        // Отримання відповіді від сервера
        boolean flag = true;
        while (flag) {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String serverResponse = in.readUTF();

                if (!serverResponse.isEmpty()) {

                    System.out.println(serverResponse);
                    flag = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        socket.close();
    }
}