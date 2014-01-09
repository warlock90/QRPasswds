package com.aes;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Base64;

public class AESEncryption {

	private final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private final String CHARSET = "utf-8";        
    private AESRandomKey ranKey = null;
    
    public AESEncryption(Context refContext) {
    	ranKey = new AESRandomKey(refContext);
    }
	
	public String aes_encrypt(String Data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ParserConfigurationException, SAXException{
        
        SecretKeySpec key = new SecretKeySpec(ranKey.getKey(),ALGORITHM);
        
        Cipher aes = Cipher.getInstance(ALGORITHM);
        aes.init(Cipher.ENCRYPT_MODE,key);
        
        byte[] encryptedBytes = aes.doFinal(Data.getBytes(CHARSET));

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
	}

	public String aes_decrypt(String Data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ParserConfigurationException, SAXException {

		SecretKeySpec key = new SecretKeySpec(ranKey.getKey(),ALGORITHM);
		
		Cipher aes = Cipher.getInstance(ALGORITHM);
		aes.init(Cipher.DECRYPT_MODE, key);
		
		byte[] decordedValue = Base64.decode(Data, Base64.DEFAULT);
		byte[] decryptedBytes = aes.doFinal(decordedValue);

    	return new String(decryptedBytes,CHARSET);    
	}

}
