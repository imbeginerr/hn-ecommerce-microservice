package vn.hn.hncoreservice.data.response;

import lombok.Data;

@Data
public class AuthenticationResponse {
	private String token;
	boolean authenticated;
}
