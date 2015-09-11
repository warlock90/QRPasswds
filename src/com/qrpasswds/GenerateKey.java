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
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.aes.AESRandomKey;

public class GenerateKey extends FragmentActivity {
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		
		AESRandomKey ran_key = new AESRandomKey(this);
		
		try {
			ran_key.generateKey();
			Toast.makeText(this, R.string.key_file_created, Toast.LENGTH_LONG).show();
			
		} catch (NoSuchAlgorithmException e) {
			//cannot happen
			
		} catch (IOException e) {
			
			Log.e(this.getClass().getSimpleName(), e.toString());
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.error_creating_file)
	               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                      
	                   }
	               })
	               .show();
		}
		finish();
		
        
    }

}
