package com.qrpasswds;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class CredentialsAdapter extends ArrayAdapter<Credential> {
	
	private final Context CONTEXT;
	private List<Credential> creds;
	private final OnLongClickListener copyToClip;
	
	public CredentialsAdapter(Context context, int textViewResourceId, List<Credential> cr) {
		super(context, textViewResourceId, cr);
		
		CONTEXT = context;
		creds = cr;
		
		copyToClip = new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				EditText view = (EditText)v;
				
				if(view.getText().toString().trim().length()>0) {
					
				    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) CONTEXT.getSystemService(Context.CLIPBOARD_SERVICE); 
				    android.content.ClipData clip = android.content.ClipData.newPlainText("copied",view.getText().toString().trim());
				    clipboard.setPrimaryClip(clip);

					Toast.makeText(CONTEXT, "Copied to clipboard", Toast.LENGTH_LONG).show();
					return true;
				}
				return false;
			}};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View row = inflater.inflate(R.layout.credentials, parent, false);
	    
	    EditText credType = (EditText) row.findViewById(R.id.credential_type);
		EditText credUser = (EditText) row.findViewById(R.id.user);
		EditText credPass = (EditText) row.findViewById(R.id.pass);
		
		credType.setText(creds.get(position).type);
		credUser.setText(creds.get(position).user);
		credPass.setText(creds.get(position).pass);
		
		credType.setOnLongClickListener(copyToClip);
		credUser.setOnLongClickListener(copyToClip);
		credPass.setOnLongClickListener(copyToClip);		

		return row;
	}


	


}
