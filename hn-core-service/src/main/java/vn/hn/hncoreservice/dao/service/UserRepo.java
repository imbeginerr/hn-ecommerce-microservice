package vn.hn.hncoreservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.hn.hncoreservice.dao.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Optional<User> findByIdAndDeletedFalse(Long id);
	
	boolean existsAllByUsernameAndDeletedFalse(String username);
	
	List<User> findByDeletedFalse();
	
	Optional<User> findByUsernameAndDeletedFalse(String username);
	
	Optional<User> findByUsername(String username);
	
}
