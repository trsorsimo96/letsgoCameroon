package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.Resa;
import cm.com.letsgo.repository.ResaRepository;
import cm.com.letsgo.repository.search.ResaSearchRepository;
import cm.com.letsgo.web.rest.errors.BadRequestAlertException;
import cm.com.letsgo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Resa.
 */
@RestController
@RequestMapping("/api")
public class ResaResource {

    private final Logger log = LoggerFactory.getLogger(ResaResource.class);

    private static final String ENTITY_NAME = "resa";

    private final ResaRepository resaRepository;

    private final ResaSearchRepository resaSearchRepository;

    public ResaResource(ResaRepository resaRepository, ResaSearchRepository resaSearchRepository) {
        this.resaRepository = resaRepository;
        this.resaSearchRepository = resaSearchRepository;
    }

    /**
     * POST  /resas : Create a new resa.
     *
     * @param resa the resa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resa, or with status 400 (Bad Request) if the resa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resas")
    @Timed
    public ResponseEntity<Resa> createResa(@RequestBody Resa resa) throws URISyntaxException {
        log.debug("REST request to save Resa : {}", resa);
        if (resa.getId() != null) {
            throw new BadRequestAlertException("A new resa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resa result = resaRepository.save(resa);
        resaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/resas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resas : Updates an existing resa.
     *
     * @param resa the resa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resa,
     * or with status 400 (Bad Request) if the resa is not valid,
     * or with status 500 (Internal Server Error) if the resa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resas")
    @Timed
    public ResponseEntity<Resa> updateResa(@RequestBody Resa resa) throws URISyntaxException {
        log.debug("REST request to update Resa : {}", resa);
        if (resa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Resa result = resaRepository.save(resa);
        resaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resas : get all the resas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resas in body
     */
    @GetMapping("/resas")
    @Timed
    public List<Resa> getAllResas() {
        log.debug("REST request to get all Resas");
        return resaRepository.findAll();
    }

    /**
     * GET  /resas/:id : get the "id" resa.
     *
     * @param id the id of the resa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resa, or with status 404 (Not Found)
     */
    @GetMapping("/resas/{id}")
    @Timed
    public ResponseEntity<Resa> getResa(@PathVariable Long id) {
        log.debug("REST request to get Resa : {}", id);
        Optional<Resa> resa = resaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resa);
    }

    /**
     * DELETE  /resas/:id : delete the "id" resa.
     *
     * @param id the id of the resa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resas/{id}")
    @Timed
    public ResponseEntity<Void> deleteResa(@PathVariable Long id) {
        log.debug("REST request to delete Resa : {}", id);

        resaRepository.deleteById(id);
        resaSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/resas?query=:query : search for the resa corresponding
     * to the query.
     *
     * @param query the query of the resa search
     * @return the result of the search
     */
    @GetMapping("/_search/resas")
    @Timed
    public List<Resa> searchResas(@RequestParam String query) {
        log.debug("REST request to search Resas for query {}", query);
        return StreamSupport
            .stream(resaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
