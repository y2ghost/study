package study.ywork.basis.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DigestDemo {
    public static void main(String[] args) {
        System.out.println("sha256:");
        sha256();
        System.out.println("sha256Stream:");
        sha256Stream();
    }

    private static void sha256() {
        byte[] input = "sha256".getBytes();
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(input);
            byte[] output = sha256.digest();
            byte[] enc = Base64.getEncoder().encode(output);
            System.out.println(new String(enc));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 使用文件流的方式计算摘要
     */
    private static void sha256Stream() {
        byte[] input = "sha256".getBytes();
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            DigestInputStream din = new DigestInputStream(new ByteArrayInputStream(input), sha256);
            din.on(true);
            din.read(input, 0, input.length);
            byte[] output = din.getMessageDigest().digest();
            byte[] enc = Base64.getEncoder().encode(output);
            System.out.println(new String(enc));
            din.close();

            DigestOutputStream dout = new DigestOutputStream(new ByteArrayOutputStream(), sha256);
            dout.on(true);
            dout.write(input, 0, input.length);
            output = din.getMessageDigest().digest();
            enc = Base64.getEncoder().encode(output);
            System.out.println(new String(enc));
            din.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
