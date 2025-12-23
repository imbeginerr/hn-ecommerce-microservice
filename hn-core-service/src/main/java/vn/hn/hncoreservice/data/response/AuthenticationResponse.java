package vn.hn.hncoreservice.data.response;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private String token;
	private String refreshToken;
	private boolean authenticated;
}