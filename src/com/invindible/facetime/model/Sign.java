package com.invindible.facetime.model;

import java.awt.image.BufferedImage;

public class Sign {
	private String signdate;
	private String username;
	private BufferedImage bfi;
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public BufferedImage getBfi() {
		return bfi;
	}
	public void setBfi(BufferedImage bfi) {
		this.bfi = bfi;
	}
}
