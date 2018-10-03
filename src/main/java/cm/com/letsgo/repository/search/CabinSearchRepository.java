package cm.com.letsgo.repository.search;

import cm.com.letsgo.domain.Cabin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cabin entity.
 */
public interface CabinSearchRepository extends ElasticsearchRepository<Cabin, Long> {
}
