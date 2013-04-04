package com.qrpasswds;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class KeyMissingDialog extends DialogFragment  {

	NoticeDialogListener dialogListener;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_title)
        	   .setMessage(R.string.alert_message)
               .setPositiveButton(R.string.alert_create, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialogListener.onCreateClick();
                   }
               })
               .setNegativeButton(R.string.alert_import, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dialogListener.onImportClick();
                   }
               
               });

        return builder.create();
    }
	
	public interface NoticeDialogListener {
        public void onCreateClick();
        public void onImportClick();
    }
	
	@Override
    public void onAttach(Activity activity) {
        
		super.onAttach(activity);
        dialogListener = (NoticeDialogListener) activity;
	
	}

	
	
 }
	
