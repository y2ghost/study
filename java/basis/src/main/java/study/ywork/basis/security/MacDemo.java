package study.ywork.basis.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// 安全消息摘要/消息认证码等示例
public class MacDemo {
    public static void main(String[] args) {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");
        mac();
        dh();
    }

    private static void mac() {
        try {
            byte[] data = "消息认证码示例".getBytes();
            KeyGenerator gen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = gen.generateKey();
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] result = mac.doFinal(data);
            System.out.println(new String(result));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // DH算法密钥对生成
    private static void dh() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("DH");
            KeyPair kp1 = gen.genKeyPair();
            KeyPair kp2 = gen.genKeyPair();
            KeyAgreement keyAgree = KeyAgreement.getInstance(gen.getAlgorithm());
            keyAgree.init(kp2.getPrivate());
            keyAgree.doPhase(kp1.getPublic(), true);
            SecretKey key = new SecretKeySpec(keyAgree.generateSecret(), "DES");
            System.out.println(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
