package com.assignmentFirst.AdsCampaignAPI.service;

import com.assignmentFirst.AdsCampaignAPI.model.*;
import com.assignmentFirst.AdsCampaignAPI.Repository.CSVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdService {

    @Autowired
    private CSVRepository repository;


    // Method to fetch all organizations list
    public List<Organization> getAllOrganizations() {
        return repository.getAllOrganizations();
    }


    // fetching all the accounts associated to particular organization
    public List<Account> getAllAccountsByOrgId(Long orgId) {
        return repository.getAllAccounts().stream()
                .filter(Account -> Account.getOrgId() == orgId)
                .collect(Collectors.toList());
    }

    // fetching all campigns associated to particular account
    public List<Campaign> getAllCampaignsByAccountId(Long accountId) {
        return repository.getAllCampaigns().stream()
                .filter(campaign -> campaign.getAccId() == accountId)
                .collect(Collectors.toList());
    }

    // Method to add organization and returns the add status
    public boolean addOrganization(Organization organization) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean exists = organizations.stream().anyMatch(org -> org.getOrgId() == organization.getOrgId());

        if (exists) {
            return false;
        } else {
            repository.addOrganization(organization);
            return true;
        }
    }



    // Method to add account
    public int addaccount(Account account) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == account.getOrgId());

        if (orgExists) {
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId(account.getOrgId());
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == account.getAccId());
            if (!accountExists) {
                repository.addAccount(account);
                return 0;
            } else {
                // Account already exists
                return 1;
            }
        }
        // Invalid organization reference
        return 2;
    }


    public int addcampaign(Campaign camp) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == camp.getOrgId());


        if (orgExists) {
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId(camp.getOrgId());
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == camp.getAccId());
            if (accountExists) {
                List<Campaign> camps = getAllCampaignsByAccountId(camp.getAccId());
                boolean campExists = camps.stream().anyMatch(campp -> campp.getAccId() == camp.getCampaignId());

                if (!campExists) {
                    // add if campaign doesn't already exists
                    repository.addCampaign(camp);
                    return 0;
                } else {
                    // campaign already exists
                    return 1;
                }
            } else {
                // invalid account reference
                return 2;
            }
        }
        // invalid organization reference
        return 3;
    }

    public boolean updateOrganizationStatus(int orgId, String newStatus) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean exists = organizations.stream().anyMatch(org -> org.getOrgId() == orgId);

        if (exists) {
            repository.updateOrganizationStatus(orgId, newStatus);
            return true;
        } else {
            return false;
        }
    }

    public int updateAccountStatus(int orgId, long accountId, String newStatus) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == orgId);

        if (orgExists) {
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId((long) orgId);
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == accountId);
            if (accountExists) {
                repository.updateAccountStatus(accountId, newStatus);
                return 0;
            }
        }
        // Invalid organization reference
        return 1;
    }


    // Update campain name and status
    public boolean updateCampaign(long campaignId, String newName, String newStatus) {
        List<Campaign> campaigns = repository.getAllCampaigns();
        boolean exists = campaigns.stream().anyMatch(c -> c.getCampaignId() == campaignId);

        if (exists) {
            repository.updateCampaign(campaignId, newName, newStatus);
            return true;
        } else {
            return false;
        }
    }

    public List<CampaignSummary> getSortedCampaignMetrics(String sortBy, String order) {
        List<CampaignSummary> metrics = repository.aggregateMetricsSummary();
        Comparator<CampaignSummary> comparator;

        switch (sortBy.toLowerCase()) {
            case "spend":
                comparator = Comparator.comparing(CampaignSummary::getSpend);
                break;
            case "revenue":
                comparator = Comparator.comparing(CampaignSummary::getRevenue);
                break;
            case "impressions":
                comparator = Comparator.comparing(CampaignSummary::getImpressions);
                break;
            case "clicks":
                comparator = Comparator.comparing(CampaignSummary::getClicks);
                break;
            default:
                comparator = Comparator.comparing(CampaignSummary::getBudget);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return metrics.stream().sorted(comparator).collect(Collectors.toList());
    }

    public List<Campaign> getAllCampaigns() {
        return repository.getAllCampaigns();
    }

    public List<campaignMetrics> getAllcampaignMetrics() {
        return repository.getAllCampaignMetrics();
    }

    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }
}
