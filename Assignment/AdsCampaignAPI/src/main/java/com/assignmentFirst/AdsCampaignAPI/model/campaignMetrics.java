package com.assignmentFirst.AdsCampaignAPI.model;

import com.assignmentFirst.AdsCampaignAPI.util.LocalDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class campaignMetrics {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @CsvBindByName(column = "campaignId")
    private Long campaignId;

    @CsvBindByName(column = "campaignName")
    private String campaignName;

    @CsvBindByName(column = "spend")
    private Double spend;

    @CsvBindByName(column = "revenue")
    private Double revenue;

    @CsvBindByName(column = "impressions")
    private Integer impressions;

    @CsvBindByName(column = "clicks")
    private Integer clicks;
    @CsvBindByName(column = "reportDate")
    @CsvCustomBindByName(converter = LocalDateConverter.class)
    private LocalDate reportDate;


    public campaignMetrics(Long campaignId, String campaignName, Double spend, Double revenue, int impressions, int clicks) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.spend = spend;
        this.revenue = revenue;
        this.impressions = impressions;
        this.clicks = clicks;
        this.reportDate = reportDate;
    }

    public campaignMetrics() {
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
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
        return String.join(",",
                String.valueOf(campaignId),
                campaignName,
                String.valueOf(spend),
                String.valueOf(revenue),
                String.valueOf(impressions),
                String.valueOf(clicks),
                reportDate != null ? reportDate.format(DATE_FORMATTER) : "N/A"
        );
    }
}
