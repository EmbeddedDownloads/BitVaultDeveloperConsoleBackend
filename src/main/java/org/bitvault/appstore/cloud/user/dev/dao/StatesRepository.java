package org.bitvault.appstore.cloud.user.dev.dao;

import org.bitvault.appstore.cloud.model.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepository extends JpaRepository<States, Integer>{

}
