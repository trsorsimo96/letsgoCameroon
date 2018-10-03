package cm.com.letsgo.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.com.letsgo.domain.Planning;
import cm.com.letsgo.repository.PlanningRepository;
import cm.com.letsgo.repository.search.PlanningSearchRepository;
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
 * REST controller for managing Planning.
 */
@RestController
@RequestMapping("/api")
public class PlanningResource {

    private final Logger log = LoggerFactory.getLogger(PlanningResource.class);

    private static final String ENTITY_NAME = "planning";

    private final PlanningRepository planningRepository;

    private final PlanningSearchRepository planningSearchRepository;

    public PlanningResource(PlanningRepository planningRepository, PlanningSearchRepository planningSearchRepository) {
        this.planningRepository = planningRepository;
        this.planningSearchRepository = planningSearchRepository;
    }

    /**
     * POST  /plannings : Create a new planning.
     *
     * @param planning the planning to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planning, or with status 400 (Bad Request) if the planning has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/plannings")
    @Timed
    public ResponseEntity<Planning> createPlanning(@RequestBody Planning planning) throws URISyntaxException {
        log.debug("REST request to save Planning : {}", planning);
        if (planning.getId() != null) {
            throw new BadRequestAlertException("A new planning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Planning result = planningRepository.save(planning);
        planningSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/plannings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /plannings : Updates an existing planning.
     *
     * @param planning the planning to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planning,
     * or with status 400 (Bad Request) if the planning is not valid,
     * or with status 500 (Internal Server Error) if the planning couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/plannings")
    @Timed
    public ResponseEntity<Planning> updatePlanning(@RequestBody Planning planning) throws URISyntaxException {
        log.debug("REST request to update Planning : {}", planning);
        if (planning.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Planning result = planningRepository.save(planning);
        planningSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planning.getId().toString()))
            .body(result);
    }

    /**
     * GET  /plannings : get all the plannings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of plannings in body
     */
    @GetMapping("/plannings")
    @Timed
    public List<Planning> getAllPlannings() {
        log.debug("REST request to get all Plannings");
        return planningRepository.findAll();
    }

    /**
     * GET  /plannings/:id : get the "id" planning.
     *
     * @param id the id of the planning to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planning, or with status 404 (Not Found)
     */
    @GetMapping("/plannings/{id}")
    @Timed
    public ResponseEntity<Planning> getPlanning(@PathVariable Long id) {
        log.debug("REST request to get Planning : {}", id);
        Optional<Planning> planning = planningRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(planning);
    }

    /**
     * DELETE  /plannings/:id : delete the "id" planning.
     *
     * @param id the id of the planning to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/plannings/{id}")
    @Timed
    public ResponseEntity<Void> deletePlanning(@PathVariable Long id) {
        log.debug("REST request to delete Planning : {}", id);

        planningRepository.deleteById(id);
        planningSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/plannings?query=:query : search for the planning corresponding
     * to the query.
     *
     * @param query the query of the planning search
     * @return the result of the search
     */
    @GetMapping("/_search/plannings")
    @Timed
    public List<Planning> searchPlannings(@RequestParam String query) {
        log.debug("REST request to search Plannings for query {}", query);
        return StreamSupport
            .stream(planningSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
