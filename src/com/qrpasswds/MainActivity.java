package com.qrpasswds;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qr.QREncoder;

public class MainActivity extends FragmentActivity {
	
	private final String FILENAME = "QRPass.key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onResume(){
		super.onResume();
		
		File keyfile = this.getFileStreamPath(FILENAME);
		
		if (!keyfile.exists()){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle(R.string.key_file_missing_title)
	        	   .setMessage(R.string.key_file_missing_message)
	               .setNeutralButton(R.string.open_settings, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       Intent intent = new Intent(MainActivity.this,Preferences.class);
	                       startActivity(intent);
	                   }
	               }).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
            	Intent intent = new Intent(this, Preferences.class);
                this.startActivity(intent);
                break;
            case R.id.scan:
            	IntentIntegrator integrator = new IntentIntegrator(this);
            	integrator.initiateScan();
            case R.id.actionbar_create_qr:
            	createPressed(new View(this));
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
		    System.out.println(scanResult.getContents());
		  }
	}
	
	public void createPressed(View v){
		new EncryptEncode().execute();
	}
	
	
	
	public void encryptionEncodingFinished(boolean success){
		
		if (success){		
			Toast.makeText(this, R.string.qr_created, Toast.LENGTH_SHORT).show();
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		}
		else {
			Toast.makeText(this, R.string.error_creating_qr, Toast.LENGTH_LONG).show();
		}
	}
		
	private class EncryptEncode extends AsyncTask<Void, Void, Boolean >{

		@Override
		protected Boolean doInBackground(Void... params) {

			Scroll scroll = (Scroll) getSupportFragmentManager().findFragmentById(R.id.scroll_fragment);
			QREncoder encoder = new QREncoder();
			
			try {
				encoder.createQR(encoder.encode(scroll.getInput()));
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
