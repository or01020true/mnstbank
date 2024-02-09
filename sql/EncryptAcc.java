// javac -encoding UTF-8 EncryptAcc.java && java EncryptAcc

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptAcc {

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
            String inputFilePath = "myacc_plain.sql";
            String outputFilePath = "myacc_cipher.sql";

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().startsWith("INSERT INTO")) {
                    // 암호화할 값 추출
                    String[] values = line.split(",");
                    String myacc = values[0].trim().substring(1).replaceAll("[']", "");
                    String myaccpw = values[4].trim().replaceAll("[']", "");

                    if (myacc.length() != 10)
                        System.out.println("Wrong Phone" + myacc);
                    if (myaccpw.length() != 4)
                        System.out.println("Wrong Sid" + myaccpw);

                    // 암호화 수행
                    String encryptedAccount = hashSHA256(myacc);
                    String encryptedPassword = hashMD5(myaccpw);

                    // 원래 SQL 문을 그대로 유지하면서 암호화된 값으로 치환하여 새로운 파일에 쓰기
                    line = line.replace(myacc, encryptedAccount);
                    line = line.replace(myaccpw, encryptedPassword);
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
