package vn.hn.hncoreservice.data.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserUpdateResponse {
	private Long id;
	
	private String username;
	
	private String fullName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dob;
	
	private String address;
	
	private Set<String> roles;
	
}
