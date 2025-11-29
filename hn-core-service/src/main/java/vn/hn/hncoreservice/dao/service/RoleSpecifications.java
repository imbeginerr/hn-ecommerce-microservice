package vn.hn.hncoreservice.dao.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.hn.hncommonservice.utils.CriteriaBuilderHelper;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.dao.model.Role_;
import vn.hn.hncoreservice.data.criteria.RoleSearchCriteria;

public class RoleSpecifications {
	
	private RoleSpecifications() {
	}
	
	public static Specification<Role> quickSearch(final RoleSearchCriteria criteria) {
		return (root, query, cb) -> {
			Predicate defaultCondition = CriteriaBuilderHelper.createBoolean(cb, root, Role_.deleted, false);
			Predicate textSearchCondition = CriteriaBuilderHelper.createOrLike(cb, root, criteria.getName(), Role_.name);
			return CriteriaBuilderHelper.and(cb, defaultCondition, textSearchCondition);
		};
	}
}
