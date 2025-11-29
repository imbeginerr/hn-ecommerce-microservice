package vn.hn.hncoreservice.dao.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.hn.hncommonservice.utils.CriteriaBuilderHelper;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.dao.model.Permission_;
import vn.hn.hncoreservice.data.criteria.PermissionSearchCriteria;

public class PermissionSpecifications {
	
	private PermissionSpecifications() {
	}
	
	public static Specification<Permission> quickSearch(final PermissionSearchCriteria criteria) {
		return (root, query, cb) -> {
			Predicate defaultCondition = CriteriaBuilderHelper.createBoolean(cb, root, Permission_.deleted, false);
			Predicate textSearchCondition = CriteriaBuilderHelper.createOrUnaccentedLike(cb, root, criteria.getName(), Permission_.name);
			return CriteriaBuilderHelper.and(cb, defaultCondition, textSearchCondition);
		};
	}
}
