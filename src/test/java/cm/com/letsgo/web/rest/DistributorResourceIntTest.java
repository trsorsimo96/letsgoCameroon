package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.Distributor;
import cm.com.letsgo.repository.DistributorRepository;
import cm.com.letsgo.repository.search.DistributorSearchRepository;
import cm.com.letsgo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static cm.com.letsgo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DistributorResource REST controller.
 *
 * @see DistributorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class DistributorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    @Autowired
    private DistributorRepository distributorRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.DistributorSearchRepositoryMockConfiguration
     */
    @Autowired
    private DistributorSearchRepository mockDistributorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDistributorMockMvc;

    private Distributor distributor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DistributorResource distributorResource = new DistributorResource(distributorRepository, mockDistributorSearchRepository);
        this.restDistributorMockMvc = MockMvcBuilders.standaloneSetup(distributorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distributor createEntity(EntityManager em) {
        Distributor distributor = new Distributor()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .number(DEFAULT_NUMBER)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return distributor;
    }

    @Before
    public void initTest() {
        distributor = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistributor() throws Exception {
        int databaseSizeBeforeCreate = distributorRepository.findAll().size();

        // Create the Distributor
        restDistributorMockMvc.perform(post("/api/distributors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributor)))
            .andExpect(status().isCreated());

        // Validate the Distributor in the database
        List<Distributor> distributorList = distributorRepository.findAll();
        assertThat(distributorList).hasSize(databaseSizeBeforeCreate + 1);
        Distributor testDistributor = distributorList.get(distributorList.size() - 1);
        assertThat(testDistributor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDistributor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDistributor.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testDistributor.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDistributor.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);

        // Validate the Distributor in Elasticsearch
        verify(mockDistributorSearchRepository, times(1)).save(testDistributor);
    }

    @Test
    @Transactional
    public void createDistributorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = distributorRepository.findAll().size();

        // Create the Distributor with an existing ID
        distributor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistributorMockMvc.perform(post("/api/distributors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributor)))
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        List<Distributor> distributorList = distributorRepository.findAll();
        assertThat(distributorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Distributor in Elasticsearch
        verify(mockDistributorSearchRepository, times(0)).save(distributor);
    }

    @Test
    @Transactional
    public void getAllDistributors() throws Exception {
        // Initialize the database
        distributorRepository.saveAndFlush(distributor);

        // Get all the distributorList
        restDistributorMockMvc.perform(get("/api/distributors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distributor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }
    
    @Test
    @Transactional
    public void getDistributor() throws Exception {
        // Initialize the database
        distributorRepository.saveAndFlush(distributor);

        // Get the distributor
        restDistributorMockMvc.perform(get("/api/distributors/{id}", distributor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(distributor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    public void getNonExistingDistributor() throws Exception {
        // Get the distributor
        restDistributorMockMvc.perform(get("/api/distributors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistributor() throws Exception {
        // Initialize the database
        distributorRepository.saveAndFlush(distributor);

        int databaseSizeBeforeUpdate = distributorRepository.findAll().size();

        // Update the distributor
        Distributor updatedDistributor = distributorRepository.findById(distributor.getId()).get();
        // Disconnect from session so that the updates on updatedDistributor are not directly saved in db
        em.detach(updatedDistributor);
        updatedDistributor
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .number(UPDATED_NUMBER)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restDistributorMockMvc.perform(put("/api/distributors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDistributor)))
            .andExpect(status().isOk());

        // Validate the Distributor in the database
        List<Distributor> distributorList = distributorRepository.findAll();
        assertThat(distributorList).hasSize(databaseSizeBeforeUpdate);
        Distributor testDistributor = distributorList.get(distributorList.size() - 1);
        assertThat(testDistributor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDistributor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDistributor.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testDistributor.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testDistributor.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);

        // Validate the Distributor in Elasticsearch
        verify(mockDistributorSearchRepository, times(1)).save(testDistributor);
    }

    @Test
    @Transactional
    public void updateNonExistingDistributor() throws Exception {
        int databaseSizeBeforeUpdate = distributorRepository.findAll().size();

        // Create the Distributor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistributorMockMvc.perform(put("/api/distributors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(distributor)))
            .andExpect(status().isBadRequest());

        // Validate the Distributor in the database
        List<Distributor> distributorList = distributorRepository.findAll();
        assertThat(distributorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Distributor in Elasticsearch
        verify(mockDistributorSearchRepository, times(0)).save(distributor);
    }

    @Test
    @Transactional
    public void deleteDistributor() throws Exception {
        // Initialize the database
        distributorRepository.saveAndFlush(distributor);

        int databaseSizeBeforeDelete = distributorRepository.findAll().size();

        // Get the distributor
        restDistributorMockMvc.perform(delete("/api/distributors/{id}", distributor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Distributor> distributorList = distributorRepository.findAll();
        assertThat(distributorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Distributor in Elasticsearch
        verify(mockDistributorSearchRepository, times(1)).deleteById(distributor.getId());
    }

    @Test
    @Transactional
    public void searchDistributor() throws Exception {
        // Initialize the database
        distributorRepository.saveAndFlush(distributor);
        when(mockDistributorSearchRepository.search(queryStringQuery("id:" + distributor.getId())))
            .thenReturn(Collections.singletonList(distributor));
        // Search the distributor
        restDistributorMockMvc.perform(get("/api/_search/distributors?query=id:" + distributor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distributor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distributor.class);
        Distributor distributor1 = new Distributor();
        distributor1.setId(1L);
        Distributor distributor2 = new Distributor();
        distributor2.setId(distributor1.getId());
        assertThat(distributor1).isEqualTo(distributor2);
        distributor2.setId(2L);
        assertThat(distributor1).isNotEqualTo(distributor2);
        distributor1.setId(null);
        assertThat(distributor1).isNotEqualTo(distributor2);
    }
}
