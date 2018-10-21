package org.bitvault.appstore.commons.application.elasticdao;

import org.bitvault.appstore.cloud.model.AppApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AppApplicationElasticRepository extends ElasticsearchRepository<AppApplication,Integer>
{
	
@Query("{\"query\":{\"filtered\":{\"filter\":{\"bool\":{\"should\":[{\"match_phrase_prefix\":{\"app_name\":\"?0\"}},{\"match_phrase_prefix\":{\"title\":\"?0\"}},{\"match_phrase_prefix\":{\"package_name\":\"?0\"}},{\"match_phrase_prefix\":{\"company\":\"?0\"}}],\"must\":{\"term\":{\"user_id\":\"?1\"}}}}}}}}")
Page<AppApplication> findAppApplicationByAppName(String app_name, String user_id,Pageable page);

@Query("{\"query\":{\"filtered\":{\"filter\":{\"bool\":{\"should\":[{\"match_phrase_prefix\":{\"app_name\":\"?0\"}},{\"match_phrase_prefix\":{\"title\":\"?0\"}},{\"match_phrase_prefix\":{\"package_name\":\"?0\"}},{\"match_phrase_prefix\":{\"company\":\"?0\"}}],\"must_not\":{\"term\":{\"status\":\"draft\"}}}}}}}")
Page<AppApplication> searchAppApplicationByAppName(String app_name,Pageable page);
}