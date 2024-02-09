// javac -encoding UTF-8 EncryptUser.java && java EncryptUser

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUser {

    public static String hashMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdBytes = md.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte mdByte : mdBytes) {
            String hex = Integer.toHexString(0xff & mdByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String hashSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] mdBytes = md.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte mdByte : mdBytes) {
            String hex = Integer.toHexString(0xff & mdByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static void main(String[] args) {
        try {
            String inputFilePath = "myuser_plain.sql";
            String outputFilePath = "myuser_cipher.sql";

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().startsWith("INSERT INTO")) {
                    // 암호화할 값 추출
                    String[] values = line.split(",");
                    String mypw = values[2].trim().replaceAll("[']", "");
                    String myphone = values[5].trim().replaceAll("[']", "");
                    String mysid = values[6].trim().replaceAll("[']", "").replaceAll("[)]", "").replaceAll(";", "");

                    if (myphone.length() != 10)
                        System.out.println("Wrong Phone" + myphone);
                    if (mysid.length() != 13)
                        System.out.println("Wrong Sid" + mysid);

                    // 암호화 수행
                    String encryptedPassword = hashMD5(mypw);
                    String encryptedPhone = hashSHA256(myphone);
                    String encryptedSid = hashSHA256(mysid);

                    // 원래 SQL 문을 그대로 유지하면서 암호화된 값으로 치환하여 새로운 파일에 쓰기
                    line = line.replace(mypw, encryptedPassword);
                    line = line.replace(myphone, encryptedPhone);
                    line = line.replace(mysid, encryptedSid);
                }

                writer.write(line);
                writer.newLine();
            }

            reader.close();
            writer.close();

            System.out.println("Encryption completed. Encrypted data saved in " + outputFilePath);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
