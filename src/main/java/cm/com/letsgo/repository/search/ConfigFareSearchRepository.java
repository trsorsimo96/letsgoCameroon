package cm.com.letsgo.repository.search;

import cm.com.letsgo.domain.ConfigFare;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigFare entity.
 */
public interface ConfigFareSearchRepository extends ElasticsearchRepository<ConfigFare, Long> {
}
