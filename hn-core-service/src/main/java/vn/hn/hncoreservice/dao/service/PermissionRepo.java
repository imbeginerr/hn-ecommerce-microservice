package vn.hn.hncoreservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.hn.hncoreservice.dao.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {
	Optional<Permission> findByNameAndDeletedFalse(String name);
	
	Optional<Permission> findByName(String name);
	
	Set<Permission> findAllByNameIn(Set<String> names);
	
	List<Permission> findAll();
}
