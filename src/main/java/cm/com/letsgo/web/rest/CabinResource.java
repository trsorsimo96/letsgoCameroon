package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.Cabin;
import cm.com.letsgo.repository.CabinRepository;
import cm.com.letsgo.repository.search.CabinSearchRepository;
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
 * REST controller for managing Cabin.
 */
@RestController
@RequestMapping("/api")
public class CabinResource {

    private final Logger log = LoggerFactory.getLogger(CabinResource.class);

    private static final String ENTITY_NAME = "cabin";

    private final CabinRepository cabinRepository;

    private final CabinSearchRepository cabinSearchRepository;

    public CabinResource(CabinRepository cabinRepository, CabinSearchRepository cabinSearchRepository) {
        this.cabinRepository = cabinRepository;
        this.cabinSearchRepository = cabinSearchRepository;
    }

    /**
     * POST  /cabins : Create a new cabin.
     *
     * @param cabin the cabin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cabin, or with status 400 (Bad Request) if the cabin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cabins")
    @Timed
    public ResponseEntity<Cabin> createCabin(@RequestBody Cabin cabin) throws URISyntaxException {
        log.debug("REST request to save Cabin : {}", cabin);
        if (cabin.getId() != null) {
            throw new BadRequestAlertException("A new cabin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cabin result = cabinRepository.save(cabin);
        cabinSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cabins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cabins : Updates an existing cabin.
     *
     * @param cabin the cabin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cabin,
     * or with status 400 (Bad Request) if the cabin is not valid,
     * or with status 500 (Internal Server Error) if the cabin couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cabins")
    @Timed
    public ResponseEntity<Cabin> updateCabin(@RequestBody Cabin cabin) throws URISyntaxException {
        log.debug("REST request to update Cabin : {}", cabin);
        if (cabin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cabin result = cabinRepository.save(cabin);
        cabinSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cabin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cabins : get all the cabins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cabins in body
     */
    @GetMapping("/cabins")
    @Timed
    public List<Cabin> getAllCabins() {
        log.debug("REST request to get all Cabins");
        return cabinRepository.findAll();
    }

    /**
     * GET  /cabins/:id : get the "id" cabin.
     *
     * @param id the id of the cabin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cabin, or with status 404 (Not Found)
     */
    @GetMapping("/cabins/{id}")
    @Timed
    public ResponseEntity<Cabin> getCabin(@PathVariable Long id) {
        log.debug("REST request to get Cabin : {}", id);
        Optional<Cabin> cabin = cabinRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cabin);
    }

    /**
     * DELETE  /cabins/:id : delete the "id" cabin.
     *
     * @param id the id of the cabin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cabins/{id}")
    @Timed
    public ResponseEntity<Void> deleteCabin(@PathVariable Long id) {
        log.debug("REST request to delete Cabin : {}", id);

        cabinRepository.deleteById(id);
        cabinSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cabins?query=:query : search for the cabin corresponding
     * to the query.
     *
     * @param query the query of the cabin search
     * @return the result of the search
     */
    @GetMapping("/_search/cabins")
    @Timed
    public List<Cabin> searchCabins(@RequestParam String query) {
        log.debug("REST request to search Cabins for query {}", query);
        return StreamSupport
            .stream(cabinSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
