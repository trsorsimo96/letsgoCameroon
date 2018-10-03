package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.ConfigFare;
import cm.com.letsgo.repository.ConfigFareRepository;
import cm.com.letsgo.repository.search.ConfigFareSearchRepository;
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
 * Test class for the ConfigFareResource REST controller.
 *
 * @see ConfigFareResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class ConfigFareResourceIntTest {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final Integer DEFAULT_FARE = 1;
    private static final Integer UPDATED_FARE = 2;

    private static final Boolean DEFAULT_CANCELABLE = false;
    private static final Boolean UPDATED_CANCELABLE = true;

    private static final Integer DEFAULT_PENALTY_CANCEL = 1;
    private static final Integer UPDATED_PENALTY_CANCEL = 2;

    private static final Boolean DEFAULT_NOSHOW = false;
    private static final Boolean UPDATED_NOSHOW = true;

    private static final Integer DEFAULT_PENALTY_NO_SHOW = 1;
    private static final Integer UPDATED_PENALTY_NO_SHOW = 2;

    @Autowired
    private ConfigFareRepository configFareRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.ConfigFareSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigFareSearchRepository mockConfigFareSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfigFareMockMvc;

    private ConfigFare configFare;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigFareResource configFareResource = new ConfigFareResource(configFareRepository, mockConfigFareSearchRepository);
        this.restConfigFareMockMvc = MockMvcBuilders.standaloneSetup(configFareResource)
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
    public static ConfigFare createEntity(EntityManager em) {
        ConfigFare configFare = new ConfigFare()
            .numero(DEFAULT_NUMERO)
            .fare(DEFAULT_FARE)
            .cancelable(DEFAULT_CANCELABLE)
            .penaltyCancel(DEFAULT_PENALTY_CANCEL)
            .noshow(DEFAULT_NOSHOW)
            .penaltyNoShow(DEFAULT_PENALTY_NO_SHOW);
        return configFare;
    }

    @Before
    public void initTest() {
        configFare = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigFare() throws Exception {
        int databaseSizeBeforeCreate = configFareRepository.findAll().size();

        // Create the ConfigFare
        restConfigFareMockMvc.perform(post("/api/config-fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configFare)))
            .andExpect(status().isCreated());

        // Validate the ConfigFare in the database
        List<ConfigFare> configFareList = configFareRepository.findAll();
        assertThat(configFareList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigFare testConfigFare = configFareList.get(configFareList.size() - 1);
        assertThat(testConfigFare.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testConfigFare.getFare()).isEqualTo(DEFAULT_FARE);
        assertThat(testConfigFare.isCancelable()).isEqualTo(DEFAULT_CANCELABLE);
        assertThat(testConfigFare.getPenaltyCancel()).isEqualTo(DEFAULT_PENALTY_CANCEL);
        assertThat(testConfigFare.isNoshow()).isEqualTo(DEFAULT_NOSHOW);
        assertThat(testConfigFare.getPenaltyNoShow()).isEqualTo(DEFAULT_PENALTY_NO_SHOW);

        // Validate the ConfigFare in Elasticsearch
        verify(mockConfigFareSearchRepository, times(1)).save(testConfigFare);
    }

    @Test
    @Transactional
    public void createConfigFareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configFareRepository.findAll().size();

        // Create the ConfigFare with an existing ID
        configFare.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigFareMockMvc.perform(post("/api/config-fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configFare)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigFare in the database
        List<ConfigFare> configFareList = configFareRepository.findAll();
        assertThat(configFareList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigFare in Elasticsearch
        verify(mockConfigFareSearchRepository, times(0)).save(configFare);
    }

    @Test
    @Transactional
    public void getAllConfigFares() throws Exception {
        // Initialize the database
        configFareRepository.saveAndFlush(configFare);

        // Get all the configFareList
        restConfigFareMockMvc.perform(get("/api/config-fares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configFare.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fare").value(hasItem(DEFAULT_FARE)))
            .andExpect(jsonPath("$.[*].cancelable").value(hasItem(DEFAULT_CANCELABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].penaltyCancel").value(hasItem(DEFAULT_PENALTY_CANCEL)))
            .andExpect(jsonPath("$.[*].noshow").value(hasItem(DEFAULT_NOSHOW.booleanValue())))
            .andExpect(jsonPath("$.[*].penaltyNoShow").value(hasItem(DEFAULT_PENALTY_NO_SHOW)));
    }
    
    @Test
    @Transactional
    public void getConfigFare() throws Exception {
        // Initialize the database
        configFareRepository.saveAndFlush(configFare);

        // Get the configFare
        restConfigFareMockMvc.perform(get("/api/config-fares/{id}", configFare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configFare.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.fare").value(DEFAULT_FARE))
            .andExpect(jsonPath("$.cancelable").value(DEFAULT_CANCELABLE.booleanValue()))
            .andExpect(jsonPath("$.penaltyCancel").value(DEFAULT_PENALTY_CANCEL))
            .andExpect(jsonPath("$.noshow").value(DEFAULT_NOSHOW.booleanValue()))
            .andExpect(jsonPath("$.penaltyNoShow").value(DEFAULT_PENALTY_NO_SHOW));
    }

    @Test
    @Transactional
    public void getNonExistingConfigFare() throws Exception {
        // Get the configFare
        restConfigFareMockMvc.perform(get("/api/config-fares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigFare() throws Exception {
        // Initialize the database
        configFareRepository.saveAndFlush(configFare);

        int databaseSizeBeforeUpdate = configFareRepository.findAll().size();

        // Update the configFare
        ConfigFare updatedConfigFare = configFareRepository.findById(configFare.getId()).get();
        // Disconnect from session so that the updates on updatedConfigFare are not directly saved in db
        em.detach(updatedConfigFare);
        updatedConfigFare
            .numero(UPDATED_NUMERO)
            .fare(UPDATED_FARE)
            .cancelable(UPDATED_CANCELABLE)
            .penaltyCancel(UPDATED_PENALTY_CANCEL)
            .noshow(UPDATED_NOSHOW)
            .penaltyNoShow(UPDATED_PENALTY_NO_SHOW);

        restConfigFareMockMvc.perform(put("/api/config-fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigFare)))
            .andExpect(status().isOk());

        // Validate the ConfigFare in the database
        List<ConfigFare> configFareList = configFareRepository.findAll();
        assertThat(configFareList).hasSize(databaseSizeBeforeUpdate);
        ConfigFare testConfigFare = configFareList.get(configFareList.size() - 1);
        assertThat(testConfigFare.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testConfigFare.getFare()).isEqualTo(UPDATED_FARE);
        assertThat(testConfigFare.isCancelable()).isEqualTo(UPDATED_CANCELABLE);
        assertThat(testConfigFare.getPenaltyCancel()).isEqualTo(UPDATED_PENALTY_CANCEL);
        assertThat(testConfigFare.isNoshow()).isEqualTo(UPDATED_NOSHOW);
        assertThat(testConfigFare.getPenaltyNoShow()).isEqualTo(UPDATED_PENALTY_NO_SHOW);

        // Validate the ConfigFare in Elasticsearch
        verify(mockConfigFareSearchRepository, times(1)).save(testConfigFare);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigFare() throws Exception {
        int databaseSizeBeforeUpdate = configFareRepository.findAll().size();

        // Create the ConfigFare

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigFareMockMvc.perform(put("/api/config-fares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configFare)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigFare in the database
        List<ConfigFare> configFareList = configFareRepository.findAll();
        assertThat(configFareList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigFare in Elasticsearch
        verify(mockConfigFareSearchRepository, times(0)).save(configFare);
    }

    @Test
    @Transactional
    public void deleteConfigFare() throws Exception {
        // Initialize the database
        configFareRepository.saveAndFlush(configFare);

        int databaseSizeBeforeDelete = configFareRepository.findAll().size();

        // Get the configFare
        restConfigFareMockMvc.perform(delete("/api/config-fares/{id}", configFare.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigFare> configFareList = configFareRepository.findAll();
        assertThat(configFareList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigFare in Elasticsearch
        verify(mockConfigFareSearchRepository, times(1)).deleteById(configFare.getId());
    }

    @Test
    @Transactional
    public void searchConfigFare() throws Exception {
        // Initialize the database
        configFareRepository.saveAndFlush(configFare);
        when(mockConfigFareSearchRepository.search(queryStringQuery("id:" + configFare.getId())))
            .thenReturn(Collections.singletonList(configFare));
        // Search the configFare
        restConfigFareMockMvc.perform(get("/api/_search/config-fares?query=id:" + configFare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configFare.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fare").value(hasItem(DEFAULT_FARE)))
            .andExpect(jsonPath("$.[*].cancelable").value(hasItem(DEFAULT_CANCELABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].penaltyCancel").value(hasItem(DEFAULT_PENALTY_CANCEL)))
            .andExpect(jsonPath("$.[*].noshow").value(hasItem(DEFAULT_NOSHOW.booleanValue())))
            .andExpect(jsonPath("$.[*].penaltyNoShow").value(hasItem(DEFAULT_PENALTY_NO_SHOW)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigFare.class);
        ConfigFare configFare1 = new ConfigFare();
        configFare1.setId(1L);
        ConfigFare configFare2 = new ConfigFare();
        configFare2.setId(configFare1.getId());
        assertThat(configFare1).isEqualTo(configFare2);
        configFare2.setId(2L);
        assertThat(configFare1).isNotEqualTo(configFare2);
        configFare1.setId(null);
        assertThat(configFare1).isNotEqualTo(configFare2);
    }
}
