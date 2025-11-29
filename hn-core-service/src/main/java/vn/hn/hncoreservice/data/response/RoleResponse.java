package vn.hn.hncoreservice.data.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
	String name;
	String displayName;
	String description;
	Set<PermissionResponse> permissions;
}
