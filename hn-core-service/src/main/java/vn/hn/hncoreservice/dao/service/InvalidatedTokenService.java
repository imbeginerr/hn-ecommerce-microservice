package vn.hn.hncoreservice.dao.service;

import org.springframework.stereotype.Service;
import vn.hn.hncoreservice.dao.model.InvalidatedToken;

@Service
public interface InvalidatedTokenService {
	InvalidatedToken save(InvalidatedToken invalidatedToken);
	
	boolean isTokenInvalidated(String id);
	
}
