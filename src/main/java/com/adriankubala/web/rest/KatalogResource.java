package com.adriankubala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adriankubala.domain.Katalog;

import com.adriankubala.repository.KatalogRepository;
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
 * REST controller for managing Katalog.
 */
@RestController
@RequestMapping("/api")
public class KatalogResource {

    private final Logger log = LoggerFactory.getLogger(KatalogResource.class);

    private static final String ENTITY_NAME = "katalog";
        
    private final KatalogRepository katalogRepository;

    public KatalogResource(KatalogRepository katalogRepository) {
        this.katalogRepository = katalogRepository;
    }

    /**
     * POST  /katalogs : Create a new katalog.
     *
     * @param katalog the katalog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new katalog, or with status 400 (Bad Request) if the katalog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/katalogs")
    @Timed
    public ResponseEntity<Katalog> createKatalog(@Valid @RequestBody Katalog katalog) throws URISyntaxException {
        log.debug("REST request to save Katalog : {}", katalog);
        if (katalog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new katalog cannot already have an ID")).body(null);
        }
        Katalog result = katalogRepository.save(katalog);
        return ResponseEntity.created(new URI("/api/katalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /katalogs : Updates an existing katalog.
     *
     * @param katalog the katalog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated katalog,
     * or with status 400 (Bad Request) if the katalog is not valid,
     * or with status 500 (Internal Server Error) if the katalog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/katalogs")
    @Timed
    public ResponseEntity<Katalog> updateKatalog(@Valid @RequestBody Katalog katalog) throws URISyntaxException {
        log.debug("REST request to update Katalog : {}", katalog);
        if (katalog.getId() == null) {
            return createKatalog(katalog);
        }
        Katalog result = katalogRepository.save(katalog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, katalog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /katalogs : get all the katalogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of katalogs in body
     */
    @GetMapping("/katalogs")
    @Timed
    public List<Katalog> getAllKatalogs() {
        log.debug("REST request to get all Katalogs");
        List<Katalog> katalogs = katalogRepository.findAll();
        return katalogs;
    }

    /**
     * GET  /katalogs/:id : get the "id" katalog.
     *
     * @param id the id of the katalog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the katalog, or with status 404 (Not Found)
     */
    @GetMapping("/katalogs/{id}")
    @Timed
    public ResponseEntity<Katalog> getKatalog(@PathVariable Long id) {
        log.debug("REST request to get Katalog : {}", id);
        Katalog katalog = katalogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(katalog));
    }

    /**
     * DELETE  /katalogs/:id : delete the "id" katalog.
     *
     * @param id the id of the katalog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/katalogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteKatalog(@PathVariable Long id) {
        log.debug("REST request to delete Katalog : {}", id);
        katalogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
