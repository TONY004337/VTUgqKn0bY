// 代码生成时间: 2025-08-28 00:35:04
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class PasswordEncryptionDecryption {
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);

        // Encryption endpoint
        app.post("/encrypt", ctx -> {
            String password = ctx.bodyAsClass(EncryptRequest.class).getPassword();
            try {
                String encryptedPassword = encrypt(password);
                ctx.json(encryptedPassword);
            } catch (Exception e) {
                ctx.status(500).result("Encryption error: " + e.getMessage());
            }
        });

        // Decryption endpoint
        app.post("/decrypt", ctx -> {
            String encryptedPassword = ctx.bodyAsClass(DecryptRequest.class).getEncryptedPassword();
            try {
                String decryptedPassword = decrypt(encryptedPassword);
                ctx.json(decryptedPassword);
            } catch (Exception e) {
                ctx.status(500).result("Decryption error: " + e.getMessage());
            }
        });
    }

    /**
     * Encrypts a password using AES algorithm.
     *
     * @param password The password to encrypt.
     * @return The encrypted password in Base64 encoding.
     * @throws Exception If encryption fails.
     */
    public static String encrypt(String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts an encrypted password using AES algorithm.
     *
     * @param encryptedPassword The encrypted password in Base64 encoding.
     * @return The decrypted password.
     * @throws Exception If decryption fails.
     */
    public static String decrypt(String encryptedPassword) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    /**
     * A POJO class to hold the password for encryption request.
     */
    static class EncryptRequest {
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * A POJO class to hold the encrypted password for decryption request.
     */
    static class DecryptRequest {
        private String encryptedPassword;

        public String getEncryptedPassword() {
            return encryptedPassword;
        }

        public void setEncryptedPassword(String encryptedPassword) {
            this.encryptedPassword = encryptedPassword;
        }
    }
}
