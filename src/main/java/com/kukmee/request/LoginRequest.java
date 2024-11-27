package com.kukmee.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NotBlank
	@Size(max = 50)
	private String username;

	@NotBlank
	@Size(max=80)
	private String password;
	
	

}