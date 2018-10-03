package com.axeane.controller;

import com.axeane.Exception.errors.ExceptionTranslator;
import com.axeane.MapstructCrudEmployeeApplication;
import com.axeane.dto.EmployeeDTO;
import com.axeane.entity.Employee;
import com.axeane.mapper.EmployeeMapper;
import com.axeane.repository.EmployeeRepository;
import com.axeane.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the EmployeeControllerTest REST controller.
 *
 * @see EmployeeControllerTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapstructCrudEmployeeApplication.class)
public class EmployeeControllerTest {
    private static final String DEFAULT_NAME = "soltani";
    private static final String UPDATE_NAME = "soltani1";

    private static final String DEFAULT_FIRST_NAME = "mustapha";
    private static final String UPDATE_FIRST_NAME = "mustapha1";

    private static final Integer DEFAULT_AGE = 15;
    private static final Integer UPDATE_AGE = 40;

    private static final String DEFAULT_SEXE = "M";
    private static final String UPDATE_SEXE = "F";


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        EmployeeController employeeController = new EmployeeController(employeeService, employeeRepository);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public EmployeeDTO createEntity(EntityManager em) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeName(DEFAULT_NAME);
        employee.setEmployeeFirstName(DEFAULT_FIRST_NAME);
        employee.setEmployeeSexe(DEFAULT_SEXE);
        employee.setEmployeeAge(DEFAULT_AGE);
        return employee;
    }

    @Before
    public void initTest() {
        employeeDTO = createEntity(em);
    }

    @Test
    public void addEmployee() throws Exception {

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        restEmployeeMockMvc.perform(post("/api/employee")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isCreated());
        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testEmployee.getSexe()).isEqualTo(DEFAULT_SEXE);

    }

    @Test
    public void getAllSalaries() throws Exception {
        // Initialize the database
        Employee employeeSaved = employeeService.save(employeeDTO);
        // Get all the employeeList
        restEmployeeMockMvc.perform(get("/api/employee?sort=id,desc", employeeSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employeeSaved.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE))) ;
    }

    @Test
    public void getEmployee() throws Exception {
        // Initialize the database
        Employee employeeSaved = employeeService.save(employeeDTO);
        // Get the Employee
        restEmployeeMockMvc.perform(get("/api/employee/{id}", employeeSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(employeeSaved.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
                .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
                .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
        ;
    }
    @Test
    public void updateSalarie() throws Exception {
        // Initialize the database
        employeeService.save(employeeDTO);
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the Employee
        Optional<Employee> updatedEmployee = employeeRepository.findById(employee.getId());
        if (updatedEmployee.isPresent()) {
            updatedEmployee.get().setName(UPDATE_NAME);
            updatedEmployee.get().setFirstName(UPDATE_FIRST_NAME);
            updatedEmployee.get().setAge(UPDATE_AGE);
            updatedEmployee.get().setSexe(UPDATE_SEXE);
        }

        restEmployeeMockMvc.perform(put("/api/employee")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmployee)))
                .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeerList = employeeRepository.findAll();
        assertThat(employeerList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeerList.get(employeerList.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(UPDATE_NAME);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATE_FIRST_NAME);
        assertThat(testEmployee.getSexe()).isEqualTo(UPDATE_SEXE);
        assertThat(testEmployee.getAge()).isEqualTo(UPDATE_AGE);
    }

    @Test
    public void deleteEmployee() throws Exception {

        // Initialize the database
        employeeService.save(employeeDTO);
        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Get the Produit
        restEmployeeMockMvc.perform(delete("/api/employee/{id}", employee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employee> employeestList = employeeRepository.findAll();
        assertThat(employeestList).hasSize(databaseSizeBeforeDelete - 1);
    }

}