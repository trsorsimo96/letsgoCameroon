package cm.com.letsgo.web.rest;

import cm.com.letsgo.LetsgoApp;

import cm.com.letsgo.domain.Cabin;
import cm.com.letsgo.repository.CabinRepository;
import cm.com.letsgo.repository.search.CabinSearchRepository;
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
 * Test class for the CabinResource REST controller.
 *
 * @see CabinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetsgoApp.class)
public class CabinResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private CabinRepository cabinRepository;

    /**
     * This repository is mocked in the cm.com.letsgo.repository.search test package.
     *
     * @see cm.com.letsgo.repository.search.CabinSearchRepositoryMockConfiguration
     */
    @Autowired
    private CabinSearchRepository mockCabinSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCabinMockMvc;

    private Cabin cabin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CabinResource cabinResource = new CabinResource(cabinRepository, mockCabinSearchRepository);
        this.restCabinMockMvc = MockMvcBuilders.standaloneSetup(cabinResource)
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
    public static Cabin createEntity(EntityManager em) {
        Cabin cabin = new Cabin()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE);
        return cabin;
    }

    @Before
    public void initTest() {
        cabin = createEntity(em);
    }

    @Test
    @Transactional
    public void createCabin() throws Exception {
        int databaseSizeBeforeCreate = cabinRepository.findAll().size();

        // Create the Cabin
        restCabinMockMvc.perform(post("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isCreated());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeCreate + 1);
        Cabin testCabin = cabinList.get(cabinList.size() - 1);
        assertThat(testCabin.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCabin.getTitle()).isEqualTo(DEFAULT_TITLE);

        // Validate the Cabin in Elasticsearch
        verify(mockCabinSearchRepository, times(1)).save(testCabin);
    }

    @Test
    @Transactional
    public void createCabinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cabinRepository.findAll().size();

        // Create the Cabin with an existing ID
        cabin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinMockMvc.perform(post("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isBadRequest());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cabin in Elasticsearch
        verify(mockCabinSearchRepository, times(0)).save(cabin);
    }

    @Test
    @Transactional
    public void getAllCabins() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);

        // Get all the cabinList
        restCabinMockMvc.perform(get("/api/cabins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabin.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void getCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);

        // Get the cabin
        restCabinMockMvc.perform(get("/api/cabins/{id}", cabin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cabin.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCabin() throws Exception {
        // Get the cabin
        restCabinMockMvc.perform(get("/api/cabins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);

        int databaseSizeBeforeUpdate = cabinRepository.findAll().size();

        // Update the cabin
        Cabin updatedCabin = cabinRepository.findById(cabin.getId()).get();
        // Disconnect from session so that the updates on updatedCabin are not directly saved in db
        em.detach(updatedCabin);
        updatedCabin
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE);

        restCabinMockMvc.perform(put("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCabin)))
            .andExpect(status().isOk());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeUpdate);
        Cabin testCabin = cabinList.get(cabinList.size() - 1);
        assertThat(testCabin.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCabin.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the Cabin in Elasticsearch
        verify(mockCabinSearchRepository, times(1)).save(testCabin);
    }

    @Test
    @Transactional
    public void updateNonExistingCabin() throws Exception {
        int databaseSizeBeforeUpdate = cabinRepository.findAll().size();

        // Create the Cabin

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCabinMockMvc.perform(put("/api/cabins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabin)))
            .andExpect(status().isBadRequest());

        // Validate the Cabin in the database
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cabin in Elasticsearch
        verify(mockCabinSearchRepository, times(0)).save(cabin);
    }

    @Test
    @Transactional
    public void deleteCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);

        int databaseSizeBeforeDelete = cabinRepository.findAll().size();

        // Get the cabin
        restCabinMockMvc.perform(delete("/api/cabins/{id}", cabin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cabin> cabinList = cabinRepository.findAll();
        assertThat(cabinList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cabin in Elasticsearch
        verify(mockCabinSearchRepository, times(1)).deleteById(cabin.getId());
    }

    @Test
    @Transactional
    public void searchCabin() throws Exception {
        // Initialize the database
        cabinRepository.saveAndFlush(cabin);
        when(mockCabinSearchRepository.search(queryStringQuery("id:" + cabin.getId())))
            .thenReturn(Collections.singletonList(cabin));
        // Search the cabin
        restCabinMockMvc.perform(get("/api/_search/cabins?query=id:" + cabin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabin.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cabin.class);
        Cabin cabin1 = new Cabin();
        cabin1.setId(1L);
        Cabin cabin2 = new Cabin();
        cabin2.setId(cabin1.getId());
        assertThat(cabin1).isEqualTo(cabin2);
        cabin2.setId(2L);
        assertThat(cabin1).isNotEqualTo(cabin2);
        cabin1.setId(null);
        assertThat(cabin1).isNotEqualTo(cabin2);
    }
}
