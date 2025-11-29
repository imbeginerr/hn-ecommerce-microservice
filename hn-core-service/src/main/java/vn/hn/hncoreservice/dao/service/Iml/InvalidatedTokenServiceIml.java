package vn.hn.hncoreservice.dao.service.Iml;

import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.InvalidatedToken;
import vn.hn.hncoreservice.dao.service.InvalidatedTokenRepo;
import vn.hn.hncoreservice.dao.service.InvalidatedTokenService;

@Service
public class InvalidatedTokenServiceIml implements InvalidatedTokenService {
	private final InvalidatedTokenRepo repo;
	
	public InvalidatedTokenServiceIml(InvalidatedTokenRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public InvalidatedToken save(InvalidatedToken invalidtedToken) {
		return repo.save(invalidtedToken);
	}
	
	@Override
	public boolean existsById(String id) {
		return repo.existsById(id);
	}
	
}
