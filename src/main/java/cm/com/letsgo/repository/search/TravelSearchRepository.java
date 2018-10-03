package cm.com.letsgo.repository.search;

import cm.com.letsgo.domain.Travel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Travel entity.
 */
public interface TravelSearchRepository extends ElasticsearchRepository<Travel, Long> {
}
