package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.Distributor;
import cm.com.letsgo.repository.DistributorRepository;
import cm.com.letsgo.repository.search.DistributorSearchRepository;
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
 * REST controller for managing Distributor.
 */
@RestController
@RequestMapping("/api")
public class DistributorResource {

    private final Logger log = LoggerFactory.getLogger(DistributorResource.class);

    private static final String ENTITY_NAME = "distributor";

    private final DistributorRepository distributorRepository;

    private final DistributorSearchRepository distributorSearchRepository;

    public DistributorResource(DistributorRepository distributorRepository, DistributorSearchRepository distributorSearchRepository) {
        this.distributorRepository = distributorRepository;
        this.distributorSearchRepository = distributorSearchRepository;
    }

    /**
     * POST  /distributors : Create a new distributor.
     *
     * @param distributor the distributor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new distributor, or with status 400 (Bad Request) if the distributor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/distributors")
    @Timed
    public ResponseEntity<Distributor> createDistributor(@RequestBody Distributor distributor) throws URISyntaxException {
        log.debug("REST request to save Distributor : {}", distributor);
        if (distributor.getId() != null) {
            throw new BadRequestAlertException("A new distributor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Distributor result = distributorRepository.save(distributor);
        distributorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/distributors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /distributors : Updates an existing distributor.
     *
     * @param distributor the distributor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated distributor,
     * or with status 400 (Bad Request) if the distributor is not valid,
     * or with status 500 (Internal Server Error) if the distributor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/distributors")
    @Timed
    public ResponseEntity<Distributor> updateDistributor(@RequestBody Distributor distributor) throws URISyntaxException {
        log.debug("REST request to update Distributor : {}", distributor);
        if (distributor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Distributor result = distributorRepository.save(distributor);
        distributorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, distributor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /distributors : get all the distributors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of distributors in body
     */
    @GetMapping("/distributors")
    @Timed
    public List<Distributor> getAllDistributors() {
        log.debug("REST request to get all Distributors");
        return distributorRepository.findAll();
    }

    /**
     * GET  /distributors/:id : get the "id" distributor.
     *
     * @param id the id of the distributor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the distributor, or with status 404 (Not Found)
     */
    @GetMapping("/distributors/{id}")
    @Timed
    public ResponseEntity<Distributor> getDistributor(@PathVariable Long id) {
        log.debug("REST request to get Distributor : {}", id);
        Optional<Distributor> distributor = distributorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(distributor);
    }

    /**
     * DELETE  /distributors/:id : delete the "id" distributor.
     *
     * @param id the id of the distributor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/distributors/{id}")
    @Timed
    public ResponseEntity<Void> deleteDistributor(@PathVariable Long id) {
        log.debug("REST request to delete Distributor : {}", id);

        distributorRepository.deleteById(id);
        distributorSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/distributors?query=:query : search for the distributor corresponding
     * to the query.
     *
     * @param query the query of the distributor search
     * @return the result of the search
     */
    @GetMapping("/_search/distributors")
    @Timed
    public List<Distributor> searchDistributors(@RequestParam String query) {
        log.debug("REST request to search Distributors for query {}", query);
        return StreamSupport
            .stream(distributorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
