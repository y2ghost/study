package study.ywork.basis.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class CipherDemo {
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		KeyGenerator gen = KeyGenerator.getInstance("DES");
		SecretKey secretKey = gen.generateKey();
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.WRAP_MODE, secretKey);
		// 包装秘密密钥，可以传递给解包的一方
		byte[] keyWrapper = cipher.wrap(secretKey);
		// 解包秘密密钥
		cipher.init(Cipher.UNWRAP_MODE, secretKey);
		Key key = cipher.unwrap(keyWrapper, "DES", Cipher.SECRET_KEY);
		System.out.println(key);
		// 加密操作
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] input = cipher.doFinal("DES DATA".getBytes());
		// 解密操作
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] output = cipher.doFinal(input);
		System.out.println(output.length);
	}
}
