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
package com.qr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QREncoder {

	private final int WIDTH = 900;
	private final int HEIGHT = 900;
	private final String FOLDER = "QRPasswds";
	
	private Context context;
	    
    public QREncoder(Context refContext) {
    	context = refContext;	
    }
		
	public Bitmap encode(String text) throws WriterException{
		
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix mtx;
		Bitmap bm;
		
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		
		mtx = writer.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
		
		bm = Bitmap.createBitmap(mtx.getWidth(), mtx.getHeight(), Bitmap.Config.ARGB_8888);
            
        for (int x = 0; x < mtx.getWidth(); x++) {
        	for (int y = 0; y < mtx.getHeight(); y++) {
                  bm.setPixel(x, y, mtx.get(x, y) ? Color.BLACK : Color.WHITE);
            }      
        }
        
        return bm;
	}
	
	public void createQR(Bitmap qr, String filename) throws IOException{
		
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {

			File QRDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+FOLDER);
			System.out.println("Printing parent levels " + QRDirectory.mkdirs());
			File QRFile = new File(QRDirectory,filename);

			FileOutputStream out = new FileOutputStream(QRFile);
		    qr.compress(Bitmap.CompressFormat.PNG, 100, out);
		    
			MediaScannerConnection.scanFile(context, new String[] {QRFile.getAbsolutePath()},null, null);
		    
		} else {
		    throw new IOException();
		}	
	}
	
}
