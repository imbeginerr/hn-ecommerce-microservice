package vn.hn.hncoreservice.dao.service.Iml;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.dao.service.PermissionRepo;
import vn.hn.hncoreservice.dao.service.PermissionService;
import vn.hn.hncoreservice.dao.service.PermissionSpecifications;
import vn.hn.hncoreservice.data.criteria.PermissionSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PermissionServiceIml implements PermissionService {
	private final PermissionRepo repo;
	
	public PermissionServiceIml(PermissionRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public Permission save(Permission permission) {
		return repo.save(permission);
	}
	
	@Override
	public Optional<Permission> findByNameAndDeletedFalse(String name) {
		return repo.findByNameAndDeletedFalse(name);
	}
	
	@Override
	public Page<Permission> findAll(PermissionSearchCriteria criteria, Pageable pageable) {
		return repo.findAll(PermissionSpecifications.quickSearch(criteria), pageable);
	}
	
	@Override
	public Optional<Permission> findByName(String name) {
		return repo.findByName(name);
	}
	
	@Override
	public List<Permission> findAll() {
		return repo.findAll();
	}
	
	@Override
	public Set<Permission> findAllByNameIn(Set<String> names) {
		return repo.findAllByNameIn(names);
	}
	
}
