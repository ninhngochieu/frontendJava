package Home.Model;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSA {
    private static  PrivateKey privateKeys;
    public static void genaKeyPari(){
        try {
            SecureRandom sr = new SecureRandom();
            // Thuật toán phát sinh khóa - RSA
            // Độ dài khóa 1024(bits), độ dài khóa này quyết định đến độ an toàn của khóa, càng lớn thì càng an toàn
            // Demo chỉ sử dụng 1024 bit. Nhưng theo khuyến cáo thì độ dài khóa nên tối thiểu là 2048
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, sr);

            // Khởi tạo cặp khóa
            KeyPair kp = kpg.genKeyPair();
            // PublicKey
            PublicKey publicKey = kp.getPublic();
            // PrivateKey
            PrivateKey privateKey = kp.getPrivate();
            privateKeys = privateKey;
            Singleton sg = Singleton.getInstance();
            sg.publicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            //System.out.println(publicKey.toString()+"và"+privateKey.toString());
            //System.out.println(sg.privateKey + "và" + sg.publicKey);
            System.out.println("Ma hoa thanh cong");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String decryt(String content){
        String key = null;
        try {
            // Đọc file chứa private key

            // Tạo private key

            // Giải mã dữ liệu
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKeys);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(content));
            //System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
            System.out.println("Giai ma thanh cong");
            key = new String(decryptOut);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return key;
    }
}
