package com.adriankubala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adriankubala.domain.Plik;

import com.adriankubala.repository.PlikRepository;
import com.adriankubala.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Plik.
 */
@RestController
@RequestMapping("/api")
public class PlikResource {

    private final Logger log = LoggerFactory.getLogger(PlikResource.class);

    private static final String ENTITY_NAME = "plik";
        
    private final PlikRepository plikRepository;

    public PlikResource(PlikRepository plikRepository) {
        this.plikRepository = plikRepository;
    }

    /**
     * POST  /pliks : Create a new plik.
     *
     * @param plik the plik to create
     * @return the ResponseEntity with status 201 (Created) and with body the new plik, or with status 400 (Bad Request) if the plik has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pliks")
    @Timed
    public ResponseEntity<Plik> createPlik(@Valid @RequestBody Plik plik) throws URISyntaxException {
        log.debug("REST request to save Plik : {}", plik);
        if (plik.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new plik cannot already have an ID")).body(null);
        }
        Plik result = plikRepository.save(plik);
        return ResponseEntity.created(new URI("/api/pliks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pliks : Updates an existing plik.
     *
     * @param plik the plik to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated plik,
     * or with status 400 (Bad Request) if the plik is not valid,
     * or with status 500 (Internal Server Error) if the plik couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pliks")
    @Timed
    public ResponseEntity<Plik> updatePlik(@Valid @RequestBody Plik plik) throws URISyntaxException {
        log.debug("REST request to update Plik : {}", plik);
        if (plik.getId() == null) {
            return createPlik(plik);
        }
        Plik result = plikRepository.save(plik);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, plik.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pliks : get all the pliks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pliks in body
     */
    @GetMapping("/pliks")
    @Timed
    public List<Plik> getAllPliks() {
        log.debug("REST request to get all Pliks");
        List<Plik> pliks = plikRepository.findAll();
        return pliks;
    }

    /**
     * GET  /pliks/:id : get the "id" plik.
     *
     * @param id the id of the plik to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the plik, or with status 404 (Not Found)
     */
    @GetMapping("/pliks/{id}")
    @Timed
    public ResponseEntity<Plik> getPlik(@PathVariable Long id) {
        log.debug("REST request to get Plik : {}", id);
        Plik plik = plikRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(plik));
    }

    /**
     * DELETE  /pliks/:id : delete the "id" plik.
     *
     * @param id the id of the plik to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pliks/{id}")
    @Timed
    public ResponseEntity<Void> deletePlik(@PathVariable Long id) {
        log.debug("REST request to delete Plik : {}", id);
        plikRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
