package DataBaseControl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "3FC85A59C6A7D3C95AD8D660BC6F5C18"; // 128/192/256 бит (16/24/32 символа)
    private static final String IV = "54335056376C4C38"; // 16 байт для AES
 
    
    public static String encrypt(String value) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        
        byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}