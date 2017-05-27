package com.adriankubala.web.rest;

import com.adriankubala.AngularFileBrowserApp;

import com.adriankubala.domain.Plik;
import com.adriankubala.repository.PlikRepository;
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
 * Test class for the PlikResource REST controller.
 *
 * @see PlikResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AngularFileBrowserApp.class)
public class PlikResourceIntTest {

    private static final String DEFAULT_NAZWA = "AAAAAAAAAA";
    private static final String UPDATED_NAZWA = "BBBBBBBBBB";

    @Autowired
    private PlikRepository plikRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlikMockMvc;

    private Plik plik;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlikResource plikResource = new PlikResource(plikRepository);
        this.restPlikMockMvc = MockMvcBuilders.standaloneSetup(plikResource)
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
    public static Plik createEntity(EntityManager em) {
        Plik plik = new Plik()
            .nazwa(DEFAULT_NAZWA);
        return plik;
    }

    @Before
    public void initTest() {
        plik = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlik() throws Exception {
        int databaseSizeBeforeCreate = plikRepository.findAll().size();

        // Create the Plik
        restPlikMockMvc.perform(post("/api/pliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plik)))
            .andExpect(status().isCreated());

        // Validate the Plik in the database
        List<Plik> plikList = plikRepository.findAll();
        assertThat(plikList).hasSize(databaseSizeBeforeCreate + 1);
        Plik testPlik = plikList.get(plikList.size() - 1);
        assertThat(testPlik.getNazwa()).isEqualTo(DEFAULT_NAZWA);
    }

    @Test
    @Transactional
    public void createPlikWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plikRepository.findAll().size();

        // Create the Plik with an existing ID
        plik.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlikMockMvc.perform(post("/api/pliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plik)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Plik> plikList = plikRepository.findAll();
        assertThat(plikList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNazwaIsRequired() throws Exception {
        int databaseSizeBeforeTest = plikRepository.findAll().size();
        // set the field null
        plik.setNazwa(null);

        // Create the Plik, which fails.

        restPlikMockMvc.perform(post("/api/pliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plik)))
            .andExpect(status().isBadRequest());

        List<Plik> plikList = plikRepository.findAll();
        assertThat(plikList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPliks() throws Exception {
        // Initialize the database
        plikRepository.saveAndFlush(plik);

        // Get all the plikList
        restPlikMockMvc.perform(get("/api/pliks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plik.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazwa").value(hasItem(DEFAULT_NAZWA.toString())));
    }

    @Test
    @Transactional
    public void getPlik() throws Exception {
        // Initialize the database
        plikRepository.saveAndFlush(plik);

        // Get the plik
        restPlikMockMvc.perform(get("/api/pliks/{id}", plik.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plik.getId().intValue()))
            .andExpect(jsonPath("$.nazwa").value(DEFAULT_NAZWA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlik() throws Exception {
        // Get the plik
        restPlikMockMvc.perform(get("/api/pliks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlik() throws Exception {
        // Initialize the database
        plikRepository.saveAndFlush(plik);
        int databaseSizeBeforeUpdate = plikRepository.findAll().size();

        // Update the plik
        Plik updatedPlik = plikRepository.findOne(plik.getId());
        updatedPlik
            .nazwa(UPDATED_NAZWA);

        restPlikMockMvc.perform(put("/api/pliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlik)))
            .andExpect(status().isOk());

        // Validate the Plik in the database
        List<Plik> plikList = plikRepository.findAll();
        assertThat(plikList).hasSize(databaseSizeBeforeUpdate);
        Plik testPlik = plikList.get(plikList.size() - 1);
        assertThat(testPlik.getNazwa()).isEqualTo(UPDATED_NAZWA);
    }

    @Test
    @Transactional
    public void updateNonExistingPlik() throws Exception {
        int databaseSizeBeforeUpdate = plikRepository.findAll().size();

        // Create the Plik

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPlikMockMvc.perform(put("/api/pliks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plik)))
            .andExpect(status().isCreated());

        // Validate the Plik in the database
        List<Plik> plikList = plikRepository.findAll();
        assertThat(plikList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePlik() throws Exception {
        // Initialize the database
        plikRepository.saveAndFlush(plik);
        int databaseSizeBeforeDelete = plikRepository.findAll().size();

        // Get the plik
        restPlikMockMvc.perform(delete("/api/pliks/{id}", plik.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Plik> plikList = plikRepository.findAll();
        assertThat(plikList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plik.class);
    }
}
