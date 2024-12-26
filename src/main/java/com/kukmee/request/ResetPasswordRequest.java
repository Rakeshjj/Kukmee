package com.kukmee.request;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {

	@NotBlank
	private String newPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
