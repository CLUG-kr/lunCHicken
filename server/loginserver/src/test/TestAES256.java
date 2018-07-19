package test;

import java.io.UnsupportedEncodingException;
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
import org.json.simple.JSONObject;

import com.clug.lunchicken.login.account.Aes256Module;
 
public class TestAES256 {
	 
	 private static volatile TestAES256 INSTANCE;
	 
	 final static String secretKey   = "12345678901234567890123456789012"; //32bit
	 static String IV                = ""; //16bit
	 
	 public static TestAES256 getInstance(){
	     if(INSTANCE==null){
	         synchronized(TestAES256.class){
	             if(INSTANCE==null)
	                 INSTANCE=new TestAES256();
	         }
	     }
	     return INSTANCE;
	 }
	 
	 private TestAES256(){
	     IV = secretKey.substring(0,16);
	    }
	 
	 //암호화
	 public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
	     byte[] keyData = secretKey.getBytes();
	 
	 SecretKey secureKey = new SecretKeySpec(keyData, "AES");
	 
	 Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	 c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));
	 
	 byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
	 String enStr = new String(Base64.encodeBase64(encrypted));
	 
	 return enStr;
	 }
	 
	 //복호화
	 public static String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
	  byte[] keyData = secretKey.getBytes();
	  SecretKey secureKey = new SecretKeySpec(keyData, "AES");
	  Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	  c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
	 
	  byte[] byteStr = Base64.decodeBase64(str.getBytes());
	 
	  return new String(c.doFinal(byteStr),"UTF-8");
	 }
	 
	 @SuppressWarnings("unchecked")
		public static String makeToken(String id) {
			JSONObject tokenObj = new JSONObject();
			tokenObj.put("account_id", id);
			tokenObj.put("created_time", String.valueOf(System.currentTimeMillis()));
			String originStr = tokenObj.toJSONString();
			
			StringBuilder modulationStr = 
				new StringBuilder()
				.append(System.currentTimeMillis() / 100)
				.append(":")
				.append(originStr)
				.append(":hello!my#friends!");
			String tokenStr = null;
			try {
				tokenStr = Aes256Module.getInstance().encodeAes(modulationStr.toString());
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
			}
			return tokenStr;
		}
	 
	 public static void main(String[] data) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		 TestAES256.getInstance();
		 try {
			String hi = AES_Encode("{data:data, data:data}{data:data, data:data}{data:data, data:data}{data:data, data:data}{data:data, data:data}{data:data, data:data}{data:data, data:data}{data:data, data:data}");
			System.out.println(hi);
			String decode_hi = AES_Decode(hi);
			System.out.println(decode_hi);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		 String enc = makeToken("owlsogul");
		 String dec = AES_Decode(enc);
		 System.out.println(enc);
		 System.out.println(dec);
	 }
	}
