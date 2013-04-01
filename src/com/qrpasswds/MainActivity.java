package com.qrpasswds;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends Activity {
	
	private LinearLayout main = null;
	private LayoutInflater inflater = null;
	private ScrollView scroll = null;
	
	private int id_counter = 0;
	private Button add_btn= null;
	
	private ArrayList<Credential> list = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		scroll = (ScrollView) findViewById(R.id.scroll_view);
		main = (LinearLayout) findViewById(R.id.main);
		
		add_btn = (Button) findViewById(R.id.add_button);
		add_btn.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				add_credential();

				scroll.post(new Runnable() {            
				    @Override
				    public void run() {
				           scroll.scrollTo(0, main.getBottom()); 
				    }
				});
							
			}
		});
		
		list = new ArrayList<Credential>();
		
		add_credential();
	
	}
	
	public void onResume(){
		super.onResume();
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void add_credential(){
		Credential cred = new Credential(this,id_counter);
		list.add(cred);
		main.addView(cred);
	}
	
	private class Credential extends LinearLayout{

		public Credential(Context context, int viewId) {
			super(context);
			this.setId(viewId);
			id_counter++;
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.credentials, this);	
		}
		
	}
}
