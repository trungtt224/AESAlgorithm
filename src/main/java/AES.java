import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author trungtt
 */
public class AES
{
	public static void main(String[] args) throws Exception
	{
		// Must be length 16
		String key = "SPIRALG-IOT-##**";
		String strEncypt = encrypt(key, "nal_space");
		System.out.println("Encrypted: " + strEncypt);

		String strDecrypt = decrypt(key, strEncypt);
		System.out.println("Decrypted: " + strDecrypt);
	}

	// TODO: store key to properties.
	private static String encrypt(String key, String _namespace) {
		try {
			String initVector = RandomStringUtils.randomAlphanumeric(16);

			IvParameterSpec ivParamSpec = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");


			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParamSpec);

			byte[] encrypted = cipher.doFinal(_namespace.getBytes());

			// Password device: namespace + ":" + initVector
			// TODO: store password to device db with device id
			String result = Base64.encodeBase64String(encrypted) + ":" +  initVector;
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	// TODO: Get key from file properties.
	private static String decrypt(String key, String encrypted) {
		try {
			String[] encryptedInfo = encrypted.split(":");
			String vector = encryptedInfo[1];

			IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}
 }
