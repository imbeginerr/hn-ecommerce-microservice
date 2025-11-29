package vn.hn.hncoreservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.data.request.RoleRequest;
import vn.hn.hncoreservice.data.response.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	@Mapping(target = "permissions", ignore = true)
	Role toRole(RoleRequest request);
	
	RoleResponse toRoleResponse(Role role);
}
