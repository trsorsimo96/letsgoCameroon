package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.Resa;
import cm.com.letsgo.repository.ResaRepository;
import cm.com.letsgo.repository.search.ResaSearchRepository;
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
 * Test class for the ResaResource REST controller.
 *
 * @see ResaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class ResaResourceIntTest {

    private static final String DEFAULT_PASSENGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PASSENGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSENGER_CNI_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSENGER_CNI_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    @Autowired
    private ResaRepository resaRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.ResaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResaSearchRepository mockResaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResaMockMvc;

    private Resa resa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResaResource resaResource = new ResaResource(resaRepository, mockResaSearchRepository);
        this.restResaMockMvc = MockMvcBuilders.standaloneSetup(resaResource)
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
    public static Resa createEntity(EntityManager em) {
        Resa resa = new Resa()
            .passengerName(DEFAULT_PASSENGER_NAME)
            .passengerCniNumber(DEFAULT_PASSENGER_CNI_NUMBER)
            .email(DEFAULT_EMAIL)
            .number(DEFAULT_NUMBER);
        return resa;
    }

    @Before
    public void initTest() {
        resa = createEntity(em);
    }

    @Test
    @Transactional
    public void createResa() throws Exception {
        int databaseSizeBeforeCreate = resaRepository.findAll().size();

        // Create the Resa
        restResaMockMvc.perform(post("/api/resas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resa)))
            .andExpect(status().isCreated());

        // Validate the Resa in the database
        List<Resa> resaList = resaRepository.findAll();
        assertThat(resaList).hasSize(databaseSizeBeforeCreate + 1);
        Resa testResa = resaList.get(resaList.size() - 1);
        assertThat(testResa.getPassengerName()).isEqualTo(DEFAULT_PASSENGER_NAME);
        assertThat(testResa.getPassengerCniNumber()).isEqualTo(DEFAULT_PASSENGER_CNI_NUMBER);
        assertThat(testResa.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testResa.getNumber()).isEqualTo(DEFAULT_NUMBER);

        // Validate the Resa in Elasticsearch
        verify(mockResaSearchRepository, times(1)).save(testResa);
    }

    @Test
    @Transactional
    public void createResaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resaRepository.findAll().size();

        // Create the Resa with an existing ID
        resa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResaMockMvc.perform(post("/api/resas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resa)))
            .andExpect(status().isBadRequest());

        // Validate the Resa in the database
        List<Resa> resaList = resaRepository.findAll();
        assertThat(resaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Resa in Elasticsearch
        verify(mockResaSearchRepository, times(0)).save(resa);
    }

    @Test
    @Transactional
    public void getAllResas() throws Exception {
        // Initialize the database
        resaRepository.saveAndFlush(resa);

        // Get all the resaList
        restResaMockMvc.perform(get("/api/resas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resa.getId().intValue())))
            .andExpect(jsonPath("$.[*].passengerName").value(hasItem(DEFAULT_PASSENGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].passengerCniNumber").value(hasItem(DEFAULT_PASSENGER_CNI_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getResa() throws Exception {
        // Initialize the database
        resaRepository.saveAndFlush(resa);

        // Get the resa
        restResaMockMvc.perform(get("/api/resas/{id}", resa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resa.getId().intValue()))
            .andExpect(jsonPath("$.passengerName").value(DEFAULT_PASSENGER_NAME.toString()))
            .andExpect(jsonPath("$.passengerCniNumber").value(DEFAULT_PASSENGER_CNI_NUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResa() throws Exception {
        // Get the resa
        restResaMockMvc.perform(get("/api/resas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResa() throws Exception {
        // Initialize the database
        resaRepository.saveAndFlush(resa);

        int databaseSizeBeforeUpdate = resaRepository.findAll().size();

        // Update the resa
        Resa updatedResa = resaRepository.findById(resa.getId()).get();
        // Disconnect from session so that the updates on updatedResa are not directly saved in db
        em.detach(updatedResa);
        updatedResa
            .passengerName(UPDATED_PASSENGER_NAME)
            .passengerCniNumber(UPDATED_PASSENGER_CNI_NUMBER)
            .email(UPDATED_EMAIL)
            .number(UPDATED_NUMBER);

        restResaMockMvc.perform(put("/api/resas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResa)))
            .andExpect(status().isOk());

        // Validate the Resa in the database
        List<Resa> resaList = resaRepository.findAll();
        assertThat(resaList).hasSize(databaseSizeBeforeUpdate);
        Resa testResa = resaList.get(resaList.size() - 1);
        assertThat(testResa.getPassengerName()).isEqualTo(UPDATED_PASSENGER_NAME);
        assertThat(testResa.getPassengerCniNumber()).isEqualTo(UPDATED_PASSENGER_CNI_NUMBER);
        assertThat(testResa.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testResa.getNumber()).isEqualTo(UPDATED_NUMBER);

        // Validate the Resa in Elasticsearch
        verify(mockResaSearchRepository, times(1)).save(testResa);
    }

    @Test
    @Transactional
    public void updateNonExistingResa() throws Exception {
        int databaseSizeBeforeUpdate = resaRepository.findAll().size();

        // Create the Resa

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResaMockMvc.perform(put("/api/resas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resa)))
            .andExpect(status().isBadRequest());

        // Validate the Resa in the database
        List<Resa> resaList = resaRepository.findAll();
        assertThat(resaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resa in Elasticsearch
        verify(mockResaSearchRepository, times(0)).save(resa);
    }

    @Test
    @Transactional
    public void deleteResa() throws Exception {
        // Initialize the database
        resaRepository.saveAndFlush(resa);

        int databaseSizeBeforeDelete = resaRepository.findAll().size();

        // Get the resa
        restResaMockMvc.perform(delete("/api/resas/{id}", resa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Resa> resaList = resaRepository.findAll();
        assertThat(resaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Resa in Elasticsearch
        verify(mockResaSearchRepository, times(1)).deleteById(resa.getId());
    }

    @Test
    @Transactional
    public void searchResa() throws Exception {
        // Initialize the database
        resaRepository.saveAndFlush(resa);
        when(mockResaSearchRepository.search(queryStringQuery("id:" + resa.getId())))
            .thenReturn(Collections.singletonList(resa));
        // Search the resa
        restResaMockMvc.perform(get("/api/_search/resas?query=id:" + resa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resa.getId().intValue())))
            .andExpect(jsonPath("$.[*].passengerName").value(hasItem(DEFAULT_PASSENGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].passengerCniNumber").value(hasItem(DEFAULT_PASSENGER_CNI_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resa.class);
        Resa resa1 = new Resa();
        resa1.setId(1L);
        Resa resa2 = new Resa();
        resa2.setId(resa1.getId());
        assertThat(resa1).isEqualTo(resa2);
        resa2.setId(2L);
        assertThat(resa1).isNotEqualTo(resa2);
        resa1.setId(null);
        assertThat(resa1).isNotEqualTo(resa2);
    }
}
