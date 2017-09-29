package com.example;

import java.io.Serializable;


public class model implements Serializable {
	private static final long serialVersionUID = 1L;
	private String Inhalt;

	public String getInhalt() {
		return Inhalt;
	}

	public void setInhalt(String inhalt) {
		this.Inhalt = inhalt;
	}
	
	
	public model(){
		setInhalt("");
	}
	public model(String x){
		setInhalt(x);
	}
	
	
}
