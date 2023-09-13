package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    /**
     * Testing employee compensation functions
     */

    private String compensationUrl;
    private String compensationIdUrl;

    private String employeeUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testCreateReadUpdate() {
        // Create Employee or the system will not recognize inserting a Compensation for them.
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Monte");
        testEmployee.setLastName("Cristo");
        testEmployee.setDepartment("Finance");
        testEmployee.setPosition("Director");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        // Create a compensation for the above created employee
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(createdEmployee.getEmployeeId());
        testCompensation.setSalary(100_000_000.0);
        testCompensation.setEffectiveDate("10/4/2023");

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation.getEmployee());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee()).getBody();
        assertEquals(createdCompensation.getEmployee(), readCompensation.getEmployee());
        assertCompensationEquivalence(createdCompensation, readCompensation);

    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getSalary(), actual.getSalary(), 1e-10);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
