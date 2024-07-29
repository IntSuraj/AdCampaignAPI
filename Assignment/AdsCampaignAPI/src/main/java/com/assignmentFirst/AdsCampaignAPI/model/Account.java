package com.assignmentFirst.AdsCampaignAPI.model;

public class Account {
    private long orgId;
    private long accountId;
    private String accountName;
    private String accountStatus;


    public Account(long orgId, long accId, String name, String status){
        this.orgId = orgId;
        this.accountId = accId;
        this.accountName = name;
        this.accountStatus = status;
    }

    public Account() {
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getAccId() {
        return accountId;
    }

    public void setAccId(long accId) {
        this.accountId = accId;
    }

    public String getName() {
        return accountName;
    }

    public void setName(String name) {
        this.accountName = name;
    }

    public String getStatus() {
        return accountStatus;
    }

    public void setStatus(String status) {
        this.accountStatus = status;
    }
}
