package com.qrpasswds;

import java.io.IOException;

import com.google.zxing.WriterException;
import com.qr.QREncoder;

import android.app.IntentService;
import android.content.Intent;

public class EncryptEncode extends IntentService {

	private String input = "";
	
	public EncryptEncode() {
		super("EncryptEncode");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		boolean executed = executeService(input);
		
		Intent broadcast = new Intent();
		broadcast.setAction("com.QRPasswds.MESSAGE_PROCESSED");
		broadcast.putExtra("Executed", executed);
		sendOrderedBroadcast(broadcast,null);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    input = intent.getStringExtra("Data");
		return super.onStartCommand(intent,flags,startId);
	}

	private boolean executeService(String data){
		
		QREncoder encoder = new QREncoder();
		System.out.println("Execute");
		try {
			encoder.createQR(encoder.encode(data));
			return true;
		} catch (IOException e) {
			return false;
		} catch (WriterException e) {
			return false;
		}	
	}
}