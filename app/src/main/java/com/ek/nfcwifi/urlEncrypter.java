package com.ek.nfcwifi;

/**
 * Created by eyeKill on 16/1/29.
 */

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class urlEncrypter {
    private static Random r=new Random();
    /**
     * aes加密
     * @param content 明文
     * @param password  密码
     * @return 密文
     */
    public static byte[] encrypt_AES(String content, byte[] password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * aes解密
     * @param content 密文
     * @param password 密码
     * @return 明文
     */
    public static byte[] decrypt_AES(byte[] content, byte[] password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 解密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] package_base64(byte[] content){
        return Base64.encode(content, Base64.URL_SAFE);
    }
    public static byte[] unpack_base64(String content){
        return Base64.decode(content, Base64.URL_SAFE);
    }

    public static String encrypt(String content,String pkName){
        byte[] a=pkName.getBytes();
        byte t;
        for(int i=0;i<a.length;i++){t=a[i];a[i]=a[a.length-i];a[a.length-i]=t;}
        byte[] p=package_base64(a);
        r.nextBytes(a);
        for(int i=0;i<p.length;i++) p[i]=(byte)((p[i]+i*i)%107);
        String re=new String(package_base64(encrypt_AES(content,p)));
        r.nextBytes(p);
        return re;
    }

    public static String decrypt(String content,String pkName){
        byte[] a=pkName.getBytes();
        byte t;
        for(int i=0;i<a.length;i++){t=a[i];a[i]=a[a.length-i];a[a.length-i]=t;}
        byte[] p=package_base64(a);
        r.nextBytes(a);
        for(int i=0;i<p.length;i++) p[i]=(byte)((p[i]+i*i)%107);
        String re=new String(decrypt_AES(unpack_base64(content), p));
        r.nextBytes(p);
        return re;
    }

}
