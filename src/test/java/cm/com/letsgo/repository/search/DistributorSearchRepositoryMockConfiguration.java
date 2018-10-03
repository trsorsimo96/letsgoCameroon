package cm.com.letsgo.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DistributorSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DistributorSearchRepositoryMockConfiguration {

    @MockBean
    private DistributorSearchRepository mockDistributorSearchRepository;

}
