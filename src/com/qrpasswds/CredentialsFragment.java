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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CredentialsFragment extends ListFragment {
	
	public final String CHARSET = "utf-8";
	public final String ROOT_XML = "q";
	public final String CHILD_XML = "c";
	public final String TYPE_ATTRIBUTE = "t";
	public final String USER_ATTRIBUTE = "u";
	public final String PASS_ATTRIBUTE = "p";
	
	private LinearLayout main = null;
	private LayoutInflater inflater = null;
	private Button addButton = null;
	private MainActivity mAc = null;
	
	private OnLongClickListener copyToClip;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		copyToClip = new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				EditText view = (EditText)v;
				
				if(view.getText().toString().trim().length()>0) {
					
				    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mAc.getSystemService(Context.CLIPBOARD_SERVICE); 
				    android.content.ClipData clip = android.content.ClipData.newPlainText("copied",view.getText().toString().trim());
				    clipboard.setPrimaryClip(clip);

					Toast.makeText(mAc, "Copied to clipboard", Toast.LENGTH_LONG).show();
					return true;
				}
				return false;
			}};

		ArrayList<Credential> credentials = new ArrayList<Credential>();
		credentials.add(new Credential("a","b","c"));
		credentials.add(new Credential("ab","bc","ca"));
		credentials.add(new Credential("ac","ba","cb"));
		credentials.add(new Credential("aa","bb","cc"));
		CredentialsAdapter adapter = new CredentialsAdapter(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,credentials);
		setListAdapter(adapter);
		
	}

	/*
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){	
		
		View fragView = inflater.inflate(R.layout.scroll, container, false);
		main = (LinearLayout) fragView.findViewById(R.id.main);
		
		mAc = (MainActivity) getActivity();
		mAc.main = main;
		
		addButton = (Button) fragView.findViewById(R.id.add_button);
		addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				addCredential(null,null,null);
			}
	     });
		
		if (savedInstanceState!=null && savedInstanceState.getInt("idCounter")>0){
			
			String retainedData = savedInstanceState.getString("data");
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			try {
				
				DocumentBuilder	db = dbf.newDocumentBuilder();
				Document dom = db.parse(new InputSource(new StringReader(retainedData)));
				
				NodeList nodes = dom.getElementsByTagName(CHILD_XML);
				
				for (int f=0;f<nodes.getLength();f++) {
				
					String type = "", user = "", pass = "";
						
						try { type = nodes.item(f).getAttributes().getNamedItem(TYPE_ATTRIBUTE).getNodeValue();
						} catch(NullPointerException e) {}
						try { user = nodes.item(f).getAttributes().getNamedItem(USER_ATTRIBUTE).getNodeValue();
						} catch(NullPointerException e) {}
						try { pass = nodes.item(f).getAttributes().getNamedItem(PASS_ATTRIBUTE).getNodeValue();
						} catch(NullPointerException e) {}
						
						addCredential(type,user,pass);

				}
			
			} catch (ParserConfigurationException e) {
				Log.e(this.getClass().getSimpleName(), e.toString());
			} catch (SAXException e) {
				Log.e(this.getClass().getSimpleName(), e.toString());
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName(), e.toString());
			}

		}
		
		return fragView;
	}
	
	 @Override
	 public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putString("data", getInput());
	        outState.putInt("idCounter", main.getChildCount());
	    }

	
	public void addCredential(String type, String user, String pass) {

		Credential cred = new Credential(this.getActivity(),main.getChildCount(),type,user,pass);

		main.addView(cred);
		
		if(type==null){		
			mAc.scrollToBottom();
		}
		
	}
	
	public String getInput(){
			
		XmlSerializer serial = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		
		try {
		
			serial.setOutput(writer);
			serial.startDocument(CHARSET, Boolean.TRUE);
			serial.startTag(null, ROOT_XML);
	
			
			for (int f=0;f<main.getChildCount();f++){
				
				LinearLayout credView = (LinearLayout)main.getChildAt(f);
				
				EditText credType = (EditText)credView.findViewById(R.id.credential_type);
				
				EditText credUser = (EditText)credView.findViewById(R.id.user);
				EditText credPass = (EditText)credView.findViewById(R.id.pass);
				
				String type = credType.getText().toString().trim();
				String user = credUser.getText().toString().trim();
				String pass = credPass.getText().toString().trim();
				
				serial.startTag(null, CHILD_XML);
				
				if (type.length()!=0) serial.attribute(null, TYPE_ATTRIBUTE, type);
				if (user.length()!=0) serial.attribute(null, USER_ATTRIBUTE, user);
				if (pass.length()!=0) serial.attribute(null, PASS_ATTRIBUTE, pass);
				
				serial.endTag(null, CHILD_XML);
								
			}
			
			serial.endTag(null, ROOT_XML);
			serial.endDocument();
			serial.flush();
		
		} catch(IOException e) {
			Log.e(this.getClass().getSimpleName(), e.toString());
		}		
		
		return writer.toString();
	}
	
	public int getInputLength(){
		return 0;
	}
	
	private class Credential extends LinearLayout{
		
		public Credential(Context context, int viewId, String type, String user, String pass) {
			super(context);
			
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.credentials, this);	
	
			ImageButton removeThis = (ImageButton) findViewById(R.id.delete_cred);
			removeThis.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					
					LinearLayout credential = (LinearLayout) v.getParent().getParent();
					LinearLayout wrapper = (LinearLayout) credential.getParent();
					
					wrapper.removeView(credential);
				}
		     });
			
			EditText credType = (EditText) this.findViewById(R.id.credential_type);
			EditText credUser = (EditText) this.findViewById(R.id.user);
			EditText credPass = (EditText) this.findViewById(R.id.pass);
			
			credType.setOnLongClickListener(copyToClip);
			credUser.setOnLongClickListener(copyToClip);
			credPass.setOnLongClickListener(copyToClip);

			if (type!=null && !type.equals("<QR3mpty/>")) {
				credType.setText(type);
			}
			
			if (user!=null && !user.equals("<QR3mpty/>")) {
				credUser.setText(user);
			}
			
			if (pass!=null && !pass.equals("<QR3mpty/>")) {
				credPass.setText(pass);
			}
	
			
		}
	}*/

	
}
