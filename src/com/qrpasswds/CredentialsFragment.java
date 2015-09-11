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

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class CredentialsFragment extends ListFragment {
	
	public final String CHARSET = "utf-8";
	public final String ROOT_XML = "q";
	public final String CHILD_XML = "c";
	public final String TYPE_ATTRIBUTE = "t";
	public final String USER_ATTRIBUTE = "u";
	public final String PASS_ATTRIBUTE = "p";
		
	public ArrayList<Credential> credentials;
	public CredentialsAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		credentials = new ArrayList<>();
		
		adapter = new CredentialsAdapter(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,credentials);
		setListAdapter(adapter);
		
	}

	public void addCredential(String type, String user, String pass) {

		Credential cred = new Credential(type, user, pass);
		credentials.add(cred);
		
	}
	
	public String getInput(){
			
		XmlSerializer serial = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		
		try {
		
			serial.setOutput(writer);
			serial.startDocument(CHARSET, Boolean.TRUE);
			serial.startTag(null, ROOT_XML);
	
			
			for (int f=0;f<credentials.size();f++){
				
				String type = ((Credential)getListView().getItemAtPosition(f)).type.trim();
				String user = ((Credential)getListView().getItemAtPosition(f)).user.trim();
				String pass = ((Credential)getListView().getItemAtPosition(f)).pass.trim();
				
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
	
}
