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

public class AES_Encryption {

	private final String algorithm = "AES";
    private final String charset = "utf-8";        
    private AES_random_key ran_key = null;
    
    public AES_Encryption(Context refContext){
    	ran_key = new AES_random_key(refContext);
    }
	
	public String aes_encrypt(String Data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException{
        
        SecretKeySpec key = new SecretKeySpec(ran_key.get_key(),algorithm);
        byte[] encrypted_bytes;
        String encrypted_data = "";
        Cipher aes;
        
        aes = Cipher.getInstance(algorithm);
        aes.init(Cipher.ENCRYPT_MODE,key);
        encrypted_bytes = aes.doFinal(Data.getBytes(charset));
        encrypted_data = new Base64().encodeToString(encrypted_bytes);
        
        return encrypted_data;
	}

	public String aes_decrypt(String Data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    
		String decrypted_data = "";
   
		SecretKeySpec key = new SecretKeySpec(ran_key.get_key(),algorithm);
		Cipher aes = Cipher.getInstance(algorithm);
		aes.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new Base64().decode(Data);
		byte[] decrypted_bytes = aes.doFinal(decordedValue);
    	decrypted_data = new String(decrypted_bytes,charset); 
    	
    	return decrypted_data;    
	}

}
