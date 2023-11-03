package study.ywork.basis.security;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.AlgorithmParameterGenerator;

/**
 * 算法参数类、生成类一般很少用到 密钥对常用用于非对称加解密
 */
public class KeysDemo {
    public static void main(String[] args) {
        desParameters();
        desGenerateParameters();
        dsaKeyPair();
        rsaKeyPair();
        desRandom();
    }

    private static void desParameters() {
        try {
            AlgorithmParameters ap = AlgorithmParameters.getInstance("DES");
            byte[] params = new BigInteger("19050619766489163472466").toByteArray();
            ap.init(params);
            byte[] b = ap.getEncoded();
            System.out.println(new BigInteger(b).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void desGenerateParameters() {
        try {
            AlgorithmParameterGenerator gen = AlgorithmParameterGenerator.getInstance("DSA");
            gen.init(512);
            AlgorithmParameters ap = gen.generateParameters();
            byte[] b = ap.getEncoded();
            System.out.println(new BigInteger(b).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void dsaKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(1024);
            KeyPair keys = kpg.genKeyPair();
            System.out.println(keys.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void rsaKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair keys = kpg.genKeyPair();
            byte[] keyBytes = keys.getPrivate().getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            System.out.println(privateKey.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void desRandom() {
        try {
            SecureRandom random = new SecureRandom();
            KeyGenerator gen = KeyGenerator.getInstance("DES");
            gen.init(random);
            SecretKey key = gen.generateKey();
            System.out.println(key.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
