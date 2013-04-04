package com.aes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;

import android.content.Context;

public class AES_random_key {

	private final String algorithm = "AES";
    private final int keysize = 128;
    private final String filename = "QRPass.key";
    private final byte[] validate_data = "CorrectQRPassKey".getBytes();
    
    private Context context;
    
    public AES_random_key(Context refContext) throws UnsupportedEncodingException{
    	context = refContext;	
    }
    	
	public void generate_key() throws NoSuchAlgorithmException, IOException {
		
		byte[] random_bytes = null;
		
		KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
        kgen.init(keysize);
        random_bytes = kgen.generateKey().getEncoded();
        
        store_key(random_bytes,context);
	}
	
	public void store_key(byte[] key, Context ctx) throws IOException {
		
		FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
		fos.write(validate_data);
		fos.write(key, validate_data.length, key.length);
		fos.close();
	}
	
	public byte[] get_key() throws IOException{
		
		FileInputStream fis = context.openFileInput(filename);
		byte[] buffer = new byte[keysize/8];
		fis.read(buffer,validate_data.length,buffer.length);
		
		return buffer;	
	}
	
	public boolean validate_key(String key_file) throws IOException{
		
		FileInputStream fis = context.openFileInput(key_file);
		byte[] buffer = new byte[validate_data.length];
		fis.read(buffer,0,buffer.length);
		
		if(buffer.equals(validate_data)){
			return true;
		}
		
		return false;		
	}
	
}
