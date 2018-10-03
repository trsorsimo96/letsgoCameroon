package cm.com.letsgo.repository.search;

import cm.com.letsgo.domain.Resa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Resa entity.
 */
public interface ResaSearchRepository extends ElasticsearchRepository<Resa, Long> {
}
