package cm.com.letsgo.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PartnerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PartnerSearchRepositoryMockConfiguration {

    @MockBean
    private PartnerSearchRepository mockPartnerSearchRepository;

}
