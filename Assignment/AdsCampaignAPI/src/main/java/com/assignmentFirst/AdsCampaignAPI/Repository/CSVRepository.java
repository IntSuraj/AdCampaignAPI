package com.assignmentFirst.AdsCampaignAPI.Repository;
import com.assignmentFirst.AdsCampaignAPI.model.*;
import com.assignmentFirst.AdsCampaignAPI.util.CSVUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CSVRepository {
    private static final String FILE_PATH = "src/main/resources/dataset.csv";


    public List<Organization> getAllOrganizations() {
        return CSVUtil.readOrganizations(FILE_PATH);
    }

    public List<Account> getAllAccounts() {
        return CSVUtil.readAccounts(FILE_PATH);
    }

    public List<Campaign> getAllCampaigns() {
        return CSVUtil.readCampaigns(FILE_PATH);
    }

    public List<campaignMetrics> getAllCampaignMetrics() {
        return CSVUtil.readCampaignMetrics(FILE_PATH);
    }

    public void addOrganization(Organization organization) {
        CSVUtil.addOrganizationwriting(organization);
    }

    public void addAccount(Account account) {
        CSVUtil.addAccountWriting(account);
    }

    public void addCampaign(Campaign campaign){
        CSVUtil.addCampaignWriting(campaign);
    }

    public List<CampaignSummary> aggregateMetricsSummary() {
        List<campaignMetrics> metricsList = CSVUtil.readCampaignMetrics(CSVUtil.FILE_PATH);
        return CSVUtil.aggregateMetrics(metricsList);
    }

    public void updateOrganizationStatus(int orgId, String newStatus) {
        CSVUtil.updateOrganizationStatus(orgId, newStatus);
    }

    public void updateAccountStatus(long accountId, String newStatus) {
        CSVUtil.updateAccountStatus(accountId, newStatus);
    }

    public void updateCampaign(long campaignId, String newName, String newStatus) {
        CSVUtil.updateCampaign(campaignId, newName, newStatus);
    }

}
