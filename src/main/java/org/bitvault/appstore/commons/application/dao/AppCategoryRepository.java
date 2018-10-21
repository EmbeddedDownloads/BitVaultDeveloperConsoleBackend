package org.bitvault.appstore.commons.application.dao;

import java.util.List;

import org.bitvault.appstore.cloud.model.AppCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppCategoryRepository extends JpaRepository<AppCategory, Integer> {

	@Query("select category from AppCategory category where category.appCategoryId = ?1")
	AppCategory findCategoryById(Integer catId);

	@Query("select category from AppCategory category where category.applications IS NOT EMPTY")
	Page<AppCategory> findCategoryWithApplication(Pageable pageable);

	@Query("select category from AppCategory category where category.categoryType = ?1")
	AppCategory findCategoryByName(String categoryName);

	@Modifying
	@Query("Update AppCategory appCategory set appCategory.status = ?1 where appCategory.appCategoryId = ?2")
	Integer updateAppCategoryByCategoryId(String status, Integer categoryId);

	@Query("select category from AppCategory category where category.status in ?1")
	Page<AppCategory> findAppCategoryByStatus(List<String> status, Pageable pageable);

	@Query("select category from AppCategory category where category.applicationType.appTypeId = ?1 and category.status in ?2")
	List<AppCategory> findAppCategoryByAppTypeId(Integer appTypeId,  List<String> status);

}
