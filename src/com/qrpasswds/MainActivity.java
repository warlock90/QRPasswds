package com.qrpasswds;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.aes.AES_random_key;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements KeyMissingDialog.NoticeDialogListener {
	
	private final String filename = "QRPass.key";
	
	private LinearLayout main = null;
	private LayoutInflater inflater = null;
	private ScrollView scroll = null;
	
	private int id_counter = 0;
	
	private final int find_file = 1;
	private AES_random_key ran_key = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		scroll = (ScrollView) findViewById(R.id.scroll_view);
		main = (LinearLayout) findViewById(R.id.main);
		
		add_credential();
	
	}
	
	public void onResume(){
		super.onResume();
		
		File keyfile = this.getFileStreamPath(filename);
		
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
	
	public void add_credential(){
		Credential cred = new Credential(this,id_counter);
		main.addView(cred);
	}
	
	public void add_pressed(View v){
		
		add_credential();

		scroll.post(new Runnable() {            
		    @Override
		    public void run() {
		           scroll.scrollTo(0, main.getBottom()); 
		    }
		});
		
	}
	
	public void create_pressed(View v){
		System.out.println(get_input());
	}
	
	public String get_input(){
		
		String input = "";
		
		for (int f=0;f<id_counter;f++){
			
			LinearLayout cred_view = (LinearLayout)main.findViewWithTag("cred"+f);
			LinearLayout cred_wrapper = (LinearLayout)cred_view.getChildAt(0);
			
			EditText cred_type = (EditText)cred_wrapper.getChildAt(0);
			
			LinearLayout user_pass_wrapper = (LinearLayout)cred_wrapper.getChildAt(1);
			
			EditText user = (EditText)user_pass_wrapper.getChildAt(0);
			EditText pass = (EditText)user_pass_wrapper.getChildAt(1);
			
			input += cred_type.getText().toString()+"\n"+user.getText().toString()+"\t"+pass.getText().toString()+"\n";
		}
		
		return input;
	}
	
	@Override
	public void onCreateClick() {
		ran_key = new AES_random_key(this);
		
		try {
			ran_key.generate_key();
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
        startActivityForResult(intent, find_file);
  
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent result){
		
		if ( requestCode == find_file && resultCode == RESULT_OK ){
			
			ran_key = new AES_random_key(this);
			String result_file = result.getData().getPath();
			
			try{
				boolean flag = ran_key.validate_key(result_file);
				
				if (flag) {
					ran_key.copy_key(result_file);
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
	
	private class Credential extends LinearLayout{

		public Credential(Context context, int viewId) {
			super(context);
			this.setTag("cred"+viewId);
			id_counter++;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.credentials, this);	
		}
		
	}

	
}
