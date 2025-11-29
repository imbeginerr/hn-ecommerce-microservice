package vn.hn.hncoreservice.dao.service.Iml;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.dao.service.RoleRepo;
import vn.hn.hncoreservice.dao.service.RoleService;
import vn.hn.hncoreservice.dao.service.RoleSpecifications;
import vn.hn.hncoreservice.data.criteria.RoleSearchCriteria;

import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceIml implements RoleService {
	private final RoleRepo repo;
	
	public RoleServiceIml(RoleRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public Role save(Role role) {
		return repo.save(role);
	}
	
	@Override
	public Optional<Role> findByNameAndDeletedFalse(String name) {
		return repo.findByNameAndDeletedFalse(name);
	}
	
	@Override
	public Page<Role> findAll(RoleSearchCriteria criteria, Pageable pageable) {
		return repo.findAll(RoleSpecifications.quickSearch(criteria), pageable);
	}
	
	@Override
	public Optional<Role> findByName(String name) {
		return repo.findByName(name);
	}
	
	@Override
	public Set<Role> findAllByNameIn(Set<String> names) {
		return repo.findAllByNameIn(names);
	}
	
}
