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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.aes.AESRandomKey;

public class ExportKey extends FragmentActivity {
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.export_key_warning)
        	   .setIcon(R.drawable.ic_alerts_and_states_warning)
        	   .setMessage(R.string.export_key_message)
               .setPositiveButton(R.string.export, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                	   AESRandomKey ran_key = new AESRandomKey(ExportKey.this);
                       
                	   try {
                		   ran_key.exportKey(null);
                		   Toast.makeText(ExportKey.this, R.string.key_exported, Toast.LENGTH_LONG).show();
                		   finish();
                		   
                       } catch (IOException e) {
                    	   
                    	   Log.e(this.getClass().getSimpleName(), e.toString());
                    	   
                    	   AlertDialog.Builder builder = new AlertDialog.Builder(ExportKey.this);
               	        	builder.setMessage(R.string.error_exporting_key)
               	               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
               	                   public void onClick(DialogInterface dialog, int id) {
               	                	   finish();
               	                   }
               	               })
               	               .show();
                    	   
                       }
                	   
                	   
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   finish();
                   }
               
               })
               .setOnCancelListener(new DialogInterface.OnCancelListener() {         
            	   	@Override
            	   	public void onCancel(DialogInterface dialog) {
            	   		finish();
            	   	}})
               .show();
        
	}

}
