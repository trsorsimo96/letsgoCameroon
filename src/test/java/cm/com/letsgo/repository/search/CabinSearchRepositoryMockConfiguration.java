package cm.com.letsgo.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CabinSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CabinSearchRepositoryMockConfiguration {

    @MockBean
    private CabinSearchRepository mockCabinSearchRepository;

}
