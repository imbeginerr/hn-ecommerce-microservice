package vn.hn.hncoreservice.dao.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.data.criteria.UserSearchCriteria;

import java.util.Optional;

@Service
public interface UserService {
	User save(User user);
	
	Optional<User> findByIdAndDeletedFalse(Long id);
	
	Page<User> findAll(UserSearchCriteria criteria, Pageable pageable);
	
	boolean existsAllByUsernameAndDeletedFalse(String username);
	
	Optional<User> findById(Long id);
	
	Optional<User> findByUsernameAndDeletedFalse(String username);
	
	Optional<User> findByUsername(String username);
	
}
