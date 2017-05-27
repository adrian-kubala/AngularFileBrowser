package com.adriankubala.web.rest;

import com.adriankubala.AngularFileBrowserApp;

import com.adriankubala.domain.Katalog;
import com.adriankubala.repository.KatalogRepository;
import com.adriankubala.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the KatalogResource REST controller.
 *
 * @see KatalogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AngularFileBrowserApp.class)
public class KatalogResourceIntTest {

    private static final String DEFAULT_NAZWA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA = "BBBBBBBBBB";

    private static final String DEFAULT_NADRZEDNY = "AAAAAAAAAA";
    private static final String UPDATED_NADRZEDNY = "BBBBBBBBBB";

    @Autowired
    private KatalogRepository katalogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKatalogMockMvc;

    private Katalog katalog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KatalogResource katalogResource = new KatalogResource(katalogRepository);
        this.restKatalogMockMvc = MockMvcBuilders.standaloneSetup(katalogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Katalog createEntity(EntityManager em) {
        Katalog katalog = new Katalog()
            .nazwa(DEFAULT_NAZWA)
            .nadrzedny(DEFAULT_NADRZEDNY);
        return katalog;
    }

    @Before
    public void initTest() {
        katalog = createEntity(em);
    }

    @Test
    @Transactional
    public void createKatalog() throws Exception {
        int databaseSizeBeforeCreate = katalogRepository.findAll().size();

        // Create the Katalog
        restKatalogMockMvc.perform(post("/api/katalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(katalog)))
            .andExpect(status().isCreated());

        // Validate the Katalog in the database
        List<Katalog> katalogList = katalogRepository.findAll();
        assertThat(katalogList).hasSize(databaseSizeBeforeCreate + 1);
        Katalog testKatalog = katalogList.get(katalogList.size() - 1);
        assertThat(testKatalog.getNazwa()).isEqualTo(DEFAULT_NAZWA);
        assertThat(testKatalog.getNadrzedny()).isEqualTo(DEFAULT_NADRZEDNY);
    }

    @Test
    @Transactional
    public void createKatalogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = katalogRepository.findAll().size();

        // Create the Katalog with an existing ID
        katalog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKatalogMockMvc.perform(post("/api/katalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(katalog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Katalog> katalogList = katalogRepository.findAll();
        assertThat(katalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNazwaIsRequired() throws Exception {
        int databaseSizeBeforeTest = katalogRepository.findAll().size();
        // set the field null
        katalog.setNazwa(null);

        // Create the Katalog, which fails.

        restKatalogMockMvc.perform(post("/api/katalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(katalog)))
            .andExpect(status().isBadRequest());

        List<Katalog> katalogList = katalogRepository.findAll();
        assertThat(katalogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKatalogs() throws Exception {
        // Initialize the database
        katalogRepository.saveAndFlush(katalog);

        // Get all the katalogList
        restKatalogMockMvc.perform(get("/api/katalogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(katalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwa").value(hasItem(DEFAULT_NAZWA.toString())))
            .andExpect(jsonPath("$.[*].nadrzedny").value(hasItem(DEFAULT_NADRZEDNY.toString())));
    }

    @Test
    @Transactional
    public void getKatalog() throws Exception {
        // Initialize the database
        katalogRepository.saveAndFlush(katalog);

        // Get the katalog
        restKatalogMockMvc.perform(get("/api/katalogs/{id}", katalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(katalog.getId().intValue()))
            .andExpect(jsonPath("$.nazwa").value(DEFAULT_NAZWA.toString()))
            .andExpect(jsonPath("$.nadrzedny").value(DEFAULT_NADRZEDNY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKatalog() throws Exception {
        // Get the katalog
        restKatalogMockMvc.perform(get("/api/katalogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKatalog() throws Exception {
        // Initialize the database
        katalogRepository.saveAndFlush(katalog);
        int databaseSizeBeforeUpdate = katalogRepository.findAll().size();

        // Update the katalog
        Katalog updatedKatalog = katalogRepository.findOne(katalog.getId());
        updatedKatalog
            .nazwa(UPDATED_NAZWA)
            .nadrzedny(UPDATED_NADRZEDNY);

        restKatalogMockMvc.perform(put("/api/katalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedKatalog)))
            .andExpect(status().isOk());

        // Validate the Katalog in the database
        List<Katalog> katalogList = katalogRepository.findAll();
        assertThat(katalogList).hasSize(databaseSizeBeforeUpdate);
        Katalog testKatalog = katalogList.get(katalogList.size() - 1);
        assertThat(testKatalog.getNazwa()).isEqualTo(UPDATED_NAZWA);
        assertThat(testKatalog.getNadrzedny()).isEqualTo(UPDATED_NADRZEDNY);
    }

    @Test
    @Transactional
    public void updateNonExistingKatalog() throws Exception {
        int databaseSizeBeforeUpdate = katalogRepository.findAll().size();

        // Create the Katalog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKatalogMockMvc.perform(put("/api/katalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(katalog)))
            .andExpect(status().isCreated());

        // Validate the Katalog in the database
        List<Katalog> katalogList = katalogRepository.findAll();
        assertThat(katalogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteKatalog() throws Exception {
        // Initialize the database
        katalogRepository.saveAndFlush(katalog);
        int databaseSizeBeforeDelete = katalogRepository.findAll().size();

        // Get the katalog
        restKatalogMockMvc.perform(delete("/api/katalogs/{id}", katalog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Katalog> katalogList = katalogRepository.findAll();
        assertThat(katalogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Katalog.class);
    }
}
