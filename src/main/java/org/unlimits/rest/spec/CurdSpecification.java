package org.unlimits.rest.spec;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.unlimits.rest.crud.service.QueryService;
import org.unlimits.rest.filters.FilterPredicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CurdSpecification<DT, EN, ID> implements Specification<EN>{
	
	private static final long serialVersionUID = 1L;
	
	private List<FilterPredicate> filterList;
	
	private Type type;
	
	private QueryService<DT, EN, ID> queryService;
	
	/**
	 * 
	 */
	public CurdSpecification(QueryService<DT, EN, ID> crudService,Type type, List<FilterPredicate> filterList) {
		this.queryService=crudService;
		this.type=type;
		this.filterList=filterList;
	}

	@Override
	public Predicate toPredicate(Root<EN> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		filterList.forEach(filter -> {
			Predicate predicate = queryService.build(type(), root, query, criteriaBuilder, filter);
			if(predicate!=null) {
				predicates.add(predicate);
			}
		});
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	protected Type type() {
		return type;
	}
}