package vn.hn.hncoreservice.mapper;

import org.mapstruct.*;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.data.request.UserCreateRequest;
import vn.hn.hncoreservice.data.request.UserUpdateRequest;
import vn.hn.hncoreservice.data.response.UserCreateResponse;
import vn.hn.hncoreservice.data.response.UserResponse;
import vn.hn.hncoreservice.data.response.UserUpdateResponse;

@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	User toEntity(UserCreateRequest request);
	
	UserCreateResponse toCreateResponse(User user);
	
	UserUpdateResponse toUpdateResponse(User user);
	
	UserResponse toResponse(User user);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntity(UserUpdateRequest request, @MappingTarget User user);
	
}
