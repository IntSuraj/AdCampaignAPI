package com.assignmentFirst.AdsCampaignAPI.service;

import com.assignmentFirst.AdsCampaignAPI.model.*;
import com.assignmentFirst.AdsCampaignAPI.Repository.CSVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
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
    public HashMap<String, Boolean> addaccount(Account account) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == account.getOrgId());

        HashMap<String, Boolean> hm = new HashMap<>();
        hm.put("orgExist", false);
        hm.put("acExist", false);
        if (orgExists) {
            hm.put("orgExist", true);
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId(account.getOrgId());
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == account.getAccId());

            if (!accountExists) {
                repository.addAccount(account);
                return hm;
            } else {
                // Account already exists
                hm.put("acExist", true);
                return hm;
            }
        }
        // Invalid organization reference
        return hm;
    }

    // method to add campaign
    public HashMap<String, Boolean> addcampaign(long orgId, long accId, Campaign camp) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == orgId);

        HashMap<String, Boolean> exist = new HashMap<>();
        exist.put("org", false);
        exist.put("acc", false);
        exist.put("camp", false);

        if (orgExists) {
            exist.put("org", true);
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId(orgId);
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == accId);
            if (accountExists) {
                exist.put("acc", true);
                List<Campaign> camps = getAllCampaignsByAccountId(accId);
                boolean campExists = camps.stream().anyMatch(campp -> campp.getCampaignId() == camp.getCampaignId());

                if (campExists) {
                    // campaign already exists
                    exist.put("camp", true);
                } else {
                    // add campaign if it doesn't already exist
                    repository.addCampaign(camp);
                }
            }
        }
        return exist;
    }


    //method for updating organization status value
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

    // Method to update account status
    public HashMap<String, Boolean> updateAccountStatus(int orgId, long accountId, String newStatus) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == orgId);
        HashMap<String, Boolean> hm = new HashMap<>();
        hm.put("orgExist", false);
        hm.put("acExist", false);

        if (orgExists) {
            hm.put("orgExist", true);
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId((long) orgId);
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == accountId);
            if (accountExists) {
                hm.put("acExist", true);
                repository.updateAccountStatus(accountId, newStatus);
                return hm;
            }
        }
        // Invalid organization reference
        return hm;
    }


    public HashMap<String, Boolean> updateCampaign(int orgId, long accountId, long campaignId, String newName, String newStatus) {
        List<Organization> organizations = repository.getAllOrganizations();
        boolean orgExists = organizations.stream().anyMatch(org -> org.getOrgId() == orgId);

        HashMap<String, Boolean> exist = new HashMap<>();
        exist.put("org", false);
        exist.put("acc", false);
        exist.put("camp", false);

        if (orgExists) {
            exist.put("org", true);
            // checks if the account already exists in the provided organization reference
            List<Account> accounts = getAllAccountsByOrgId((long) orgId);
            boolean accountExists = accounts.stream().anyMatch(acc -> acc.getAccId() == accountId);
            if (accountExists) {
                exist.put("acc", true);
                List<Campaign> camps = getAllCampaignsByAccountId(accountId);
                boolean campExists = camps.stream().anyMatch(campp -> campp.getCampaignId() == campaignId);

                if (campExists) {
                    // Update the campaign if it exists
                    exist.put("camp", true);
                    repository.updateCampaign(campaignId, newName, newStatus);
                    return exist;
                } else {
                    // Campaign does not exist
                    return exist;
                }
            } else {
                // Invalid account reference
                return exist;
            }
        }
        // Invalid organization reference
        return exist;
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
