package org.bitvault.appstore.commons.application.elasticdao;

import org.bitvault.appstore.cloud.model.DevUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DevUserElasticRepository extends ElasticsearchRepository<DevUser, String> {
	@Query("{\"query\":{\"filtered\":{\"filter\":{\"bool\":{\"should\":[{\"match_phrase_prefix\":{\"username\":\"?0\"}},{\"match_phrase_prefix\":{\"email\":\"?0\"}}],\"must_not\":{\"terms\":{\"status\":[\"review\",\"rejected\"]}}}}}}}")
	Page<DevUser> findDevUserByUsername(String username, Pageable page);

}
