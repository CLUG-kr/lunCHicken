package com.clug.lunchicken.login.account;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Aes256Module {
	 
	 final static String secretKey   = "12345678901234567890123456789012"; //32bit
	 static String IV                = ""; //16bit
	 
	 private static volatile Aes256Module instance; 
	 public static Aes256Module getInstance(){
	     if(instance==null){
	         synchronized(Aes256Module.class){
	             if(instance==null)
	            	 instance=new Aes256Module();
	         }
	     }
	     return instance;
	 }
	 
	 private Aes256Module(){
	     IV = secretKey.substring(0,16);
	 }
	 
	 //암호화
	 public String encodeAes(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
	     byte[] keyData = secretKey.getBytes();
	 
		 SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		 
		 Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		 c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
		 
		 byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		 String enStr = new String(Base64.encodeBase64(encrypted));
		 
		 return enStr;
	 }
	 
	 //복호화
	 public String decodeAes(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		  byte[] keyData = secretKey.getBytes();
		  SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		  Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		  c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
		 
		  byte[] byteStr = Base64.decodeBase64(str.getBytes());
		 
		  return new String(c.doFinal(byteStr),"UTF-8");
	 }
}
