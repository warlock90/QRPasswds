package com.aes;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;

public class AESEncryption {

	private final String ALGORITHM = "AES";
    private final String CHARSET = "utf-8";        
    private AESRandomKey ranKey = null;
    
    public AESEncryption(Context refContext) {
    	ranKey = new AESRandomKey(refContext);
    }
	
	public String aes_encrypt(String Data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException{
        
        SecretKeySpec key = new SecretKeySpec(ranKey.getKey(),ALGORITHM);
        byte[] encryptedBytes;
        String encryptedData = "";
        Cipher aes;
        
        aes = Cipher.getInstance(ALGORITHM);
        aes.init(Cipher.ENCRYPT_MODE,key);
        encryptedBytes = aes.doFinal(Data.getBytes(CHARSET));
        encryptedData = new Base64().encodeToString(encryptedBytes);
        
        return encryptedData;
	}

	public String aes_decrypt(String Data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    
		String decryptedData = "";
   
		SecretKeySpec key = new SecretKeySpec(ranKey.getKey(),ALGORITHM);
		Cipher aes = Cipher.getInstance(ALGORITHM);
		aes.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new Base64().decode(Data);
		byte[] decryptedBytes = aes.doFinal(decordedValue);
    	decryptedData = new String(decryptedBytes,CHARSET); 
    	
    	return decryptedData;    
	}

}
