package com.equiax.employeerecord.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.equiax.employeerecord.domain.Sheet;
import com.equiax.employeerecord.repository.SheetRepository;
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
 * REST controller for managing Sheet.
 */
@RestController
@RequestMapping("/api")
public class SheetResource {

    private final Logger log = LoggerFactory.getLogger(SheetResource.class);

    @Inject
    private SheetRepository sheetRepository;

    /**
     * POST  /sheets -> Create a new sheet.
     */
    @RequestMapping(value = "/sheets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sheet> createSheet(@Valid @RequestBody Sheet sheet) throws URISyntaxException {
        log.debug("REST request to save Sheet : {}", sheet);
        if (sheet.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sheet cannot already have an ID").body(null);
        }
        Sheet result = sheetRepository.save(sheet);
        return ResponseEntity.created(new URI("/api/sheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sheet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sheets -> Updates an existing sheet.
     */
    @RequestMapping(value = "/sheets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sheet> updateSheet(@Valid @RequestBody Sheet sheet) throws URISyntaxException {
        log.debug("REST request to update Sheet : {}", sheet);
        if (sheet.getId() == null) {
            return createSheet(sheet);
        }
        Sheet result = sheetRepository.save(sheet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sheet", sheet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sheets -> get all the sheets.
     */
    @RequestMapping(value = "/sheets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sheet>> getAllSheets(Pageable pageable)
        throws URISyntaxException {
        Page<Sheet> page = sheetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sheets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sheets/:id -> get the "id" sheet.
     */
    @RequestMapping(value = "/sheets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sheet> getSheet(@PathVariable Long id) {
        log.debug("REST request to get Sheet : {}", id);
        return Optional.ofNullable(sheetRepository.findOne(id))
            .map(sheet -> new ResponseEntity<>(
                sheet,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sheets/:id -> delete the "id" sheet.
     */
    @RequestMapping(value = "/sheets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSheet(@PathVariable Long id) {
        log.debug("REST request to delete Sheet : {}", id);
        sheetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sheet", id.toString())).build();
    }
}
