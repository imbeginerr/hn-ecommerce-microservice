package vn.hn.hncoreservice.dao.service.Iml;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.dao.service.UserRepo;
import vn.hn.hncoreservice.dao.service.UserService;
import vn.hn.hncoreservice.dao.service.UserSpecifications;
import vn.hn.hncoreservice.data.criteria.UserSearchCriteria;

import java.util.Optional;

@Service
public class UserServiceIml implements UserService {
	private final UserRepo repo;
	
	public UserServiceIml(UserRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public User save(User user) {
		return repo.save(user);
	}
	
	@Override
	public Optional<User> findByIdAndDeletedFalse(Long id) {
		return repo.findByIdAndDeletedFalse(id);
	}
	
	@Override
	public Page<User> findAll(UserSearchCriteria criteria, Pageable pageable) {
		return repo.findAll(UserSpecifications.quickSearch(criteria), pageable);
	}
	
	@Override
	public boolean existsAllByUsernameAndDeletedFalse(String username) {
		return repo.existsAllByUsernameAndDeletedFalse(username);
	}
	
	@Override
	public Optional<User> findById(Long id) {
		return Optional.empty();
	}
	
	@Override
	public Optional<User> findByUsernameAndDeletedFalse(String username) {
		return repo.findByUsernameAndDeletedFalse(username);
	}
	
	@Override
	public Optional<User> findByUsername(String username) {
		return repo.findByUsername(username);
	}
}
