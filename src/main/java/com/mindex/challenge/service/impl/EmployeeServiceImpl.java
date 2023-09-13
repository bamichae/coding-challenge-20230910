package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReports(String id) {
        LOG.debug("Retrieving direct reports for employee [{}]", id);

        // Get the data for the employee in question
        Employee employee = read(id);
        ReportingStructure reportingStructure = new ReportingStructure();

        // Search for the employee's reports
        int reports = countReports(employee.getEmployeeId(), 0);
        reportingStructure.setEmployee(id);
        reportingStructure.setNumberOfReports(reports);

        LOG.debug("Report Count for employee [{}]: [{}]", id, reports);

        return reportingStructure;
    }

    private int countReports(String id, int reportCount) {
        Employee parentEmployee = employeeRepository.findByEmployeeId(id);

        if (parentEmployee.getDirectReports() == null) {
            // Return 0 if an employee doesn't have any reports
            LOG.debug("Employee [{}] without any direct reports reached (leaf node)", id);
            return 0;
        } else {
            // Else count the direct reports (children) and search through their reports
            List<Employee> childEmployees = parentEmployee.getDirectReports();
            reportCount += childEmployees.size();

            LOG.debug("Employee [{}] reached with [{}] direct reports", id, childEmployees.size());

            // Recursively search the parent employee's direct reports
            for (Employee childEmployee : childEmployees) {
                LOG.debug("Searching employee:[{}] --> Direct report: [{}]", id, childEmployee.getEmployeeId());
                reportCount += countReports(childEmployee.getEmployeeId(), 0);
            }

            return reportCount;
        }
    }
}
