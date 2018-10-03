package cm.com.letsgo.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ResaSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ResaSearchRepositoryMockConfiguration {

    @MockBean
    private ResaSearchRepository mockResaSearchRepository;

}
