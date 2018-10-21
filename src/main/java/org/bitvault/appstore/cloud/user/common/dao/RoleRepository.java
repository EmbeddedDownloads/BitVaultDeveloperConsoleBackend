package org.bitvault.appstore.cloud.user.common.dao;

import org.bitvault.appstore.cloud.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
