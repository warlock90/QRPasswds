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

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.aes.AESEncryption;
import com.google.zxing.WriterException;
import com.qr.QREncoder;

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
			return true;
			
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (WriterException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (InvalidKeyException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (NoSuchAlgorithmException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (NoSuchPaddingException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (IllegalBlockSizeException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (BadPaddingException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (ParserConfigurationException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		} catch (SAXException e) {
			Log.e(this.getClass().getSimpleName(),e.toString());
			return false;
		}	
	}
}
