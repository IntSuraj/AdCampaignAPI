package com.assignmentFirst.AdsCampaignAPI.model;


public class CampaignSummary {
    private Long campaignId;
    private String campaignName;
    private Double budget;
    private Double spend;
    private Double revenue;
    private Integer impressions;
    private Integer clicks;

    public CampaignSummary(Long campaignId, String campaignName) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.budget = 0.0;
        this.spend = 0.0;
        this.revenue = 0.0;
        this.impressions = 0;
        this.clicks = 0;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getSpend() {
        return spend;
    }

    public void setSpend(Double spend) {
        this.spend = spend;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        return String.format("CampaignSummary{campaignId=%d, campaignName='%s', budget=%.2f, spend=%.2f, revenue=%.2f, impressions=%d, clicks=%d}",
                campaignId, campaignName, budget, spend, revenue, impressions, clicks);
    }
}
