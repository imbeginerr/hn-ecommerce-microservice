package vn.hn.hncoreservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.data.request.PermissionRequest;
import vn.hn.hncoreservice.data.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	@Mapping(target = "decription", source = "description")
	Permission toPermission(PermissionRequest request);
	
	@Mapping(target = "description", source = "decription")
	PermissionResponse toPermissionResponse(Permission permission);
}
