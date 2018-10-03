package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.ConfigCommission;
import cm.com.letsgo.repository.ConfigCommissionRepository;
import cm.com.letsgo.repository.search.ConfigCommissionSearchRepository;
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
 * Test class for the ConfigCommissionResource REST controller.
 *
 * @see ConfigCommissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class ConfigCommissionResourceIntTest {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final Integer DEFAULT_COMMISION = 1;
    private static final Integer UPDATED_COMMISION = 2;

    private static final Integer DEFAULT_COMMISION_PARTNER = 1;
    private static final Integer UPDATED_COMMISION_PARTNER = 2;

    @Autowired
    private ConfigCommissionRepository configCommissionRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.ConfigCommissionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigCommissionSearchRepository mockConfigCommissionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfigCommissionMockMvc;

    private ConfigCommission configCommission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigCommissionResource configCommissionResource = new ConfigCommissionResource(configCommissionRepository, mockConfigCommissionSearchRepository);
        this.restConfigCommissionMockMvc = MockMvcBuilders.standaloneSetup(configCommissionResource)
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
    public static ConfigCommission createEntity(EntityManager em) {
        ConfigCommission configCommission = new ConfigCommission()
            .numero(DEFAULT_NUMERO)
            .commision(DEFAULT_COMMISION)
            .commisionPartner(DEFAULT_COMMISION_PARTNER);
        return configCommission;
    }

    @Before
    public void initTest() {
        configCommission = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigCommission() throws Exception {
        int databaseSizeBeforeCreate = configCommissionRepository.findAll().size();

        // Create the ConfigCommission
        restConfigCommissionMockMvc.perform(post("/api/config-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configCommission)))
            .andExpect(status().isCreated());

        // Validate the ConfigCommission in the database
        List<ConfigCommission> configCommissionList = configCommissionRepository.findAll();
        assertThat(configCommissionList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigCommission testConfigCommission = configCommissionList.get(configCommissionList.size() - 1);
        assertThat(testConfigCommission.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testConfigCommission.getCommision()).isEqualTo(DEFAULT_COMMISION);
        assertThat(testConfigCommission.getCommisionPartner()).isEqualTo(DEFAULT_COMMISION_PARTNER);

        // Validate the ConfigCommission in Elasticsearch
        verify(mockConfigCommissionSearchRepository, times(1)).save(testConfigCommission);
    }

    @Test
    @Transactional
    public void createConfigCommissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configCommissionRepository.findAll().size();

        // Create the ConfigCommission with an existing ID
        configCommission.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigCommissionMockMvc.perform(post("/api/config-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configCommission)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigCommission in the database
        List<ConfigCommission> configCommissionList = configCommissionRepository.findAll();
        assertThat(configCommissionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigCommission in Elasticsearch
        verify(mockConfigCommissionSearchRepository, times(0)).save(configCommission);
    }

    @Test
    @Transactional
    public void getAllConfigCommissions() throws Exception {
        // Initialize the database
        configCommissionRepository.saveAndFlush(configCommission);

        // Get all the configCommissionList
        restConfigCommissionMockMvc.perform(get("/api/config-commissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configCommission.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].commision").value(hasItem(DEFAULT_COMMISION)))
            .andExpect(jsonPath("$.[*].commisionPartner").value(hasItem(DEFAULT_COMMISION_PARTNER)));
    }
    
    @Test
    @Transactional
    public void getConfigCommission() throws Exception {
        // Initialize the database
        configCommissionRepository.saveAndFlush(configCommission);

        // Get the configCommission
        restConfigCommissionMockMvc.perform(get("/api/config-commissions/{id}", configCommission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configCommission.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.commision").value(DEFAULT_COMMISION))
            .andExpect(jsonPath("$.commisionPartner").value(DEFAULT_COMMISION_PARTNER));
    }

    @Test
    @Transactional
    public void getNonExistingConfigCommission() throws Exception {
        // Get the configCommission
        restConfigCommissionMockMvc.perform(get("/api/config-commissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigCommission() throws Exception {
        // Initialize the database
        configCommissionRepository.saveAndFlush(configCommission);

        int databaseSizeBeforeUpdate = configCommissionRepository.findAll().size();

        // Update the configCommission
        ConfigCommission updatedConfigCommission = configCommissionRepository.findById(configCommission.getId()).get();
        // Disconnect from session so that the updates on updatedConfigCommission are not directly saved in db
        em.detach(updatedConfigCommission);
        updatedConfigCommission
            .numero(UPDATED_NUMERO)
            .commision(UPDATED_COMMISION)
            .commisionPartner(UPDATED_COMMISION_PARTNER);

        restConfigCommissionMockMvc.perform(put("/api/config-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigCommission)))
            .andExpect(status().isOk());

        // Validate the ConfigCommission in the database
        List<ConfigCommission> configCommissionList = configCommissionRepository.findAll();
        assertThat(configCommissionList).hasSize(databaseSizeBeforeUpdate);
        ConfigCommission testConfigCommission = configCommissionList.get(configCommissionList.size() - 1);
        assertThat(testConfigCommission.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testConfigCommission.getCommision()).isEqualTo(UPDATED_COMMISION);
        assertThat(testConfigCommission.getCommisionPartner()).isEqualTo(UPDATED_COMMISION_PARTNER);

        // Validate the ConfigCommission in Elasticsearch
        verify(mockConfigCommissionSearchRepository, times(1)).save(testConfigCommission);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigCommission() throws Exception {
        int databaseSizeBeforeUpdate = configCommissionRepository.findAll().size();

        // Create the ConfigCommission

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigCommissionMockMvc.perform(put("/api/config-commissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configCommission)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigCommission in the database
        List<ConfigCommission> configCommissionList = configCommissionRepository.findAll();
        assertThat(configCommissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigCommission in Elasticsearch
        verify(mockConfigCommissionSearchRepository, times(0)).save(configCommission);
    }

    @Test
    @Transactional
    public void deleteConfigCommission() throws Exception {
        // Initialize the database
        configCommissionRepository.saveAndFlush(configCommission);

        int databaseSizeBeforeDelete = configCommissionRepository.findAll().size();

        // Get the configCommission
        restConfigCommissionMockMvc.perform(delete("/api/config-commissions/{id}", configCommission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigCommission> configCommissionList = configCommissionRepository.findAll();
        assertThat(configCommissionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigCommission in Elasticsearch
        verify(mockConfigCommissionSearchRepository, times(1)).deleteById(configCommission.getId());
    }

    @Test
    @Transactional
    public void searchConfigCommission() throws Exception {
        // Initialize the database
        configCommissionRepository.saveAndFlush(configCommission);
        when(mockConfigCommissionSearchRepository.search(queryStringQuery("id:" + configCommission.getId())))
            .thenReturn(Collections.singletonList(configCommission));
        // Search the configCommission
        restConfigCommissionMockMvc.perform(get("/api/_search/config-commissions?query=id:" + configCommission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configCommission.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].commision").value(hasItem(DEFAULT_COMMISION)))
            .andExpect(jsonPath("$.[*].commisionPartner").value(hasItem(DEFAULT_COMMISION_PARTNER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigCommission.class);
        ConfigCommission configCommission1 = new ConfigCommission();
        configCommission1.setId(1L);
        ConfigCommission configCommission2 = new ConfigCommission();
        configCommission2.setId(configCommission1.getId());
        assertThat(configCommission1).isEqualTo(configCommission2);
        configCommission2.setId(2L);
        assertThat(configCommission1).isNotEqualTo(configCommission2);
        configCommission1.setId(null);
        assertThat(configCommission1).isNotEqualTo(configCommission2);
    }
}
