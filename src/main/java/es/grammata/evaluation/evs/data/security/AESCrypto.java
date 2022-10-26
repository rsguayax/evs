package es.grammata.evaluation.evs.data.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


@org.springframework.stereotype.Component
@Configurable
public class AESCrypto {
	
	public static String ALGORITHM = "AES";
	private static String AES_CBS_PADDING = "AES/CBC/PKCS5Padding";
	private static int AES_128 = 128;
	private static String key;
	private static String vector;


	public static byte[] encrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception {
		return AESCrypto.encryptDecrypt(Cipher.ENCRYPT_MODE, key, IV, message);
	}

	public static byte[] decrypt(final byte[] key, final byte[] IV, final byte[] message) throws Exception {
		return AESCrypto.encryptDecrypt(Cipher.DECRYPT_MODE, key, IV, message);
	}

	private static byte[] encryptDecrypt(final int mode, final byte[] key, final byte[] IV, final byte[] message)
			throws Exception {
		final Cipher cipher = Cipher.getInstance(AES_CBS_PADDING);
		final SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
		final IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(mode, keySpec, ivSpec);
		return cipher.doFinal(message);
	}
	
	
	public static String encrypt(String text) throws Exception {
		BASE64Encoder encoder = new BASE64Encoder();

		byte[] cipherText = AESCrypto.encrypt(AESCrypto.key.getBytes(), AESCrypto.vector.getBytes(), text.getBytes());
		
		return encoder.encode(cipherText); 
	}
	
	
	public static String decrypt(String cipherText) throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] epArray = decoder.decodeBuffer(cipherText);

		byte[] decryptedString = AESCrypto.decrypt(AESCrypto.key.getBytes(), AESCrypto.vector.getBytes(), epArray);
		
		return new String(decryptedString); 
	}
	
	
	@Value("${crypto.key}")
    public void setExternalKey(String externalKeyParam) {
        key = externalKeyParam;
    }
	
	@Value("${crypto.vector}")
    public void setExternalVector(String externalVectorParam) {
        vector = externalVectorParam;
    }

	
	
	public static void main(String[] args) throws Exception {
		
		/*KeyGenerator keyGenerator = KeyGenerator.getInstance(AESCrypto.ALGORITHM);
		keyGenerator.init(AES_128);
		//Generate Key
		SecretKey key = keyGenerator.generateKey();
		System.out.println("KEY: " + key.getEncoded());
		//Initialization vector
		SecretKey IV = keyGenerator.generateKey();*/
		String key = "I472GksF578S8rQ9";
		String vector = "pOO9wLxM1lcS4pK0";
		

		String randomString = "Kt31Rr"; //UUID.randomUUID().toString().substring(0, 16);
		System.out.println("1. Message to Encrypt: " + randomString);
		
		BASE64Encoder encoder = new BASE64Encoder();
		BASE64Decoder decoder = new BASE64Decoder();

		byte[] cipherText = AESCrypto.encrypt(key.getBytes(), vector.getBytes(), randomString.getBytes());
		System.out.println("2. Encrypted Text: " + encoder.encode(cipherText));
		
		byte[] decryptedString = AESCrypto.decrypt(key.getBytes(), vector.getBytes(), cipherText);
		System.out.println("3. Decrypted Message : " + new String(decryptedString));
		
		String test = AESCrypto.decrypt("vKnbyqBZiAgLpWztecUPLg==");
		System.out.println("4. Decrypted Message : " + test);
		
		
	}
}