package vn.hn.hncoreservice.dao.service.Iml;

import org.springframework.stereotype.Service;
import vn.hn.hncommonservice.service.IsTokenInvalidated;
import vn.hn.hncoreservice.dao.model.InvalidatedToken;
import vn.hn.hncoreservice.dao.service.InvalidatedTokenRepo;
import vn.hn.hncoreservice.dao.service.InvalidatedTokenService;

@Service
public class InvalidatedTokenServiceIml implements InvalidatedTokenService, IsTokenInvalidated {
	private final InvalidatedTokenRepo repo;
	
	public InvalidatedTokenServiceIml(InvalidatedTokenRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public InvalidatedToken save(InvalidatedToken invalidtedToken) {
		return repo.save(invalidtedToken);
	}
	
	@Override
	public boolean isTokenInvalidated(String jti) {
		return repo.existsById(jti);
	}
	
}
