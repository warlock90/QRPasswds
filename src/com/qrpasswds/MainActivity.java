package com.qrpasswds;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	public void onResume(){
		super.onResume();
		
	}

	public void onStop(){
		super.onStop();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class Credentials extends View{

		public Credentials(Context context,ViewGroup viewgroup) {
			super(context);
			Credentials.inflate(this.getContext(), R.layout.credentials, viewgroup);
		}
		
	}

}

