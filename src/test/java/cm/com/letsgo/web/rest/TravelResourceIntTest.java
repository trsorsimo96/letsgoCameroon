package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.Travel;
import cm.com.letsgo.repository.TravelRepository;
import cm.com.letsgo.repository.search.TravelSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static cm.com.letsgo.web.rest.TestUtil.sameInstant;
import static cm.com.letsgo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TravelResource REST controller.
 *
 * @see TravelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class TravelResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_NB_PLACE = 1;
    private static final Integer UPDATED_NB_PLACE = 2;

    private static final Integer DEFAULT_LEFT_PLACE = 1;
    private static final Integer UPDATED_LEFT_PLACE = 2;

    @Autowired
    private TravelRepository travelRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.TravelSearchRepositoryMockConfiguration
     */
    @Autowired
    private TravelSearchRepository mockTravelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTravelMockMvc;

    private Travel travel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TravelResource travelResource = new TravelResource(travelRepository, mockTravelSearchRepository);
        this.restTravelMockMvc = MockMvcBuilders.standaloneSetup(travelResource)
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
    public static Travel createEntity(EntityManager em) {
        Travel travel = new Travel()
            .number(DEFAULT_NUMBER)
            .date(DEFAULT_DATE)
            .nbPlace(DEFAULT_NB_PLACE)
            .leftPlace(DEFAULT_LEFT_PLACE);
        return travel;
    }

    @Before
    public void initTest() {
        travel = createEntity(em);
    }

    @Test
    @Transactional
    public void createTravel() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        // Create the Travel
        restTravelMockMvc.perform(post("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travel)))
            .andExpect(status().isCreated());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate + 1);
        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTravel.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTravel.getNbPlace()).isEqualTo(DEFAULT_NB_PLACE);
        assertThat(testTravel.getLeftPlace()).isEqualTo(DEFAULT_LEFT_PLACE);

        // Validate the Travel in Elasticsearch
        verify(mockTravelSearchRepository, times(1)).save(testTravel);
    }

    @Test
    @Transactional
    public void createTravelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        // Create the Travel with an existing ID
        travel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTravelMockMvc.perform(post("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travel)))
            .andExpect(status().isBadRequest());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate);

        // Validate the Travel in Elasticsearch
        verify(mockTravelSearchRepository, times(0)).save(travel);
    }

    @Test
    @Transactional
    public void getAllTravels() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get all the travelList
        restTravelMockMvc.perform(get("/api/travels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travel.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].nbPlace").value(hasItem(DEFAULT_NB_PLACE)))
            .andExpect(jsonPath("$.[*].leftPlace").value(hasItem(DEFAULT_LEFT_PLACE)));
    }
    
    @Test
    @Transactional
    public void getTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/{id}", travel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(travel.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.nbPlace").value(DEFAULT_NB_PLACE))
            .andExpect(jsonPath("$.leftPlace").value(DEFAULT_LEFT_PLACE));
    }

    @Test
    @Transactional
    public void getNonExistingTravel() throws Exception {
        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        int databaseSizeBeforeUpdate = travelRepository.findAll().size();

        // Update the travel
        Travel updatedTravel = travelRepository.findById(travel.getId()).get();
        // Disconnect from session so that the updates on updatedTravel are not directly saved in db
        em.detach(updatedTravel);
        updatedTravel
            .number(UPDATED_NUMBER)
            .date(UPDATED_DATE)
            .nbPlace(UPDATED_NB_PLACE)
            .leftPlace(UPDATED_LEFT_PLACE);

        restTravelMockMvc.perform(put("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTravel)))
            .andExpect(status().isOk());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeUpdate);
        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTravel.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTravel.getNbPlace()).isEqualTo(UPDATED_NB_PLACE);
        assertThat(testTravel.getLeftPlace()).isEqualTo(UPDATED_LEFT_PLACE);

        // Validate the Travel in Elasticsearch
        verify(mockTravelSearchRepository, times(1)).save(testTravel);
    }

    @Test
    @Transactional
    public void updateNonExistingTravel() throws Exception {
        int databaseSizeBeforeUpdate = travelRepository.findAll().size();

        // Create the Travel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTravelMockMvc.perform(put("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travel)))
            .andExpect(status().isBadRequest());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Travel in Elasticsearch
        verify(mockTravelSearchRepository, times(0)).save(travel);
    }

    @Test
    @Transactional
    public void deleteTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        int databaseSizeBeforeDelete = travelRepository.findAll().size();

        // Get the travel
        restTravelMockMvc.perform(delete("/api/travels/{id}", travel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Travel in Elasticsearch
        verify(mockTravelSearchRepository, times(1)).deleteById(travel.getId());
    }

    @Test
    @Transactional
    public void searchTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);
        when(mockTravelSearchRepository.search(queryStringQuery("id:" + travel.getId())))
            .thenReturn(Collections.singletonList(travel));
        // Search the travel
        restTravelMockMvc.perform(get("/api/_search/travels?query=id:" + travel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travel.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].nbPlace").value(hasItem(DEFAULT_NB_PLACE)))
            .andExpect(jsonPath("$.[*].leftPlace").value(hasItem(DEFAULT_LEFT_PLACE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Travel.class);
        Travel travel1 = new Travel();
        travel1.setId(1L);
        Travel travel2 = new Travel();
        travel2.setId(travel1.getId());
        assertThat(travel1).isEqualTo(travel2);
        travel2.setId(2L);
        assertThat(travel1).isNotEqualTo(travel2);
        travel1.setId(null);
        assertThat(travel1).isNotEqualTo(travel2);
    }
}
