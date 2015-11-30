package com.crowhill.employeerecord.web.rest;

import com.crowhill.employeerecord.Application;
import com.crowhill.employeerecord.domain.LineItem;
import com.crowhill.employeerecord.repository.LineItemRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LineItemResource REST controller.
 *
 * @see LineItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LineItemResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_STR = dateTimeFormatter.format(DEFAULT_START);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_STR = dateTimeFormatter.format(DEFAULT_END);
    private static final String DEFAULT_DETAILS = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBBBBBBBBBBBB";

    @Inject
    private LineItemRepository lineItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLineItemMockMvc;

    private LineItem lineItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LineItemResource lineItemResource = new LineItemResource();
        ReflectionTestUtils.setField(lineItemResource, "lineItemRepository", lineItemRepository);
        this.restLineItemMockMvc = MockMvcBuilders.standaloneSetup(lineItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lineItem = new LineItem();
        lineItem.setStart(DEFAULT_START);
        lineItem.setEnd(DEFAULT_END);
        lineItem.setDetails(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    public void createLineItem() throws Exception {
        int databaseSizeBeforeCreate = lineItemRepository.findAll().size();

        // Create the LineItem

        restLineItemMockMvc.perform(post("/api/lineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineItem)))
                .andExpect(status().isCreated());

        // Validate the LineItem in the database
        List<LineItem> lineItems = lineItemRepository.findAll();
        assertThat(lineItems).hasSize(databaseSizeBeforeCreate + 1);
        LineItem testLineItem = lineItems.get(lineItems.size() - 1);
        assertThat(testLineItem.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testLineItem.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testLineItem.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineItemRepository.findAll().size();
        // set the field null
        lineItem.setStart(null);

        // Create the LineItem, which fails.

        restLineItemMockMvc.perform(post("/api/lineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineItem)))
                .andExpect(status().isBadRequest());

        List<LineItem> lineItems = lineItemRepository.findAll();
        assertThat(lineItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineItemRepository.findAll().size();
        // set the field null
        lineItem.setEnd(null);

        // Create the LineItem, which fails.

        restLineItemMockMvc.perform(post("/api/lineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineItem)))
                .andExpect(status().isBadRequest());

        List<LineItem> lineItems = lineItemRepository.findAll();
        assertThat(lineItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineItemRepository.findAll().size();
        // set the field null
        lineItem.setDetails(null);

        // Create the LineItem, which fails.

        restLineItemMockMvc.perform(post("/api/lineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineItem)))
                .andExpect(status().isBadRequest());

        List<LineItem> lineItems = lineItemRepository.findAll();
        assertThat(lineItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLineItems() throws Exception {
        // Initialize the database
        lineItemRepository.saveAndFlush(lineItem);

        // Get all the lineItems
        restLineItemMockMvc.perform(get("/api/lineItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lineItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START_STR)))
                .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END_STR)))
                .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }

    @Test
    @Transactional
    public void getLineItem() throws Exception {
        // Initialize the database
        lineItemRepository.saveAndFlush(lineItem);

        // Get the lineItem
        restLineItemMockMvc.perform(get("/api/lineItems/{id}", lineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lineItem.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START_STR))
            .andExpect(jsonPath("$.end").value(DEFAULT_END_STR))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLineItem() throws Exception {
        // Get the lineItem
        restLineItemMockMvc.perform(get("/api/lineItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineItem() throws Exception {
        // Initialize the database
        lineItemRepository.saveAndFlush(lineItem);

		int databaseSizeBeforeUpdate = lineItemRepository.findAll().size();

        // Update the lineItem
        lineItem.setStart(UPDATED_START);
        lineItem.setEnd(UPDATED_END);
        lineItem.setDetails(UPDATED_DETAILS);

        restLineItemMockMvc.perform(put("/api/lineItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineItem)))
                .andExpect(status().isOk());

        // Validate the LineItem in the database
        List<LineItem> lineItems = lineItemRepository.findAll();
        assertThat(lineItems).hasSize(databaseSizeBeforeUpdate);
        LineItem testLineItem = lineItems.get(lineItems.size() - 1);
        assertThat(testLineItem.getStart()).isEqualTo(UPDATED_START);
        assertThat(testLineItem.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testLineItem.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void deleteLineItem() throws Exception {
        // Initialize the database
        lineItemRepository.saveAndFlush(lineItem);

		int databaseSizeBeforeDelete = lineItemRepository.findAll().size();

        // Get the lineItem
        restLineItemMockMvc.perform(delete("/api/lineItems/{id}", lineItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LineItem> lineItems = lineItemRepository.findAll();
        assertThat(lineItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
