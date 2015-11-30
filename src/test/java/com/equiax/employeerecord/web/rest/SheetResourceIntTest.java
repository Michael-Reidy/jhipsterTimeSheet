package com.equiax.employeerecord.web.rest;

import com.equiax.employeerecord.Application;
import com.equiax.employeerecord.domain.Sheet;
import com.equiax.employeerecord.repository.SheetRepository;

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
 * Test class for the SheetResource REST controller.
 *
 * @see SheetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SheetResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_PROJECTNAME = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PROJECTNAME = "BBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_WEEKENDING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_WEEKENDING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_WEEKENDING_STR = dateTimeFormatter.format(DEFAULT_WEEKENDING);

    @Inject
    private SheetRepository sheetRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSheetMockMvc;

    private Sheet sheet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SheetResource sheetResource = new SheetResource();
        ReflectionTestUtils.setField(sheetResource, "sheetRepository", sheetRepository);
        this.restSheetMockMvc = MockMvcBuilders.standaloneSetup(sheetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sheet = new Sheet();
        sheet.setProjectname(DEFAULT_PROJECTNAME);
        sheet.setWeekending(DEFAULT_WEEKENDING);
    }

    @Test
    @Transactional
    public void createSheet() throws Exception {
        int databaseSizeBeforeCreate = sheetRepository.findAll().size();

        // Create the Sheet

        restSheetMockMvc.perform(post("/api/sheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sheet)))
                .andExpect(status().isCreated());

        // Validate the Sheet in the database
        List<Sheet> sheets = sheetRepository.findAll();
        assertThat(sheets).hasSize(databaseSizeBeforeCreate + 1);
        Sheet testSheet = sheets.get(sheets.size() - 1);
        assertThat(testSheet.getProjectname()).isEqualTo(DEFAULT_PROJECTNAME);
        assertThat(testSheet.getWeekending()).isEqualTo(DEFAULT_WEEKENDING);
    }

    @Test
    @Transactional
    public void checkWeekendingIsRequired() throws Exception {
        int databaseSizeBeforeTest = sheetRepository.findAll().size();
        // set the field null
        sheet.setWeekending(null);

        // Create the Sheet, which fails.

        restSheetMockMvc.perform(post("/api/sheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sheet)))
                .andExpect(status().isBadRequest());

        List<Sheet> sheets = sheetRepository.findAll();
        assertThat(sheets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSheets() throws Exception {
        // Initialize the database
        sheetRepository.saveAndFlush(sheet);

        // Get all the sheets
        restSheetMockMvc.perform(get("/api/sheets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sheet.getId().intValue())))
                .andExpect(jsonPath("$.[*].projectname").value(hasItem(DEFAULT_PROJECTNAME.toString())))
                .andExpect(jsonPath("$.[*].weekending").value(hasItem(DEFAULT_WEEKENDING_STR)));
    }

    @Test
    @Transactional
    public void getSheet() throws Exception {
        // Initialize the database
        sheetRepository.saveAndFlush(sheet);

        // Get the sheet
        restSheetMockMvc.perform(get("/api/sheets/{id}", sheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sheet.getId().intValue()))
            .andExpect(jsonPath("$.projectname").value(DEFAULT_PROJECTNAME.toString()))
            .andExpect(jsonPath("$.weekending").value(DEFAULT_WEEKENDING_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSheet() throws Exception {
        // Get the sheet
        restSheetMockMvc.perform(get("/api/sheets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSheet() throws Exception {
        // Initialize the database
        sheetRepository.saveAndFlush(sheet);

		int databaseSizeBeforeUpdate = sheetRepository.findAll().size();

        // Update the sheet
        sheet.setProjectname(UPDATED_PROJECTNAME);
        sheet.setWeekending(UPDATED_WEEKENDING);

        restSheetMockMvc.perform(put("/api/sheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sheet)))
                .andExpect(status().isOk());

        // Validate the Sheet in the database
        List<Sheet> sheets = sheetRepository.findAll();
        assertThat(sheets).hasSize(databaseSizeBeforeUpdate);
        Sheet testSheet = sheets.get(sheets.size() - 1);
        assertThat(testSheet.getProjectname()).isEqualTo(UPDATED_PROJECTNAME);
        assertThat(testSheet.getWeekending()).isEqualTo(UPDATED_WEEKENDING);
    }

    @Test
    @Transactional
    public void deleteSheet() throws Exception {
        // Initialize the database
        sheetRepository.saveAndFlush(sheet);

		int databaseSizeBeforeDelete = sheetRepository.findAll().size();

        // Get the sheet
        restSheetMockMvc.perform(delete("/api/sheets/{id}", sheet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sheet> sheets = sheetRepository.findAll();
        assertThat(sheets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
