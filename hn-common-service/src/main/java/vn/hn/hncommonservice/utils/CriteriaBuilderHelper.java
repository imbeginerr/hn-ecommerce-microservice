package vn.hn.hncommonservice.utils;

import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CriteriaBuilderHelper {
	
	private CriteriaBuilderHelper() {
		// Utility class
	}
	
	/**
	 * LIKE search with unaccent support Tìm "nguyen" sẽ match "Nguyễn", "Nguyên", "Nguyên"
	 */
	public static <T> Predicate createUnaccentedLike(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, String> attribute,
			String value
	                                                ) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		// PostgreSQL: Sử dụng unaccent extension
		Expression<String> unaccentedField = cb.function(
				"unaccent",
				String.class,
				cb.lower(root.get(attribute))
		                                                );
		
		String unaccentedValue = CoreUtils.removeAccent(value);
		String likePattern = "%" + unaccentedValue + "%";
		
		return cb.like(unaccentedField, likePattern);
	}
	
	/**
	 * OR LIKE search với nhiều fields (unaccented) Tìm "nguyen van" trong họ, tên, email
	 */
	@SafeVarargs
	public static <T> Predicate createOrUnaccentedLike(
			CriteriaBuilder cb,
			Root<T> root,
			String keyword,
			SingularAttribute<? super T, String>... attributes
	                                                  ) {
		if (keyword == null || keyword.trim().isEmpty() || attributes.length == 0) {
			return null;
		}
		
		List<Predicate> predicates = new ArrayList<>();
		String unaccentedKeyword = CoreUtils.removeAccent(keyword);
		String likePattern = "%" + unaccentedKeyword + "%";
		
		for (SingularAttribute<? super T, String> attribute : attributes) {
			Expression<String> unaccentedField = cb.function(
					"unaccent",
					String.class,
					cb.lower(root.get(attribute))
			                                                );
			predicates.add(cb.like(unaccentedField, likePattern));
		}
		
		return cb.or(predicates.toArray(new Predicate[0]));
	}
	
	/**
	 * Exact match with unaccent "nguyen" match "Nguyễn"
	 */
	public static <T> Predicate createUnaccentedEquals(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, String> attribute,
			String value
	                                                  ) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		Expression<String> unaccentedField = cb.function(
				"unaccent",
				String.class,
				cb.lower(root.get(attribute))
		                                                );
		
		String unaccentedValue = CoreUtils.removeAccent(value);
		
		return cb.equal(unaccentedField, unaccentedValue);
	}
	
	// ... existing methods (createEquals, createBoolean, createIn, etc.)
	
	public static <T, Y> Predicate createEquals(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, Y> attribute,
			Y value
	                                           ) {
		if (value == null) {
			return null;
		}
		return cb.equal(root.get(attribute), value);
	}
	
	public static <T> Predicate createBoolean(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, Boolean> attribute,
			Boolean value
	                                         ) {
		if (value == null) {
			return null;
		}
		return cb.equal(root.get(attribute), value);
	}
	
	@SafeVarargs
	public static <T> Predicate createOrLike(
			CriteriaBuilder cb,
			Root<T> root,
			String keyword,
			SingularAttribute<? super T, String>... attributes
	                                        ) {
		if (keyword == null || keyword.trim().isEmpty() || attributes.length == 0) {
			return null;
		}
		
		String likePattern = "%" + keyword.toLowerCase().trim() + "%";
		List<Predicate> predicates = new ArrayList<>();
		
		for (SingularAttribute<? super T, String> attribute : attributes) {
			predicates.add(cb.like(cb.lower(root.get(attribute)), likePattern));
		}
		
		return cb.or(predicates.toArray(new Predicate[0]));
	}
	
	public static <T, Y> Predicate createIn(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, Y> attribute,
			List<Y> values
	                                       ) {
		if (values == null || values.isEmpty()) {
			return null;
		}
		return root.get(attribute).in(values);
	}
	
	public static <T, Y extends Comparable<? super Y>> Predicate createGreaterThanOrEqualTo(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, Y> attribute,
			Y value
	                                                                                       ) {
		if (value == null) {
			return null;
		}
		return cb.greaterThanOrEqualTo(root.get(attribute), value);
	}
	
	public static <T, Y extends Comparable<? super Y>> Predicate createLessThanOrEqualTo(
			CriteriaBuilder cb,
			Root<T> root,
			SingularAttribute<? super T, Y> attribute,
			Y value
	                                                                                    ) {
		if (value == null) {
			return null;
		}
		return cb.lessThanOrEqualTo(root.get(attribute), value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T, Y> Join<T, Y> getOrCreateJoin(
			Root<T> root,
			SingularAttribute<? super T, ?> attribute,
			JoinType joinType
	                                               ) {
		for (Join<T, ?> join : root.getJoins()) {
			if (join.getAttribute().getName().equals(attribute.getName())) {
				return (Join<T, Y>) join;
			}
		}
		return (Join<T, Y>) root.join(attribute.getName(), joinType);
	}
	
	public static Predicate and(CriteriaBuilder cb, Predicate... predicates) {
		List<Predicate> nonNullPredicates = Arrays.stream(predicates)
				.filter(Objects::nonNull)
				.toList();
		
		if (nonNullPredicates.isEmpty()) {
			return cb.conjunction();
		}
		
		return cb.and(nonNullPredicates.toArray(new Predicate[0]));
	}
	
	public static Predicate or(CriteriaBuilder cb, Predicate... predicates) {
		List<Predicate> nonNullPredicates = Arrays.stream(predicates)
				.filter(Objects::nonNull)
				.toList();
		
		if (nonNullPredicates.isEmpty()) {
			return cb.disjunction();
		}
		
		return cb.or(nonNullPredicates.toArray(new Predicate[0]));
	}
}
