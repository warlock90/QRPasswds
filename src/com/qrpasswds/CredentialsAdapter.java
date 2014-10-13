package com.qrpasswds;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class CredentialsAdapter extends ArrayAdapter<Credential> {
	
	private final Context CONTEXT;
	private List<Credential> creds;
	
	public CredentialsAdapter(Context context, int textViewResourceId, List<Credential> cr) {
		super(context, textViewResourceId, cr);
		CONTEXT = context;
		creds = cr;
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

		return row;
	}


	


}
