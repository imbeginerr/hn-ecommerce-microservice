package vn.hn.hncoreservice.dao.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.data.criteria.RoleSearchCriteria;

import java.util.Optional;
import java.util.Set;

@Service
public interface RoleService {
	Role save(Role role);
	
	Optional<Role> findByNameAndDeletedFalse(String name);
	
	Page<Role> findAll(RoleSearchCriteria criteria, Pageable pageable);
	
	Optional<Role> findByName(String name);
	
	Set<Role> findAllByNameIn(Set<String> names);
	
}
