package com.qrpasswds;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ExportKey extends FragmentActivity {
	
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.export_key_title)
        	   .setMessage(R.string.export_key_message)
               .setPositiveButton(R.string.export_key_export, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //todo export key
                	   finish();
                	   
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   finish();
                   }
               
               })
               .show();
        
    }

}
