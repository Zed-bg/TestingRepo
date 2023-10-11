package com.nirvasoft.rp.shared;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NewTokenUserReq {
	private String userId;
	private String password;
	private String comfirmPassword;
	
	public NewTokenUserReq() {
		clearProperty();
	}

	private void clearProperty() {
		userId = "";
		password= "";
		comfirmPassword= "";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComfirmPassword() {
		return comfirmPassword;
	}

	public void setComfirmPassword(String comfirmPassword) {
		this.comfirmPassword = comfirmPassword;
	}
}
