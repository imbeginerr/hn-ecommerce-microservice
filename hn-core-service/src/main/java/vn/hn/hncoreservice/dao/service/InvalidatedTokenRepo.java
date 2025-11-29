package vn.hn.hncoreservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.hn.hncoreservice.dao.model.InvalidatedToken;

@Repository
public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String>, JpaSpecificationExecutor<InvalidatedToken> {

}
