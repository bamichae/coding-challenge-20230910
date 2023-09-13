package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {
    /**
     * Testing functions pertaining to employee creation, updating, reports
     */

    private String employeeUrl;
    private String employeeIdUrl;

    private String numberOfReportsUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";

        numberOfReportsUrl = employeeIdUrl + "/direct-reports";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReportingEmployees() {
        Employee testEmployee1 = new Employee();
        testEmployee1.setEmployeeId("1");
        testEmployee1.setFirstName("Monte");
        testEmployee1.setLastName("Cristo");
        testEmployee1.setDepartment("Finance");
        testEmployee1.setPosition("Director");
        List<Employee> employee1Reports = new ArrayList<>();
        testEmployee1.setDirectReports(employee1Reports);

        Employee testEmployee2 = new Employee();
        testEmployee2.setEmployeeId("2");
        testEmployee2.setFirstName("Santiago");
        testEmployee2.setLastName("Alchemist");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer");
        List<Employee> employee2Reports = new ArrayList<>();
        testEmployee2.setDirectReports(employee2Reports);

        Employee testEmployee3 = new Employee();
        testEmployee3.setEmployeeId("3");
        testEmployee3.setFirstName("Paul");
        testEmployee3.setLastName("Atreides");
        testEmployee3.setDepartment("Accounting");
        testEmployee3.setPosition("Accountant I");
        List<Employee> employee3Reports = new ArrayList<>();
        testEmployee3.setDirectReports(employee3Reports);

        Employee testEmployee4 = new Employee();
        testEmployee4.setEmployeeId("4");
        testEmployee4.setFirstName("Louis");
        testEmployee4.setLastName("Zamperini");
        testEmployee4.setDepartment("Engineering");
        testEmployee4.setPosition("Manager");
        List<Employee> employee4Reports = new ArrayList<>();
        testEmployee4.setDirectReports(employee4Reports);

        // Set the reports for employees
        employee1Reports.add(testEmployee2);
        employee1Reports.add(testEmployee3);

        employee3Reports.add(testEmployee4);

        // Store the employees in the database
        employeeRepository.insert(testEmployee1);
        employeeRepository.insert(testEmployee2);
        employeeRepository.insert(testEmployee3);
        employeeRepository.insert(testEmployee4);

        // Check that employee1 has 3 total reports
        ReportingStructure reportingStructure1 = restTemplate.getForEntity(numberOfReportsUrl, ReportingStructure.class, testEmployee1.getEmployeeId()).getBody();
        assertEquals(reportingStructure1.getEmployee(), testEmployee1.getEmployeeId());
        assertEquals(reportingStructure1.getNumberOfReports(), 3);

        // Check that employee3 has 1 report
        ReportingStructure reportingStructure3 = restTemplate.getForEntity(numberOfReportsUrl, ReportingStructure.class, testEmployee3.getEmployeeId()).getBody();
        assertEquals(reportingStructure3.getEmployee(), testEmployee3.getEmployeeId());
        assertEquals(reportingStructure3.getNumberOfReports(), 1);

        // Check that employee4 has 0 reports
        ReportingStructure reportingStructure4 = restTemplate.getForEntity(numberOfReportsUrl, ReportingStructure.class, testEmployee4.getEmployeeId()).getBody();
        assertEquals(reportingStructure4.getEmployee(), testEmployee4.getEmployeeId());
        assertEquals(reportingStructure4.getNumberOfReports(), 0);

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
