package org.bitvault.appstore.cloud.user.dev.dao;

import org.bitvault.appstore.cloud.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{

}
