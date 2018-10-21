package org.bitvault.appstore.cloud.user.dev.dao;

import org.bitvault.appstore.cloud.model.UserActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityTypeRepository extends JpaRepository<UserActivityType, Integer> {

}
