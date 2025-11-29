package vn.hn.hncoreservice.data.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserUpdateRequest {
	
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
			message = "Your password must be at least 8 characters long and contain at least one lowercase letter (a-z), one uppercase letter (A-Z), and one digit (0-9)."
	)
	private String password;
	
	private String fullName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dob;
	
	private Set<String> roles;
	
}
