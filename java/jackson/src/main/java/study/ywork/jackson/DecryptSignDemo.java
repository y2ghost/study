package study.ywork.jackson;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 解密数据示例 签名数据示例
 */
public class DecryptSignDemo {
    private static final String PRIVATE_DER = "private.der";
    private static final String APPLY_KEY = "apply.key";
    private static PrivateKey privateKey;

    public static void main(String[] args) {
        loadPrivateKey();
        String data = getApplyData();
        String json = decrypt(data);
        System.out.println("解码数据: \n" + json);
        String sign = sign(json);
        System.out.println("签名数据: \n" + sign);

        String license = getLicenseData(json);
        System.out.println("授权数据: \n" + license);
    }

    private static File getFile(String fileName) {
        String filePath = DecryptSignDemo.class.getClassLoader().getResource(fileName).getPath();
        File file = new File(filePath);
        return file;
    }

    private static String getApplyData() {
        String data = "";
        try {
            File file = getFile(APPLY_KEY);
            byte[] keyBytes = Files.readAllBytes(file.toPath());
            data = new String(keyBytes);
        } catch (Exception ex) {
            System.out.println("加载申请数据失败：" + ex.getMessage());
        }

        return data;
    }

    private static void loadPrivateKey() {
        try {
            File file = getFile(PRIVATE_DER);
            byte[] keyBytes = Files.readAllBytes(file.toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(spec);
        } catch (Exception ex) {
            System.out.println("加载许可秘钥失败：" + ex.getMessage());
        }
    }

    private static String decrypt(String applyData) {
        String json = "";
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(applyData));
            json = new String(result);
        } catch (Exception ex) {
            System.out.println("解密数据失败：" + ex.getMessage());
        }

        return json;
    }

    private static String sign(String json) {
        byte[] data = json.getBytes();
        String signString = "";

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            byte[] signData = signature.sign();
            byte[] encData = Base64.getEncoder().encode(signData);
            signString = new String(encData);
        } catch (Exception ex) {
            System.out.println("签名数据失败：" + ex.getMessage());
        }

        return signString;
    }

    private static String getLicenseData(String json) {
        String result = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> applyJsonMap = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
            // 申请数据增加签发时间、到期时间
            applyJsonMap.put("issueTime", "2023-10-31 23:11:11");
            applyJsonMap.put("expireTime", "2024-11-11");
            applyJsonMap.put("signer", "YY");
            // 计算JSON格式的授权数据的签名
            String licenseJson = objectMapper.writeValueAsString(applyJsonMap);
            String licenseSign = sign(licenseJson);
            // 构造授权文件数据
            Map<String, String> licenseDataMap = new HashMap<>();
            licenseDataMap.put("licenseData", licenseJson);
            licenseDataMap.put("signature", licenseSign);
            String licenseDataJson = objectMapper.writeValueAsString(licenseDataMap);
            byte[] encryptedLicenseData = Base64.getEncoder().encode(licenseDataJson.getBytes());
            result = new String(encryptedLicenseData);
        } catch (Exception ex) {
            System.out.println("生成授权数据异常: " + ex.getMessage());
        }

        return result;
    }
}
