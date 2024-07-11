package org.unlimits.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomRepository<EN, ID> extends JpaRepository<EN, ID>, JpaSpecificationExecutor<EN> {
	
}
