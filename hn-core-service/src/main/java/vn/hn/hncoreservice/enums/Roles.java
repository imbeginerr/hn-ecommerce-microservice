package vn.hn.hncoreservice.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Roles {
	ADMIN("ADMIN"),
	USER("USER");
	
	private final String value;
	
	Roles(String value) {
		this.value = value;
	}
}
