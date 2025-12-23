package vn.hn.hncoreservice.data.request;

import lombok.Data;

@Data
public class LogOutRequest {
	private String accessToken;   // Optional - có thể lấy từ header
	private String refreshToken;  // Required
}
