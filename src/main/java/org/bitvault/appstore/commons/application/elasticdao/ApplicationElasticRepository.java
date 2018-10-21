package org.bitvault.appstore.commons.application.elasticdao;

import org.bitvault.appstore.cloud.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApplicationElasticRepository extends ElasticsearchRepository<Application,Integer>
{
	@Query("{\"query\":{\"bool\":{\"should\":[{\"match_phrase_prefix\":{\"app_name\":\"?0\"}},{\"match_phrase_prefix\":{\"title\":\"?0\"}},{\"match_phrase_prefix\":{\"package_name\":\"?0\"}},{\"match_phrase_prefix\":{\"company\":\"?0\"}}]}}}")
	Page<Application> findApplicationByAppName(String app_name, Pageable pageable);

	//{\"query\": {\"bool\" : {\"should\" : [{ \"match_phrase_prefix\" : { \"app_name\" : \"Calculator\" } },{ \"match_phrase_prefix\" : { \"app_name\" : \"Musicname\" } }]}}}
}
