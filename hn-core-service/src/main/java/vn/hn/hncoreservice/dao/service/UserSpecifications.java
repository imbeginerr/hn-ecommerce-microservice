package vn.hn.hncoreservice.dao.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.hn.hncommonservice.utils.CriteriaBuilderHelper;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.dao.model.User_;
import vn.hn.hncoreservice.data.criteria.UserSearchCriteria;

public class UserSpecifications {
	
	private UserSpecifications() {
	}
	
	public static Specification<User> quickSearch(final UserSearchCriteria criteria) {
		return (root, query, cb) -> {
			Predicate defaultCondition = CriteriaBuilderHelper.createBoolean(cb, root, User_.deleted, false);
			Predicate textSearchCondition = CriteriaBuilderHelper.createOrUnaccentedLike(cb, root, criteria.getUsername(), User_.username);
			return CriteriaBuilderHelper.and(cb, defaultCondition, textSearchCondition);
		};
	}
}
