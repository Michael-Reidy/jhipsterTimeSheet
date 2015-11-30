package com.equiax.employeerecord.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.equiax.employeerecord.domain.LineItem;
import com.equiax.employeerecord.repository.LineItemRepository;
import com.equiax.employeerecord.web.rest.util.HeaderUtil;
import com.equiax.employeerecord.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LineItem.
 */
@RestController
@RequestMapping("/api")
public class LineItemResource {

    private final Logger log = LoggerFactory.getLogger(LineItemResource.class);

    @Inject
    private LineItemRepository lineItemRepository;

    /**
     * POST  /lineItems -> Create a new lineItem.
     */
    @RequestMapping(value = "/lineItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LineItem> createLineItem(@Valid @RequestBody LineItem lineItem) throws URISyntaxException {
        log.debug("REST request to save LineItem : {}", lineItem);
        if (lineItem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new lineItem cannot already have an ID").body(null);
        }
        LineItem result = lineItemRepository.save(lineItem);
        return ResponseEntity.created(new URI("/api/lineItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lineItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lineItems -> Updates an existing lineItem.
     */
    @RequestMapping(value = "/lineItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LineItem> updateLineItem(@Valid @RequestBody LineItem lineItem) throws URISyntaxException {
        log.debug("REST request to update LineItem : {}", lineItem);
        if (lineItem.getId() == null) {
            return createLineItem(lineItem);
        }
        LineItem result = lineItemRepository.save(lineItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lineItem", lineItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lineItems -> get all the lineItems.
     */
    @RequestMapping(value = "/lineItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LineItem>> getAllLineItems(Pageable pageable)
        throws URISyntaxException {
        Page<LineItem> page = lineItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lineItems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lineItems/:id -> get the "id" lineItem.
     */
    @RequestMapping(value = "/lineItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LineItem> getLineItem(@PathVariable Long id) {
        log.debug("REST request to get LineItem : {}", id);
        return Optional.ofNullable(lineItemRepository.findOne(id))
            .map(lineItem -> new ResponseEntity<>(
                lineItem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lineItems/:id -> delete the "id" lineItem.
     */
    @RequestMapping(value = "/lineItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLineItem(@PathVariable Long id) {
        log.debug("REST request to delete LineItem : {}", id);
        lineItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lineItem", id.toString())).build();
    }
}
