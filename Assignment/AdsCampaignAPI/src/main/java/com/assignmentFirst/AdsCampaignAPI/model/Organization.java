package com.assignmentFirst.AdsCampaignAPI.model;

import com.opencsv.bean.CsvBindByName;

public class Organization {

    @CsvBindByName(column = "orgId")
    private int orgId;

    @CsvBindByName(column = "orgName")
    private String orgName;

    @CsvBindByName(column = "orgCountry")
    private String orgCountry;

    @CsvBindByName(column = "orgStatus")
    private String orgStatus;

    // No-argument constructor
    public Organization() {
    }

    // Parameterized constructor
    public Organization(int orgId, String orgName, String orgCountry, String orgStatus) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgCountry = orgCountry;
        this.orgStatus = orgStatus;
    }

    // Getters and Setters
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCountry() {
        return orgCountry;
    }

    public void setOrgCountry(String orgCountry) {
        this.orgCountry = orgCountry;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }

}

