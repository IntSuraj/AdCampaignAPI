package com.assignmentFirst.AdsCampaignAPI.model;

public class Campaign {

    private long orgId;
    private long accountId ;
    private long campaignId;
    private String campaignName;
    private String campaignStatus;
    private long budget;

    public Campaign() {
    }

    public Campaign(long orgId, long accId, long campaignId, String name, String status, long budget){
        this.orgId = orgId;
        this.accountId = accId;
        this.campaignId = campaignId;
        this.campaignName = name;
        this.campaignStatus = status;
    }

    public long getAccId() {
        return accountId;
    }

    public void setAccId(long accId) {
        this.accountId = accId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }


    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return campaignName;
    }

    public void setName(String name) {
        this.campaignName = name;
    }

    public String getStatus() {
        return campaignStatus;
    }

    public void setStatus(String status) {
        this.campaignStatus = status;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

}
