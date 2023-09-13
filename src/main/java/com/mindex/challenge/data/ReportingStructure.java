package com.mindex.challenge.data;

public class ReportingStructure {
    /**
     * For holding info on employees who report directly or indirectly to another employee.
     */
    private String employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }


    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
