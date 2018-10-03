package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.Travel;
import cm.com.letsgo.repository.TravelRepository;
import cm.com.letsgo.repository.search.TravelSearchRepository;
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
 * REST controller for managing Travel.
 */
@RestController
@RequestMapping("/api")
public class TravelResource {

    private final Logger log = LoggerFactory.getLogger(TravelResource.class);

    private static final String ENTITY_NAME = "travel";

    private final TravelRepository travelRepository;

    private final TravelSearchRepository travelSearchRepository;

    public TravelResource(TravelRepository travelRepository, TravelSearchRepository travelSearchRepository) {
        this.travelRepository = travelRepository;
        this.travelSearchRepository = travelSearchRepository;
    }

    /**
     * POST  /travels : Create a new travel.
     *
     * @param travel the travel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new travel, or with status 400 (Bad Request) if the travel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/travels")
    @Timed
    public ResponseEntity<Travel> createTravel(@RequestBody Travel travel) throws URISyntaxException {
        log.debug("REST request to save Travel : {}", travel);
        if (travel.getId() != null) {
            throw new BadRequestAlertException("A new travel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Travel result = travelRepository.save(travel);
        travelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/travels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /travels : Updates an existing travel.
     *
     * @param travel the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel,
     * or with status 400 (Bad Request) if the travel is not valid,
     * or with status 500 (Internal Server Error) if the travel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/travels")
    @Timed
    public ResponseEntity<Travel> updateTravel(@RequestBody Travel travel) throws URISyntaxException {
        log.debug("REST request to update Travel : {}", travel);
        if (travel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Travel result = travelRepository.save(travel);
        travelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, travel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /travels : get all the travels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of travels in body
     */
    @GetMapping("/travels")
    @Timed
    public List<Travel> getAllTravels() {
        log.debug("REST request to get all Travels");
        return travelRepository.findAll();
    }

    /**
     * GET  /travels/:id : get the "id" travel.
     *
     * @param id the id of the travel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the travel, or with status 404 (Not Found)
     */
    @GetMapping("/travels/{id}")
    @Timed
    public ResponseEntity<Travel> getTravel(@PathVariable Long id) {
        log.debug("REST request to get Travel : {}", id);
        Optional<Travel> travel = travelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(travel);
    }

    /**
     * DELETE  /travels/:id : delete the "id" travel.
     *
     * @param id the id of the travel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/travels/{id}")
    @Timed
    public ResponseEntity<Void> deleteTravel(@PathVariable Long id) {
        log.debug("REST request to delete Travel : {}", id);

        travelRepository.deleteById(id);
        travelSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/travels?query=:query : search for the travel corresponding
     * to the query.
     *
     * @param query the query of the travel search
     * @return the result of the search
     */
    @GetMapping("/_search/travels")
    @Timed
    public List<Travel> searchTravels(@RequestParam String query) {
        log.debug("REST request to search Travels for query {}", query);
        return StreamSupport
            .stream(travelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
