/**
 * 
 */
package org.unlimits.rest.repository;

import java.lang.reflect.Type;

import org.unlimits.rest.filters.FilterPredicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *  @author omnie
 */
@FunctionalInterface
public interface CustomPredicate<EN> {

	/**
	 * @param type
	 * @param root
	 * @param query
	 * @param criteriaBuilder
	 * @param filter
	 * @return
	 */
	Predicate build(Type type, Root<EN> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, FilterPredicate filter);

}
