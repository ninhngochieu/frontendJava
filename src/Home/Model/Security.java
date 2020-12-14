package Home.Model;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Security {
    private SecretKey secretKey;
    private byte[] key;
    public Security() {
    }
    private void setKey(String string_key){
        try {
            this.key = string_key.getBytes("UTF-8");
            this.key = MessageDigest.getInstance("SHA-1").digest(this.key);
            this.key = Arrays.copyOf(this.key,16);
            this.secretKey = new SecretKeySpec(this.key,"AES");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public String encrypt(String s,String ramdom_key){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            setKey(ramdom_key);
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "123";
    }
    public String decrypt(String s,String ramdom_key){
        try {
            setKey(ramdom_key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return "Failure";
    }

}
