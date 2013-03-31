package com.aes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;

import android.content.Context;

public class AES_random_key {

	private final String algorithm = "AES";
    private final int keysize = 128;
    private final String filename = "key";
    private Context context;
    
    public AES_random_key(Context refContext){
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
		fos.write(key);
		fos.close();
	}
	
	public byte[] get_key() throws IOException{
		
		FileInputStream fis = context.openFileInput(filename);
		byte[] buffer = null;
		fis.read(buffer);
		
		return buffer;	
	}
	
}
