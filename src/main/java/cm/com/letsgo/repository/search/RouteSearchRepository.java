package cm.com.letsgo.repository.search;

import cm.com.letsgo.domain.Route;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Route entity.
 */
public interface RouteSearchRepository extends ElasticsearchRepository<Route, Long> {
}
