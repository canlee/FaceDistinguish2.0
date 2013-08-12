package com.invindible.facetime.model;

import java.awt.image.BufferedImage;

public class UserByAdmin {
	private String username;
	private BufferedImage bfi;
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
