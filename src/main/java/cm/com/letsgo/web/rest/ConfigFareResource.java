package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.ConfigFare;
import cm.com.letsgo.repository.ConfigFareRepository;
import cm.com.letsgo.repository.search.ConfigFareSearchRepository;
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
 * REST controller for managing ConfigFare.
 */
@RestController
@RequestMapping("/api")
public class ConfigFareResource {

    private final Logger log = LoggerFactory.getLogger(ConfigFareResource.class);

    private static final String ENTITY_NAME = "configFare";

    private final ConfigFareRepository configFareRepository;

    private final ConfigFareSearchRepository configFareSearchRepository;

    public ConfigFareResource(ConfigFareRepository configFareRepository, ConfigFareSearchRepository configFareSearchRepository) {
        this.configFareRepository = configFareRepository;
        this.configFareSearchRepository = configFareSearchRepository;
    }

    /**
     * POST  /config-fares : Create a new configFare.
     *
     * @param configFare the configFare to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configFare, or with status 400 (Bad Request) if the configFare has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-fares")
    @Timed
    public ResponseEntity<ConfigFare> createConfigFare(@RequestBody ConfigFare configFare) throws URISyntaxException {
        log.debug("REST request to save ConfigFare : {}", configFare);
        if (configFare.getId() != null) {
            throw new BadRequestAlertException("A new configFare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigFare result = configFareRepository.save(configFare);
        configFareSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/config-fares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-fares : Updates an existing configFare.
     *
     * @param configFare the configFare to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configFare,
     * or with status 400 (Bad Request) if the configFare is not valid,
     * or with status 500 (Internal Server Error) if the configFare couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-fares")
    @Timed
    public ResponseEntity<ConfigFare> updateConfigFare(@RequestBody ConfigFare configFare) throws URISyntaxException {
        log.debug("REST request to update ConfigFare : {}", configFare);
        if (configFare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigFare result = configFareRepository.save(configFare);
        configFareSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configFare.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-fares : get all the configFares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of configFares in body
     */
    @GetMapping("/config-fares")
    @Timed
    public List<ConfigFare> getAllConfigFares() {
        log.debug("REST request to get all ConfigFares");
        return configFareRepository.findAll();
    }

    /**
     * GET  /config-fares/:id : get the "id" configFare.
     *
     * @param id the id of the configFare to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configFare, or with status 404 (Not Found)
     */
    @GetMapping("/config-fares/{id}")
    @Timed
    public ResponseEntity<ConfigFare> getConfigFare(@PathVariable Long id) {
        log.debug("REST request to get ConfigFare : {}", id);
        Optional<ConfigFare> configFare = configFareRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(configFare);
    }

    /**
     * DELETE  /config-fares/:id : delete the "id" configFare.
     *
     * @param id the id of the configFare to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-fares/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigFare(@PathVariable Long id) {
        log.debug("REST request to delete ConfigFare : {}", id);

        configFareRepository.deleteById(id);
        configFareSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/config-fares?query=:query : search for the configFare corresponding
     * to the query.
     *
     * @param query the query of the configFare search
     * @return the result of the search
     */
    @GetMapping("/_search/config-fares")
    @Timed
    public List<ConfigFare> searchConfigFares(@RequestParam String query) {
        log.debug("REST request to search ConfigFares for query {}", query);
        return StreamSupport
            .stream(configFareSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
