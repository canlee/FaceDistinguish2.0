package com.invindible.facetime.model;

import java.awt.image.BufferedImage;

public class UserDeleteModel {

	private String username;
	private int id;
	private BufferedImage bfi;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BufferedImage getBfi() {
		return bfi;
	}
	public void setBfi(BufferedImage bfi) {
		this.bfi = bfi;
	}

}
