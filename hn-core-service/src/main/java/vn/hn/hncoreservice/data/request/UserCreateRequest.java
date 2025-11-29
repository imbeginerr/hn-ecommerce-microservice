package vn.hn.hncoreservice.data.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserCreateRequest {
	
	@NotBlank
	@Pattern(
			regexp = "^(?=.*[a-z]).{6,}$",
			message = "INVALID_USERNAME"
	)
	private String username;
	
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
			message = "INVALID_PASSWORD"
	)
	private String password;
	
	private String fullName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dob;
	
	private Set<String> roles;
}
