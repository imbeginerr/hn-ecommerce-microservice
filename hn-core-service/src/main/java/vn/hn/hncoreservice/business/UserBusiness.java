package vn.hn.hncoreservice.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hn.hncommonservice.exception.AppException;
import vn.hn.hncommonservice.exception.ErrorCode;
import vn.hn.hncommonservice.utils.CoreUtils;
import vn.hn.hncoreservice.constant.PredefinedRole;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.dao.service.RoleService;
import vn.hn.hncoreservice.dao.service.UserService;
import vn.hn.hncoreservice.data.criteria.UserSearchCriteria;
import vn.hn.hncoreservice.data.request.UserCreateRequest;
import vn.hn.hncoreservice.data.request.UserUpdateRequest;
import vn.hn.hncoreservice.data.response.UserCreateResponse;
import vn.hn.hncoreservice.data.response.UserResponse;
import vn.hn.hncoreservice.data.response.UserUpdateResponse;
import vn.hn.hncoreservice.mapper.UserMapper;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserBusiness {
	
	private final UserService userService;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final RoleService roleService;
	
	@Transactional
	public UserCreateResponse create(UserCreateRequest request) {
		
		if (userService.existsAllByUsernameAndDeletedFalse(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}
		User user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		HashSet<Role> roles = new HashSet<>();
		roleService.findByName(PredefinedRole.USER_ROLE).ifPresent(roles::add);
		user.setRoles(roles);
		User savedUser = userService.save(user);
		
		log.info("User created with id: {}", savedUser.getId());
		
		return userMapper.toCreateResponse(savedUser);
	}
	
	@Transactional
	public UserUpdateResponse update(Long id, UserUpdateRequest request) {
		log.info("Updating user id: {}", id);
		
		User user = userService.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException(STR."Không tìm thấy user với id: \{id}"));
		
		userMapper.updateEntity(request, user);
		if (request.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		if (request.getRoles() != null) {
			var roles = roleService.findAllByNameIn(request.getRoles());
			user.setRoles(roles);
		}
		User updatedUser = userService.save(user);
		
		log.info("User updated successfully");
		
		return userMapper.toUpdateResponse(updatedUser);
	}
	
	@Transactional(readOnly = true)
	@PostAuthorize("returnObject.username == authentication.name")
	public UserResponse findById(Long id) {
		log.info("Fetching user id: {}", id);
		
		User user = userService.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
		
		return userMapper.toResponse(user);
	}
	
	@Transactional(readOnly = true)
	public Page<UserResponse> findAll(UserSearchCriteria criteria) {
		Page<User> pageUser = userService.findAll(criteria,
				CoreUtils.getPageRequest(criteria.getPage(), criteria.getSize(), criteria.getSortBy(), criteria.getSortDir()));
		return pageUser.map(userMapper::toResponse);
	}
	
	@Transactional
	public void delete(Long id) {
		log.info("Deleting user id: {}", id);
		
		User user = userService.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
		
		user.softDelete();
		userService.save(user);
		
		log.info("User deleted successfully");
	}
	
	@Transactional
	public void restore(Long id) {
		log.info("Restoring user id: {}", id);
		
		User user = userService.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
		
		user.restore();
		userService.save(user);
		
		log.info("User restored successfully");
	}
	
	@Transactional
	@PostAuthorize("returnObject.username == authentication.name")
	public UserResponse getMyInfo() {
		var context = SecurityContextHolder.getContext();
		String name = context.getAuthentication().getName();
		
		User user = userService.findByUsernameAndDeletedFalse(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		return userMapper.toResponse(user);
	}
}
