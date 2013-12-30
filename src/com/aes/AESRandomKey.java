package com.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.util.Xml;

public class AESRandomKey {

	private final String ALGORITHM = "AES";
    private final String CHARSET = "utf-8";
	private final String FOLDER = "QRPasswds";
    private final int KEYSIZE_BITS = 128;
    private final int KEYSIZE_BYTES = KEYSIZE_BITS / 8;
    private final String FILENAME = "QRPass.key";
    
    private Context context;
    
    public AESRandomKey(Context refContext) {
    	context = refContext;	
    }
    	
	public void generateKey() throws IOException, NoSuchAlgorithmException {
		
		byte[] randomBytes = new byte[KEYSIZE_BYTES];
		
		KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        kgen.init(KEYSIZE_BITS);
        randomBytes = kgen.generateKey().getEncoded();
        
        storeKey(randomBytes,context);
	}
	
	public void storeKey(byte[] key, Context ctx) throws IOException {
		
		FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		XmlSerializer serial = Xml.newSerializer();
		
		serial.setOutput(fos, CHARSET);
		serial.startDocument(CHARSET, Boolean.TRUE);
		serial.startTag(null, FOLDER);
		serial.text(Base64.encodeToString(key, Base64.DEFAULT));
		serial.endTag(null, FOLDER);
		serial.endDocument();
		serial.flush();
		
		fos.close();
	}
	
	public byte[] getKey() throws IOException, ParserConfigurationException, SAXException{
		
		FileInputStream fis = context.openFileInput(FILENAME);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(fis);
		
		String pass = dom.getElementsByTagName(FOLDER).item(0).getTextContent();
						
		return Base64.decode(pass, Base64.DEFAULT);
	}
	
	public boolean validateKey(InputStream keyStream) throws IOException, SAXException, ParserConfigurationException {
				
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(keyStream);
		String pass = dom.getElementsByTagName(FOLDER).item(0).getTextContent();
		
		int byteLength = Base64.decode(pass, Base64.DEFAULT).length;
		
		keyStream.close();
		
		if(byteLength == 16 || byteLength == 24 || byteLength == 32){
			return true;
		}
		
		return false;		
	}
		
	public void exportKey(String inputFilename) throws IOException{
		
		FileInputStream in;
		
		if (inputFilename == null) {
			in = context.openFileInput(FILENAME);
		}
		else {
			in = new FileInputStream(new File(inputFilename));
		}
				
		File QRDirectory = new File(Environment.getExternalStorageDirectory(), FILENAME);		
		FileOutputStream out = new FileOutputStream(QRDirectory);
		
		byte[] buffer = new byte[256];
		
		while (in.read(buffer)>0) out.write(buffer);
		
		in.close();
		out.close();
		
	}
	
	public void importKey(InputStream filepath, Context ctx) throws IOException{
		
		FileOutputStream out = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		
		byte[] buffer = new byte[256];
	
		while (filepath.read(buffer)>0) out.write(buffer);
		
		filepath.close();
		out.close();
		
		
	}
	
}
