package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.ConfigCommission;
import cm.com.letsgo.repository.ConfigCommissionRepository;
import cm.com.letsgo.repository.search.ConfigCommissionSearchRepository;
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
 * REST controller for managing ConfigCommission.
 */
@RestController
@RequestMapping("/api")
public class ConfigCommissionResource {

    private final Logger log = LoggerFactory.getLogger(ConfigCommissionResource.class);

    private static final String ENTITY_NAME = "configCommission";

    private final ConfigCommissionRepository configCommissionRepository;

    private final ConfigCommissionSearchRepository configCommissionSearchRepository;

    public ConfigCommissionResource(ConfigCommissionRepository configCommissionRepository, ConfigCommissionSearchRepository configCommissionSearchRepository) {
        this.configCommissionRepository = configCommissionRepository;
        this.configCommissionSearchRepository = configCommissionSearchRepository;
    }

    /**
     * POST  /config-commissions : Create a new configCommission.
     *
     * @param configCommission the configCommission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configCommission, or with status 400 (Bad Request) if the configCommission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-commissions")
    @Timed
    public ResponseEntity<ConfigCommission> createConfigCommission(@RequestBody ConfigCommission configCommission) throws URISyntaxException {
        log.debug("REST request to save ConfigCommission : {}", configCommission);
        if (configCommission.getId() != null) {
            throw new BadRequestAlertException("A new configCommission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigCommission result = configCommissionRepository.save(configCommission);
        configCommissionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/config-commissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-commissions : Updates an existing configCommission.
     *
     * @param configCommission the configCommission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configCommission,
     * or with status 400 (Bad Request) if the configCommission is not valid,
     * or with status 500 (Internal Server Error) if the configCommission couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-commissions")
    @Timed
    public ResponseEntity<ConfigCommission> updateConfigCommission(@RequestBody ConfigCommission configCommission) throws URISyntaxException {
        log.debug("REST request to update ConfigCommission : {}", configCommission);
        if (configCommission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigCommission result = configCommissionRepository.save(configCommission);
        configCommissionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configCommission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-commissions : get all the configCommissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of configCommissions in body
     */
    @GetMapping("/config-commissions")
    @Timed
    public List<ConfigCommission> getAllConfigCommissions() {
        log.debug("REST request to get all ConfigCommissions");
        return configCommissionRepository.findAll();
    }

    /**
     * GET  /config-commissions/:id : get the "id" configCommission.
     *
     * @param id the id of the configCommission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configCommission, or with status 404 (Not Found)
     */
    @GetMapping("/config-commissions/{id}")
    @Timed
    public ResponseEntity<ConfigCommission> getConfigCommission(@PathVariable Long id) {
        log.debug("REST request to get ConfigCommission : {}", id);
        Optional<ConfigCommission> configCommission = configCommissionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(configCommission);
    }

    /**
     * DELETE  /config-commissions/:id : delete the "id" configCommission.
     *
     * @param id the id of the configCommission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-commissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigCommission(@PathVariable Long id) {
        log.debug("REST request to delete ConfigCommission : {}", id);

        configCommissionRepository.deleteById(id);
        configCommissionSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/config-commissions?query=:query : search for the configCommission corresponding
     * to the query.
     *
     * @param query the query of the configCommission search
     * @return the result of the search
     */
    @GetMapping("/_search/config-commissions")
    @Timed
    public List<ConfigCommission> searchConfigCommissions(@RequestParam String query) {
        log.debug("REST request to search ConfigCommissions for query {}", query);
        return StreamSupport
            .stream(configCommissionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
