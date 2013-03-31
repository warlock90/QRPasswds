package com.qr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QR_encoder {

	private static int width = 600;
	private static int height = 600;
	private static String folder = "QRPasswds";
	private static String filename = "QR_passwords.png";
	
	public Bitmap encode(String text) throws WriterException{
		
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix mtx;
		Bitmap bm;
		
        mtx = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        bm = Bitmap.createBitmap(mtx.getWidth(), mtx.getHeight(), Bitmap.Config.ARGB_8888);
            
        for (int x = 0; x < mtx.getWidth(); x++) {
        	for (int y = 0; y < mtx.getHeight(); y++) {
                  bm.setPixel(x, y, mtx.get(x, y) ? Color.BLACK : Color.WHITE);
            }      
        }
        
        return bm;
	}
	
	public void create_QR(Bitmap qr) throws IOException{
		
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File QR_directory = new File(Environment.getExternalStorageDirectory().toString()+"/"+folder);
			QR_directory.mkdir();
			FileOutputStream out = new FileOutputStream(QR_directory+"/"+filename);
		    qr.compress(Bitmap.CompressFormat.PNG, 100, out);
		} else {
		    throw new IOException();
		}	
	}
	
}
