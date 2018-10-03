package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.Planning;
import cm.com.letsgo.repository.PlanningRepository;
import cm.com.letsgo.repository.search.PlanningSearchRepository;
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
 * Test class for the PlanningResource REST controller.
 *
 * @see PlanningResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class PlanningResourceIntTest {

    private static final Boolean DEFAULT_MON = false;
    private static final Boolean UPDATED_MON = true;

    private static final Boolean DEFAULT_TUE = false;
    private static final Boolean UPDATED_TUE = true;

    private static final Boolean DEFAULT_WED = false;
    private static final Boolean UPDATED_WED = true;

    private static final Boolean DEFAULT_THU = false;
    private static final Boolean UPDATED_THU = true;

    private static final Boolean DEFAULT_FRI = false;
    private static final Boolean UPDATED_FRI = true;

    private static final Boolean DEFAULT_SAT = false;
    private static final Boolean UPDATED_SAT = true;

    private static final Boolean DEFAULT_SUN = false;
    private static final Boolean UPDATED_SUN = true;

    private static final ZonedDateTime DEFAULT_DEPARTURE_HOUR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURE_HOUR = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ARRIVAL_HOUR = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ARRIVAL_HOUR = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PlanningRepository planningRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.PlanningSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanningSearchRepository mockPlanningSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlanningMockMvc;

    private Planning planning;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanningResource planningResource = new PlanningResource(planningRepository, mockPlanningSearchRepository);
        this.restPlanningMockMvc = MockMvcBuilders.standaloneSetup(planningResource)
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
    public static Planning createEntity(EntityManager em) {
        Planning planning = new Planning()
            .mon(DEFAULT_MON)
            .tue(DEFAULT_TUE)
            .wed(DEFAULT_WED)
            .thu(DEFAULT_THU)
            .fri(DEFAULT_FRI)
            .sat(DEFAULT_SAT)
            .sun(DEFAULT_SUN)
            .departureHour(DEFAULT_DEPARTURE_HOUR)
            .arrivalHour(DEFAULT_ARRIVAL_HOUR);
        return planning;
    }

    @Before
    public void initTest() {
        planning = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanning() throws Exception {
        int databaseSizeBeforeCreate = planningRepository.findAll().size();

        // Create the Planning
        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isCreated());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate + 1);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.isMon()).isEqualTo(DEFAULT_MON);
        assertThat(testPlanning.isTue()).isEqualTo(DEFAULT_TUE);
        assertThat(testPlanning.isWed()).isEqualTo(DEFAULT_WED);
        assertThat(testPlanning.isThu()).isEqualTo(DEFAULT_THU);
        assertThat(testPlanning.isFri()).isEqualTo(DEFAULT_FRI);
        assertThat(testPlanning.isSat()).isEqualTo(DEFAULT_SAT);
        assertThat(testPlanning.isSun()).isEqualTo(DEFAULT_SUN);
        assertThat(testPlanning.getDepartureHour()).isEqualTo(DEFAULT_DEPARTURE_HOUR);
        assertThat(testPlanning.getArrivalHour()).isEqualTo(DEFAULT_ARRIVAL_HOUR);

        // Validate the Planning in Elasticsearch
        verify(mockPlanningSearchRepository, times(1)).save(testPlanning);
    }

    @Test
    @Transactional
    public void createPlanningWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planningRepository.findAll().size();

        // Create the Planning with an existing ID
        planning.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningMockMvc.perform(post("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate);

        // Validate the Planning in Elasticsearch
        verify(mockPlanningSearchRepository, times(0)).save(planning);
    }

    @Test
    @Transactional
    public void getAllPlannings() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get all the planningList
        restPlanningMockMvc.perform(get("/api/plannings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].mon").value(hasItem(DEFAULT_MON.booleanValue())))
            .andExpect(jsonPath("$.[*].tue").value(hasItem(DEFAULT_TUE.booleanValue())))
            .andExpect(jsonPath("$.[*].wed").value(hasItem(DEFAULT_WED.booleanValue())))
            .andExpect(jsonPath("$.[*].thu").value(hasItem(DEFAULT_THU.booleanValue())))
            .andExpect(jsonPath("$.[*].fri").value(hasItem(DEFAULT_FRI.booleanValue())))
            .andExpect(jsonPath("$.[*].sat").value(hasItem(DEFAULT_SAT.booleanValue())))
            .andExpect(jsonPath("$.[*].sun").value(hasItem(DEFAULT_SUN.booleanValue())))
            .andExpect(jsonPath("$.[*].departureHour").value(hasItem(sameInstant(DEFAULT_DEPARTURE_HOUR))))
            .andExpect(jsonPath("$.[*].arrivalHour").value(hasItem(sameInstant(DEFAULT_ARRIVAL_HOUR))));
    }
    
    @Test
    @Transactional
    public void getPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get the planning
        restPlanningMockMvc.perform(get("/api/plannings/{id}", planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planning.getId().intValue()))
            .andExpect(jsonPath("$.mon").value(DEFAULT_MON.booleanValue()))
            .andExpect(jsonPath("$.tue").value(DEFAULT_TUE.booleanValue()))
            .andExpect(jsonPath("$.wed").value(DEFAULT_WED.booleanValue()))
            .andExpect(jsonPath("$.thu").value(DEFAULT_THU.booleanValue()))
            .andExpect(jsonPath("$.fri").value(DEFAULT_FRI.booleanValue()))
            .andExpect(jsonPath("$.sat").value(DEFAULT_SAT.booleanValue()))
            .andExpect(jsonPath("$.sun").value(DEFAULT_SUN.booleanValue()))
            .andExpect(jsonPath("$.departureHour").value(sameInstant(DEFAULT_DEPARTURE_HOUR)))
            .andExpect(jsonPath("$.arrivalHour").value(sameInstant(DEFAULT_ARRIVAL_HOUR)));
    }

    @Test
    @Transactional
    public void getNonExistingPlanning() throws Exception {
        // Get the planning
        restPlanningMockMvc.perform(get("/api/plannings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning
        Planning updatedPlanning = planningRepository.findById(planning.getId()).get();
        // Disconnect from session so that the updates on updatedPlanning are not directly saved in db
        em.detach(updatedPlanning);
        updatedPlanning
            .mon(UPDATED_MON)
            .tue(UPDATED_TUE)
            .wed(UPDATED_WED)
            .thu(UPDATED_THU)
            .fri(UPDATED_FRI)
            .sat(UPDATED_SAT)
            .sun(UPDATED_SUN)
            .departureHour(UPDATED_DEPARTURE_HOUR)
            .arrivalHour(UPDATED_ARRIVAL_HOUR);

        restPlanningMockMvc.perform(put("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanning)))
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.isMon()).isEqualTo(UPDATED_MON);
        assertThat(testPlanning.isTue()).isEqualTo(UPDATED_TUE);
        assertThat(testPlanning.isWed()).isEqualTo(UPDATED_WED);
        assertThat(testPlanning.isThu()).isEqualTo(UPDATED_THU);
        assertThat(testPlanning.isFri()).isEqualTo(UPDATED_FRI);
        assertThat(testPlanning.isSat()).isEqualTo(UPDATED_SAT);
        assertThat(testPlanning.isSun()).isEqualTo(UPDATED_SUN);
        assertThat(testPlanning.getDepartureHour()).isEqualTo(UPDATED_DEPARTURE_HOUR);
        assertThat(testPlanning.getArrivalHour()).isEqualTo(UPDATED_ARRIVAL_HOUR);

        // Validate the Planning in Elasticsearch
        verify(mockPlanningSearchRepository, times(1)).save(testPlanning);
    }

    @Test
    @Transactional
    public void updateNonExistingPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Create the Planning

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc.perform(put("/api/plannings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planning)))
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Planning in Elasticsearch
        verify(mockPlanningSearchRepository, times(0)).save(planning);
    }

    @Test
    @Transactional
    public void deletePlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeDelete = planningRepository.findAll().size();

        // Get the planning
        restPlanningMockMvc.perform(delete("/api/plannings/{id}", planning.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Planning in Elasticsearch
        verify(mockPlanningSearchRepository, times(1)).deleteById(planning.getId());
    }

    @Test
    @Transactional
    public void searchPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);
        when(mockPlanningSearchRepository.search(queryStringQuery("id:" + planning.getId())))
            .thenReturn(Collections.singletonList(planning));
        // Search the planning
        restPlanningMockMvc.perform(get("/api/_search/plannings?query=id:" + planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].mon").value(hasItem(DEFAULT_MON.booleanValue())))
            .andExpect(jsonPath("$.[*].tue").value(hasItem(DEFAULT_TUE.booleanValue())))
            .andExpect(jsonPath("$.[*].wed").value(hasItem(DEFAULT_WED.booleanValue())))
            .andExpect(jsonPath("$.[*].thu").value(hasItem(DEFAULT_THU.booleanValue())))
            .andExpect(jsonPath("$.[*].fri").value(hasItem(DEFAULT_FRI.booleanValue())))
            .andExpect(jsonPath("$.[*].sat").value(hasItem(DEFAULT_SAT.booleanValue())))
            .andExpect(jsonPath("$.[*].sun").value(hasItem(DEFAULT_SUN.booleanValue())))
            .andExpect(jsonPath("$.[*].departureHour").value(hasItem(sameInstant(DEFAULT_DEPARTURE_HOUR))))
            .andExpect(jsonPath("$.[*].arrivalHour").value(hasItem(sameInstant(DEFAULT_ARRIVAL_HOUR))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planning.class);
        Planning planning1 = new Planning();
        planning1.setId(1L);
        Planning planning2 = new Planning();
        planning2.setId(planning1.getId());
        assertThat(planning1).isEqualTo(planning2);
        planning2.setId(2L);
        assertThat(planning1).isNotEqualTo(planning2);
        planning1.setId(null);
        assertThat(planning1).isNotEqualTo(planning2);
    }
}
