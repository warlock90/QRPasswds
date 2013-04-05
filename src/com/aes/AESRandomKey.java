package com.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import android.content.Context;


public class AESRandomKey {

	private final String ALGORITHM = "AES";
    private final int KEYSIZE_BITS = 128;
    private final int KEYSIZE_BYTES = KEYSIZE_BITS / 8;
    private final String FILENAME = "QRPass.key";
    private final byte[] VALIDATE_DATA = "QRPassKey".getBytes();
    
    private Context context;
    
    public AESRandomKey(Context refContext) {
    	context = refContext;	
    }
    	
	public void generateKey() throws IOException, NoSuchAlgorithmException {
		
		byte[] randomBytes = new byte[KEYSIZE_BYTES];
		
		KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        kgen.init(KEYSIZE_BITS);
        randomBytes = kgen.generateKey().getEncoded();
        
        storeKey(randomBytes,context);
	}
	
	public void storeKey(byte[] key, Context ctx) throws IOException {
		
		FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		fos.write(VALIDATE_DATA);
		fos.flush();
		fos.write(key);
		fos.close();
	}
	
	public byte[] getKey() throws IOException{
		
		FileInputStream fis = context.openFileInput(FILENAME);
		byte[] buffer = new byte[KEYSIZE_BYTES];
		fis.skip(VALIDATE_DATA.length);
		fis.read(buffer);
		fis.close();
		
		return buffer;	
	}
	
	public boolean validateKey(String key_file) throws IOException {
		
		File key = new File(key_file);
		FileInputStream fis = new FileInputStream(key);
		byte[] buffer = new byte[KEYSIZE_BYTES];
		fis.skip(VALIDATE_DATA.length);
		fis.read(buffer);
		fis.close();
		
		if(buffer.length == KEYSIZE_BYTES){
			return true;
		}
		
		return false;		
	}
	
	public void copyKey(String key_file){
		
	}
	
}
