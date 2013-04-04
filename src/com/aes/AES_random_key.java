package com.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import android.content.Context;


public class AES_random_key {

	private final String algorithm = "AES";
    private final int keysize_bits = 128;
    private final int keysize_bytes = keysize_bits / 8;
    private final String filename = "QRPass.key";
    private final byte[] validate_data = "QRPassKey".getBytes();
    
    private Context context;
    
    public AES_random_key(Context refContext) {
    	context = refContext;	
    }
    	
	public void generate_key() throws IOException, NoSuchAlgorithmException {
		
		byte[] random_bytes = new byte[keysize_bytes];
		
		KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
        kgen.init(keysize_bits);
        random_bytes = kgen.generateKey().getEncoded();
        
        store_key(random_bytes,context);
	}
	
	public void store_key(byte[] key, Context ctx) throws IOException {
		
		FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
		fos.write(validate_data);
		fos.flush();
		fos.write(key);
		fos.close();
	}
	
	public byte[] get_key() throws IOException{
		
		FileInputStream fis = context.openFileInput(filename);
		byte[] buffer = new byte[keysize_bytes];
		fis.read(buffer,validate_data.length,buffer.length);
		fis.close();
		
		return buffer;	
	}
	
	public boolean validate_key(String key_file) throws IOException {
		
		File key = new File(key_file);
		FileInputStream fis = new FileInputStream(key);
		byte[] buffer = new byte[validate_data.length];
		fis.read(buffer,0,buffer.length);
		fis.close();
		
		if(buffer.equals(validate_data)){
			return true;
		}
		
		return false;		
	}
	
	public void copy_key(String key_file){
		
	}
	
}
