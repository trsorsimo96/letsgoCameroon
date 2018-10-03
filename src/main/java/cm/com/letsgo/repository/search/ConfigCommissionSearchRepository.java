package cm.com.letsgo.repository.search;

import cm.com.letsgo.domain.ConfigCommission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigCommission entity.
 */
public interface ConfigCommissionSearchRepository extends ElasticsearchRepository<ConfigCommission, Long> {
}
