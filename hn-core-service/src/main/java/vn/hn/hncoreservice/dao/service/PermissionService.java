package vn.hn.hncoreservice.dao.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.data.criteria.PermissionSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface PermissionService {
	Permission save(Permission permission);
	
	Optional<Permission> findByNameAndDeletedFalse(String name);
	
	Page<Permission> findAll(PermissionSearchCriteria criteria, Pageable pageable);
	
	Optional<Permission> findByName(String name);
	
	List<Permission> findAll();
	
	Set<Permission> findAllByNameIn(Set<String> names);
	
}
