package org.bitvault.appstore.commons.application.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.AppTester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface AppTesterRepository extends JpaRepository<AppTester,Integer>{


@Query("Select appTester From AppTester appTester where appTester.publicAddress=?1")
List<AppTester>FindByPublicAddress(String publicAddress);

@Modifying
    @Query("delete  From AppTester appTester where appTester.application.appApplicationId=?1")
    void deleteAppTesterByApplicationId(Integer applicationId);



}
