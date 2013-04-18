package com.qrpasswds;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aes.AESRandomKey;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ImportKey extends Activity {

	private final int FIND_FILE = 1;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
        	startActivityForResult(intent, FIND_FILE);
        }
        catch(ActivityNotFoundException e){
        	Toast.makeText(this, R.string.file_manager_not_found, Toast.LENGTH_LONG).show();
        }
	}
	
	public void onResume(){
		super.onResume();
		finish();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent result){
		
		if ( requestCode == FIND_FILE && resultCode == RESULT_OK ){
			
			AESRandomKey ranKey = new AESRandomKey(this);
			String resultFile = result.getData().getPath();
			
			try{
				
				boolean flag = ranKey.validateKey(resultFile);
				
				if (flag) {
					ranKey.importKey(resultFile, this);
					Toast.makeText(this, R.string.key_imported, Toast.LENGTH_LONG).show();
					finish();
				}
				else {
					Toast.makeText(this, R.string.not_valid_file, Toast.LENGTH_LONG ).show();
					finish();
				}
				
			} catch(IOException e){ 
				Toast.makeText(this, R.string.error_reading_file, Toast.LENGTH_LONG).show();
				finish();
			} catch (SAXException e) {
					Toast.makeText(this, R.string.not_valid_file, Toast.LENGTH_LONG ).show();
					finish();
			} catch (ParserConfigurationException e) {
				Toast.makeText(this, R.string.not_valid_file, Toast.LENGTH_LONG ).show();
				finish();
			}
		}
	}
}
