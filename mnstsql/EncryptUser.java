// javac -encoding UTF-8 Encrypt.java EncryptUser.java && java EncryptUser
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUser {

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
                    int[] split_indexes = new int[9];
                    split_indexes[0] = line.indexOf("(");
                    split_indexes[split_indexes.length - 1] = line.lastIndexOf(")");
                    for (int i = 1; i < split_indexes.length - 1; ++i) {
                        split_indexes[i] = line.indexOf(',', split_indexes[i - 1] + 1);
                    }
                    String mypw = line.substring(split_indexes[2] + 2, split_indexes[3] - 1);
                    String myphone = line.substring(split_indexes[5] + 2, split_indexes[6] - 1);
                    String mysid = line.substring(split_indexes[6] + 2, split_indexes[7] - 1);

                    if (myphone.length() != 10)
                        System.out.println("Wrong Phone" + myphone);
                    if (mysid.length() != 13)
                        System.out.println("Wrong Sid" + mysid);

                    // 암호화 수행
                    String encryptedPassword = Encrypt.hashMD5(mypw);
                    String encryptedPhone = Encrypt.encryptAES(myphone);
                    String encryptedSid = Encrypt.encryptAES(mysid);

                    // 원래 SQL 문을 그대로 유지하면서 암호화된 값으로 치환하여 새로운 파일에 쓰기
                    line = line.substring(0, split_indexes[2] + 2) + encryptedPassword + line.substring(split_indexes[3] - 1, split_indexes[5] + 2) + encryptedPhone + line.substring(split_indexes[6] - 1, split_indexes[6] + 2) + encryptedSid + line.substring(split_indexes[7] - 1);
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
