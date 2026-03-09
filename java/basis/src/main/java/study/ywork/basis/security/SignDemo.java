package study.ywork.basis.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;

// 签名示例
public class SignDemo {
    public static void main(String[] args) {
        sign();
        signObject();
    }

    private static void sign() {
        try {
            // 生成签名
            byte[] data = "签名示例".getBytes();
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(1024);
            KeyPair keyPair = gen.generateKeyPair();
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(keyPair.getPrivate());
            sign.update(data);
            // 获取签名结果
            byte[] signBytes = sign.sign();
            // 验证签名
            sign.initVerify(keyPair.getPublic());
            sign.update(data);
            boolean status = sign.verify(signBytes);
            System.out.println(String.format("签名验证结果: %s\n", status));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void signObject() {
        try {
            // 生成签名
            byte[] data = "签名示例".getBytes();
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(1024);
            KeyPair keyPair = gen.generateKeyPair();
            Signature sign = Signature.getInstance("SHA256withRSA");
            SignedObject signObj = new SignedObject(data, keyPair.getPrivate(), sign);
            // 获取签名结果
            // byte[] signBytes = signObj.getSignature();
            // 验证签名
            boolean status = signObj.verify(keyPair.getPublic(), sign);
            System.out.println(String.format("签名验证结果: %s\n", status));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
