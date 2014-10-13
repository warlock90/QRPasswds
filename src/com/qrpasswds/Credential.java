package com.qrpasswds;

public class Credential {
	
	public String type;
	public String user;
	public String pass;
	
	public Credential(){};
	
	public Credential(String t, String u, String p){
		type = t;
		user = u;
		pass = p;
	}

}
