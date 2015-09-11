/*******************************************************************************
 * Copyright (c) 2013-2014 warlock9_0
 * 
 * This file is part of QRPasswds.
 * 
 * QRPasswds is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * QRPasswds is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with QRPasswds.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.qrpasswds;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aes.AESEncryption;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qr.QRDecoder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends FragmentActivity {
	
	private final String KEY_FILENAME = "QRPass.key";
	private final String ACTION_RESP = "com.QRPasswds.MESSAGE_PROCESSED";
	private final int FIND_FILE = 1;

	private CredentialsFragment scroll = null;
	
	private LinearLayout loadingView = null;
	private LinearLayout activityLayout = null;
	
	private boolean isLoading;
	private String scannedFilename;
	
	private EncryptEncodeReceiver receiver = null;
	private IntentFilter filter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		scroll = (CredentialsFragment) getSupportFragmentManager().findFragmentById(R.id.scroll_fragment);

		loadingView = (LinearLayout) findViewById(R.id.loading);
		activityLayout = (LinearLayout) findViewById(R.id.activity_layout);
				
		if (savedInstanceState!=null && savedInstanceState.getBoolean("isLoading")){
			loading(true);
		}
		
		filter = new IntentFilter(ACTION_RESP);
		filter.setPriority(1);
		receiver = new EncryptEncodeReceiver();
	
	}
	
	public void onResume(){
		super.onResume();

		File keyfile = this.getFileStreamPath(KEY_FILENAME);
		
		if (!keyfile.exists()){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle(R.string.key_file_missing_title)
	        	   .setIcon(R.drawable.ic_alerts_and_states_warning)
	        	   .setMessage(R.string.key_file_missing_message)
	               .setPositiveButton(R.string.generate, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       Intent intent = new Intent(MainActivity.this,GenerateKey.class);
	                       startActivity(intent);
	                   }
	               })
	               .setNegativeButton(R.string.import_dialog, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       Intent intent = new Intent(MainActivity.this,ImportKey.class);
	                       startActivity(intent);
	                   }
	               })
	               .setCancelable(false)
	               .show();
		}

		registerReceiver(receiver,filter);
		
	}
	
	public void onPause(){
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	@Override
	 public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putBoolean("isLoading", isLoading);
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
			switch (item.getItemId()) {
			
            	case R.id.scan:
            		IntentIntegrator integrator = new IntentIntegrator(this);
            		integrator.initiateScan();
            		return true;
            		
            	case R.id.actionbar_create_qr:
            		createPressed(new View(this));
            		return true;
            		
            	case R.id.delete:
            		if (scroll.credentials.size()>0){
            			AlertDialog.Builder builder = new AlertDialog.Builder(this);
            			builder.setTitle(R.string.clear_dialog_title)
            			.setIcon(R.drawable.ic_alerts_and_states_warning)
    	        	   		.setMessage(R.string.clear_message)
    	        	   		.setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
    	        	   			public void onClick(DialogInterface dialog, int id) {
    	        	   				scroll.credentials.clear();
    	        	   				scroll.adapter.notifyDataSetChanged();
    	        	   			}
    	        	   		})
    	        	   		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    	        	   			public void onClick(DialogInterface dialog, int id) {
    	                    
    	        	   			}
    	        	   		})
    	        	   		.show();
            			
            			scannedFilename = null;
            		}
            		return true;
            		
            	case R.id.import_key:
            		Intent importKey = new Intent(this, ImportKey.class);
                	this.startActivity(importKey);
                	return true;
                	
            	case R.id.export_key:
            		Intent exportKey = new Intent(this, ExportKey.class);
            		this.startActivity(exportKey);
            		return true;
            		
            	case R.id.scan_file:
            		Intent scan = new Intent();
            		scan.setType("image/*");
            		scan.setAction(Intent.ACTION_GET_CONTENT);
                    try {
                    	startActivityForResult(scan, FIND_FILE);
                    }
                    catch(ActivityNotFoundException e){
                    	
                    	Log.e(this.getClass().getSimpleName(), e.toString());

                    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    	builder.setMessage(R.string.file_manager_not_found_file)
            	                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            	                   public void onClick(DialogInterface dialog, int id) {
            	                      
            	                   }
            	               })
            	               .show();
                    }
                    return true;

				case R.id.add_button:
                    addCredentialDialog();
					return true;
                    
            	default:
            		return super.onOptionsItemSelected(item);
        }
		
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent result) {
	   	        	
		String data = null;
		boolean error = false;
		
		  if ( requestCode == FIND_FILE && resultCode == RESULT_OK ){
			  
			  QRDecoder decoder = new QRDecoder();
			  
			  
			  try {
				  data = decoder.decode(getContentResolver().openInputStream(result.getData()));
				  scannedFilename = getTitleFromUri(this, result.getData());
							
				
			  }catch (Exception e) {

				  Log.e(this.getClass().getSimpleName(), e.toString());
				  
				  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	   	        	builder.setMessage(R.string.not_valid_qr)
	   	               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	   	                   public void onClick(DialogInterface dialog, int id) {
	   	                	
	   	                   }
	   	               })
	   	               .show();

	   	        	error = true;
			  }		  
		  }
		  
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, result);
		  
		  if (scanResult != null) data = scanResult.getContents();
		  
		  
		  if (data!=null && !error) {
			  
			  AESEncryption aes = new AESEncryption(this);
			  loading(true);
			  			
			  try {
				  
				  String retainedData = aes.aes_decrypt(data);

				  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					
					try {
						
						DocumentBuilder	db = dbf.newDocumentBuilder();
						Document dom = db.parse(new InputSource(new StringReader(retainedData)));
						
						NodeList nodes = dom.getElementsByTagName(scroll.CHILD_XML);

						for (int f=0;f<nodes.getLength();f++) {
						
							String type, user, pass;
								
								try { type = nodes.item(f).getAttributes().getNamedItem(scroll.TYPE_ATTRIBUTE).getNodeValue();
								} catch(NullPointerException e) {
                                    type = "";
                                }
								try { user = nodes.item(f).getAttributes().getNamedItem(scroll.USER_ATTRIBUTE).getNodeValue();
								} catch(NullPointerException e) {
                                    user = "";
                                }
								try { pass = nodes.item(f).getAttributes().getNamedItem(scroll.PASS_ATTRIBUTE).getNodeValue();
								} catch(NullPointerException e) {
                                    pass = "";
                                }

								scroll.addCredential(type,user,pass);
						}
					
					} catch (ParserConfigurationException e) {
						Log.e(this.getClass().getSimpleName(), e.toString());
					} catch (SAXException | IOException e) {
						Log.e(this.getClass().getSimpleName(), e.toString());
						throw new Exception();
					}

              } catch (IllegalArgumentException e) {
				  
				  Log.e(this.getClass().getSimpleName(), e.toString());
				  
				  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	   	        	builder.setMessage(R.string.not_your_key)
	   	               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	   	                   public void onClick(DialogInterface dialog, int id) {
	   	                      
	   	                   }
	   	               })
	   	               .show();
			 
			  } catch (Exception e) {
				  
				  Log.e(this.getClass().getSimpleName(), e.toString());

				  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	   	        	builder.setMessage(R.string.error_scanning)
	   	               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	   	                   public void onClick(DialogInterface dialog, int id) {
	   	                      
	   	                   }
	   	               })
	   	               .show();
				  
			  } 
		   
		  }
		  
		  scroll.adapter.notifyDataSetChanged();
		  loading(false);
	}

    private void addCredentialDialog(){

        scroll.addCredential("","","");
        scroll.adapter.notifyDataSetChanged();
    }
	
	public void createPressed(View v){
		
			if (scroll.credentials.size() > 0)	{
				
				LayoutInflater inflater = this.getLayoutInflater();
				View inflaterView = inflater.inflate(R.layout.filename, activityLayout, false);
				
				SimpleDateFormat sd = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
				sd.setTimeZone(TimeZone.getDefault());
				String formatted_sd = sd.format(new Date());
				
				final RadioButton fdefault = (RadioButton) inflaterView.findViewById(R.id.filename_default);
				
				final RadioButton fcustom = (RadioButton) inflaterView.findViewById(R.id.filename_custom);
				fcustom.setText(formatted_sd+"_");
				
				final EditText fcustomPart = (EditText) inflaterView.findViewById(R.id.filename_custom_part);
				final TextView fextention = (TextView) inflaterView.findViewById(R.id.file_extention);
				
				if (scannedFilename != null && !scannedFilename.startsWith("Q")) {
					
					String fcustomPartRecovered = "";
					
					for (int f=1;f<scannedFilename.split("_").length;f++) {
						
						fcustomPartRecovered += scannedFilename.split("_")[f] + "_";
					}
					
					fcustom.setText(scannedFilename.split("_")[0]+"_");
					fcustomPart.setText(fcustomPartRecovered.substring(0, fcustomPartRecovered.length()-5));
					fcustom.setChecked(true);
					fdefault.setChecked(false);
					fcustomPart.setEnabled(true);
					
				}
				
				fdefault.setOnClickListener(new android.view.View.OnClickListener(){

					@Override
					public void onClick(View v) {
						
						fcustom.setChecked(false);
						fdefault.setChecked(true);
						fcustomPart.setEnabled(false);
						
					}});
				
				fcustom.setOnClickListener(new android.view.View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						fcustom.setChecked(true);
						fdefault.setChecked(false);
						fcustomPart.setEnabled(true);
						
					}
				});
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.qr_filename)
					.setView(inflaterView)
					.setPositiveButton(R.string.ok, new OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							String qrFilename;
							if (fdefault.isChecked()) {
								qrFilename = fdefault.getText().toString().split(" ")[0];
							}
							else {
								qrFilename = fcustom.getText().toString() + fcustomPart.getText().toString().replace(" ", "") + fextention.getText().toString();
							}
							
							
							loading(true);
							
							Intent toReceiver = new Intent(getApplicationContext(),EncryptEncode.class);
							System.out.println(scroll.getInput());
							toReceiver.putExtra("Data", scroll.getInput());
							toReceiver.putExtra("Filename", qrFilename);
							startService(toReceiver);
							
						}})
					.setNegativeButton(R.string.cancel, null)
					.show();				
			
				
			
			}
			else {
				
				Toast.makeText(this, R.string.no_credential_error, Toast.LENGTH_LONG).show();
			}
		}

	
	private void encryptionEncodingFinished(boolean success){
				
		if (success){		
			Toast.makeText(this, R.string.qr_created, Toast.LENGTH_SHORT).show();
		}
		else {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.error_creating_qr)
	               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                      
	                   }
	               })
	               .show();
		}
	}
	
	private void loading(boolean flag){
		
		if (flag) {

			if (activityLayout != null) activityLayout.setVisibility(View.INVISIBLE);

			loadingView.setVisibility(View.VISIBLE);
			isLoading = true;
		}
		else {
			if (activityLayout != null) activityLayout.setVisibility(View.VISIBLE);
			
			loadingView.setVisibility(View.INVISIBLE);
			isLoading = false;
		}
	}
	
	private String getTitleFromUri(Context context, Uri contentUri) {
			
		if(contentUri.getScheme().equals("file")) {
			return contentUri.getLastPathSegment();
		}
		else {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { OpenableColumns.DISPLAY_NAME};
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}
	}
		
	private class EncryptEncodeReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			boolean executed = intent.getBooleanExtra("Executed", false);
			if (executed) {
				loading(false);
				encryptionEncodingFinished(intent.getBooleanExtra("Success", false));
			}
			abortBroadcast();
			
		}
		
	}
	
	
}
