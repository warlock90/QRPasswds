package com.qrpasswds;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CredentialsFragment extends Fragment {
	
	private LinearLayout main = null;
	private LayoutInflater inflater = null;
	private Button addButton = null;
	private MainActivity mAc = null;
		
	private int idCounter = 0;
			
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){	
		
		View fragView = inflater.inflate(R.layout.scroll, container, false);
		main = (LinearLayout) fragView.findViewById(R.id.main);
		
		mAc = (MainActivity) getActivity();
		mAc.setMain(main);
		
		addButton = (Button) fragView.findViewById(R.id.add_button);
		addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				addCredential(null,null,null);
			}
	     });
		
		if (savedInstanceState!=null && savedInstanceState.getInt("idCounter")>0){
			String[] retainedData = savedInstanceState.getString("data").split("\n");
			for (int f=0;f<retainedData.length;f+=3) {
				addCredential(retainedData[f],retainedData[f+1],retainedData[f+2]);
			}
		}
		
		return fragView;
	}
	
	 @Override
	 public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putString("data", getInput());
	        outState.putInt("idCounter", idCounter);
	    }

	
	public void addCredential(String type, String user, String pass){

		Credential cred = new Credential(this.getActivity(),idCounter,type,user,pass);

		main.addView(cred);
		
		if(type==null){		
			mAc.scrollToBottom();
		}
	}
	
	public String getInput(){
		
		String input = "";
		
		for (int f=0;f<idCounter;f++){
			
			LinearLayout credView = (LinearLayout)main.findViewWithTag("cred"+f);
			
			EditText credType = (EditText)credView.findViewById(R.id.credential_type);
			
			EditText credUser = (EditText)credView.findViewById(R.id.user);
			EditText credPass = (EditText)credView.findViewById(R.id.pass);
			
			String type = credType.getText().toString().trim();
			String user = credUser.getText().toString().trim();
			String pass = credPass.getText().toString().trim();
			
			if (type.length()==0) type = "<QR3mpty/>";
			if (user.length()==0) user = "<QR3mpty/>";
			if (pass.length()==0) pass = "<QR3mpty/>";			
			
			input += type+"\n"+user+"\n"+pass+"\n";
						
		}
		
		return input;
	}
	
	public void setIdCounter(int value){
		idCounter = value;
	}
	
	public int getIdCounter(){
		return idCounter;
	}
	
	private class Credential extends LinearLayout{
		
		public Credential(Context context, int viewId, String type, String user, String pass) {
			super(context);
			this.setTag("cred"+viewId);
			idCounter++;

			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.credentials, this);	

			if (type!=null && !type.equals("<QR3mpty/>")) {
				EditText credType = (EditText) this.findViewById(R.id.credential_type);
				credType.setText(type);
			}
			
			if (user!=null && !user.equals("<QR3mpty/>")) {
				EditText credUser = (EditText) this.findViewById(R.id.user);
				credUser.setText(user);
			}
			
			if (pass!=null && !pass.equals("<QR3mpty/>")) {
				EditText credPass = (EditText) this.findViewById(R.id.pass);
				credPass.setText(pass);
			}
	
			
		}
	}

	
}
