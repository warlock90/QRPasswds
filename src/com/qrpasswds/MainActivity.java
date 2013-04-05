package com.qrpasswds;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aes.AESRandomKey;
import com.google.zxing.WriterException;
import com.qr.QREncoder;


public class MainActivity extends FragmentActivity implements KeyMissingDialog.NoticeDialogListener {
	
	private final String FILENAME = "QRPass.key";
	
	private LinearLayout main = null;

	private LayoutInflater inflater = null;
	private ScrollView scroll = null;

	private int idCounter = 0;
	
	private final int FIND_FILE = 1;
	private AESRandomKey ranKey = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		scroll = (ScrollView) findViewById(R.id.scroll_view);
		main = (LinearLayout) findViewById(R.id.main);
		
		addCredential();
	
	}
	
	public void onResume(){
		super.onResume();
		
		File keyfile = this.getFileStreamPath(FILENAME);
		
		if (!keyfile.exists()){
			KeyMissingDialog dialog = new KeyMissingDialog();
			dialog.setCancelable(false);
			dialog.show(getSupportFragmentManager(), "Dialog");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addCredential(){
		Credential cred = new Credential(this,idCounter);
		main.addView(cred);
	}
	
	public void addPressed(View v){
		
		addCredential();

		scroll.post(new Runnable() {            
		    @Override
		    public void run() {
		           scroll.scrollTo(0, main.getBottom()); 
		    }
		});
	}
	
	public void createPressed(View v){
		new EncryptEncode().execute();
	}
	
	public String getInput(){
		
		String input = "";
		
		for (int f=0;f<idCounter;f++){
			
			LinearLayout credView = (LinearLayout)main.findViewWithTag("cred"+f);
			LinearLayout credWrapper = (LinearLayout)credView.getChildAt(0);
			
			EditText credType = (EditText)credWrapper.getChildAt(0);
			
			LinearLayout userPassWrapper = (LinearLayout)credWrapper.getChildAt(1);
			
			EditText credUser = (EditText)userPassWrapper.getChildAt(0);
			EditText credPass = (EditText)userPassWrapper.getChildAt(1);
			
			String type = credType.getText().toString().trim();
			String user = credUser.getText().toString().trim();
			String pass = credPass.getText().toString().trim();
			
			if (type.equals("")) type = "empty";
			if (user.equals("")) user = "empty";
			if (pass.equals("")) pass = "empty";			
			
			input += type+"\n"+user+"\n"+pass+"\n";
		}
		
		return input;
	}
	
	@Override
	public void onCreateClick() {
		ranKey = new AESRandomKey(this);
		
		try {
			ranKey.generateKey();
			Toast key_file_created = Toast.makeText(this, R.string.key_file_created, Toast.LENGTH_SHORT);
			key_file_created.show();
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		
		} catch (IOException e) {
			Toast error_creating_file = Toast.makeText(this, R.string.error_creating_file, Toast.LENGTH_LONG);
			error_creating_file.show();
		}
	}

	@Override
	public void onImportClick() {
		
		Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, FIND_FILE);
  
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent result){
		
		if ( requestCode == FIND_FILE && resultCode == RESULT_OK ){
			
			ranKey = new AESRandomKey(this);
			String resultFile = result.getData().getPath();
			
			try{
				boolean flag = ranKey.validateKey(resultFile);
				
				if (flag) {
					ranKey.copyKey(resultFile);
				}
				else {
					Toast not_valid_file = Toast.makeText(this, R.string.not_valid_file, Toast.LENGTH_LONG );
					not_valid_file.show();
				}
				
			} catch(IOException e){ 
				Toast error = Toast.makeText(this, R.string.error_reading_file, Toast.LENGTH_LONG);
				error.show();
				}
		}
	}
	
	public void encryptionEncodingFinished(boolean success){
		
		if (success){		
			Toast qr_created = Toast.makeText(this, R.string.qr_created, Toast.LENGTH_SHORT);
			qr_created.show();
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		}
		else {
			Toast error_creating_qr = Toast.makeText(this, R.string.error_creating_qr, Toast.LENGTH_LONG);
			error_creating_qr.show();
		}
	}
	
	private class Credential extends LinearLayout{

		public Credential(Context context, int viewId) {
			super(context);
			this.setTag("cred"+viewId);
			idCounter++;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.credentials, this);	
		}
		
	}
	
	private class EncryptEncode extends AsyncTask<Void, Void, Boolean >{

		@Override
		protected Boolean doInBackground(Void... params) {

			QREncoder encoder = new QREncoder();
			
			try {
				encoder.createQR(encoder.encode(getInput()));
				return true;
			} catch (IOException e) {
				return false;
			} catch (WriterException e) {
				return false;
			}		
		}
		
		public void onPostExecute(Boolean result){
			encryptionEncodingFinished(result);
		}
	}
}
