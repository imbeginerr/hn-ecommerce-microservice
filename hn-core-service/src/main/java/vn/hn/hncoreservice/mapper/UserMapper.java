package vn.hn.hncoreservice.mapper;

import org.mapstruct.*;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.data.request.UserCreateRequest;
import vn.hn.hncoreservice.data.request.UserUpdateRequest;
import vn.hn.hncoreservice.data.response.UserCreateResponse;
import vn.hn.hncoreservice.data.response.UserResponse;
import vn.hn.hncoreservice.data.response.UserUpdateResponse;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "roles", ignore = true)
	User toEntity(UserCreateRequest request);
	
	@Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
	UserCreateResponse toCreateResponse(User user);
	
	default Set<String> mapRoles(Set<Role> roles) {
		if (roles == null || roles.isEmpty()) {
			return Set.of();
		}
		return roles.stream()
				.map(Role::getName)
				.collect(Collectors.toSet());
	}
	
	@Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
	UserUpdateResponse toUpdateResponse(User user);
	
	@Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
	UserResponse toResponse(User user);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntity(UserUpdateRequest request, @MappingTarget User user);
	
}
