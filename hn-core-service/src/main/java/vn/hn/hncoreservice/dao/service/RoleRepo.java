package vn.hn.hncoreservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.hn.hncoreservice.dao.model.Role;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
	Optional<Role> findByNameAndDeletedFalse(String name);
	
	Optional<Role> findByName(String name);
	
	Set<Role> findAllByNameIn(Set<String> names);
}