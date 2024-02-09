// javac -encoding UTF-8 Encrypt.java EncryptAcc.java && java EncryptAcc
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptAcc {

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
                    int[] split_indexes = new int[7];
                    split_indexes[0] = line.indexOf("(");
                    split_indexes[split_indexes.length - 1] = line.lastIndexOf(")");
                    for (int i = 1; i < split_indexes.length - 1; ++i) {
                        split_indexes[i] = line.indexOf(',', split_indexes[i - 1] + 1);
                    }
                    String myacc = line.substring(split_indexes[0] + 2, split_indexes[1] - 1);
                    String myaccpw = line.substring(split_indexes[4] + 2, split_indexes[5] - 1);

                    if (myacc.length() != 10)
                        System.out.println("Wrong Phone" + myacc);
                    if (myaccpw.length() != 4)
                        System.out.println("Wrong Sid" + myaccpw);

                    // 암호화 수행
                    String encryptedAccount = Encrypt.encryptAES(myacc);
                    String encryptedPassword = Encrypt.hashMD5(myaccpw);

                    // 원래 SQL 문을 그대로 유지하면서 암호화된 값으로 치환하여 새로운 파일에 쓰기
                    line = line.substring(0, split_indexes[0] + 2) + encryptedAccount + line.substring(split_indexes[1] - 1, split_indexes[4] + 2) + encryptedPassword + line.substring(split_indexes[5] - 1);
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
