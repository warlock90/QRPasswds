package com.qrpasswds;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aes.AESEncryption;
import com.google.zxing.WriterException;
import com.qr.QREncoder;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;

public class EncryptEncode extends IntentService {

	private String input;
	private String filename;
	
	public EncryptEncode() {
		super("EncryptEncode");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		boolean success = executeService(input);
		
		Intent broadcast = new Intent();
		broadcast.setAction("com.QRPasswds.MESSAGE_PROCESSED");
		broadcast.putExtra("Executed", true);
		broadcast.putExtra("Success", success);
		sendOrderedBroadcast(broadcast,null);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    input = intent.getStringExtra("Data");
	    filename = intent.getStringExtra("Filename");
		return super.onStartCommand(intent,flags,startId);
	}

	private boolean executeService(String data){
		
		QREncoder encoder = new QREncoder(getApplicationContext());
		AESEncryption aes = new AESEncryption(getApplicationContext());
		
		try {
			encoder.createQR(encoder.encode(aes.aes_encrypt(data)), filename);
			
			File QRDirectory = new File(this.getExternalFilesDir(Enivronment.DIRECTORY_PICTURES), filename);
			MediaScannerConnection.scanFile(getApplicationContext(), new String[] {QRDirectory.getAbsolutePath()},null, null);
			
			return true;
			
		} catch (IOException e) {
			return false;
		} catch (WriterException e) {
			return false;
		} catch (InvalidKeyException e) {
			return false;
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (NoSuchPaddingException e) {
			return false;
		} catch (IllegalBlockSizeException e) {
			return false;
		} catch (BadPaddingException e) {
			return false;
		} catch (ParserConfigurationException e) {
			return false;
		} catch (SAXException e) {
			return false;
		}	
	}
}
