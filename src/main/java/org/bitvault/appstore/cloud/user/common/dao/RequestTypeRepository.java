package org.bitvault.appstore.cloud.user.common.dao;

import org.bitvault.appstore.cloud.model.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, Integer> {

	@Query("select reqType from RequestType reqType where reqType.requestType = ?1")
	public RequestType findRequestTypeByType(String type);
}