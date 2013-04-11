package com.qrpasswds;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.aes.AESRandomKey;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class GenerateKey extends FragmentActivity {
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		
		AESRandomKey ran_key = new AESRandomKey(this);
		
		try {
			ran_key.generateKey();
			Toast.makeText(this, R.string.key_file_created, Toast.LENGTH_LONG).show();
			
		} catch (NoSuchAlgorithmException e) {
			
		} catch (IOException e) {
			Toast.makeText(this, R.string.error_creating_file, Toast.LENGTH_LONG).show();
		}
		finish();
		
        
    }

}
