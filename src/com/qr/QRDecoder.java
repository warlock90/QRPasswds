package com.qr;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;


public class QRDecoder {

	
	public String decode(String qrImagePath) throws NullPointerException, NotFoundException, ChecksumException, FormatException{
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		Bitmap bitmap = BitmapFactory.decodeFile(qrImagePath,options);
		
		int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
	
		LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
		BinaryBitmap binary = new BinaryBitmap(new HybridBinarizer(source));
		
		bitmap.recycle();
		
		QRCodeReader reader = new QRCodeReader();
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		
		Result result = reader.decode(binary,hints);
		
		return result.toString();
		
	}
	
}
