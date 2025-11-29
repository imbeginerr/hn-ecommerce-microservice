package vn.hn.hncoreservice.mapper;

import org.mapstruct.Mapper;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.data.request.PermissionRequest;
import vn.hn.hncoreservice.data.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	Permission toPermission(PermissionRequest request);
	
	PermissionResponse toPermissionResponse(Permission permission);
}
