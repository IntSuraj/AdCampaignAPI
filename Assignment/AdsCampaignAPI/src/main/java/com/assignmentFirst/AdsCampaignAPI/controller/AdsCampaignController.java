package com.assignmentFirst.AdsCampaignAPI.controller;

import com.assignmentFirst.AdsCampaignAPI.model.*;
import com.assignmentFirst.AdsCampaignAPI.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdsCampaignController {

    @Autowired
    private AdService adService;

    // EndPoint to fetch all the unique organizations present in data
    @GetMapping("/organizations")
    public List<Organization> getAllOrganizations() {
        return adService.getAllOrganizations();
    }

    // Fetch all the unique accounts present in the data
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return adService.getAllAccounts();
    }

    // Fetch all the unique campaigns present in the data(includes campaignId, name, status and budget)
    @GetMapping("/campaigns")
    public List<Campaign> getAllCampaigns() {
        return adService.getAllCampaigns();
    }

    // Fetch all the data related to campaigns (includes campaignId, campaignname, Reportdate, spend and other features for campaigns )
    @GetMapping("/campaignmetrics")
    public List<campaignMetrics> getAllCampaignMetrics() {
        return adService.getAllcampaignMetrics();
    }

    // Get Sorted Campaign Summary Metrics
    @GetMapping("/campaignMetricsSorted")
    public List<CampaignSummary> getSortedCampaignMetrics(@RequestParam String sortBy, @RequestParam String order) {
        return adService.getSortedCampaignMetrics(sortBy, order);
    }

    // Add Organization - (only organization deatils - OrgId, name, country, status)
    @PostMapping("/organization")
    public String addOrganization(@RequestBody Organization organization) {
        if (adService.addOrganization(organization)) {
            return "organization added successfully";
        }
        return "organization already exists";
    }

    // Add Account - (Provide OrgID and account related features)
    @PostMapping("/organization/{orgId}/account")
    public String addAccount(@PathVariable Long orgId, @RequestBody Account account) {
        account.setOrgId(orgId);
        int isAdded = adService.addaccount(account);
        if (isAdded == 0) {
            return "account added successfully";
        } else if(isAdded == 1) {
            return "account already exists";
        }
        return "Invalid organization reference";
    }

    // Add Campaign - (provide organizationId, accountId and other related features)
    @PostMapping("orgs/{orgId}/account/{accId}/campaign")
    public String addCampaign(@PathVariable Long orgId, @PathVariable Long accId, @RequestBody Campaign campaign) {
        campaign.setAccId(orgId);
        campaign.setAccId(accId);
        int isAdded = adService.addcampaign(campaign);
        if (isAdded == 0 ) {
            return "campaign added successfully";
        } else if (isAdded == 1 ) {
            return "campaign already exists";
        }
        else if(isAdded == 2 ) {
            return "invalid account Id reference";
        }
        return "invalid organization Id reference";
    }

    // Update Organization Status
    @PutMapping("/organization/{orgId}/status")
    public String updateOrganizationStatus(@PathVariable int orgId, @RequestParam String status) {
        boolean isUpdated = adService.updateOrganizationStatus(orgId, status);
        if (isUpdated) {
            return "organization status updated successfully";
        } else {
            return "organization does not exist";
        }
    }

    /*
    // Update Account Status
    @PutMapping("org/{orgId}/account/{accountId}/status")
    public ResponseEntity<String> updateAccountStatus(@PathVariable int orgId, @PathVariable long accountId, @RequestParam String status) {
        int isUpdated = adService.updateAccountStatus(orgId, accountId, status);
        if (isUpdated == 0) {
            return new ResponseEntity<>("account status updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("invalid id", HttpStatus.NOT_FOUND);
    }

     */


    // Update Account Status
    @PutMapping("org/{orgId}/account/{accountId}/status")
    public String  updateAccountStatus(@PathVariable int orgId, @PathVariable long accountId, @RequestParam String status) {
        int isUpdated = adService.updateAccountStatus(orgId, accountId, status);
        if (isUpdated == 0) {
            return "account status updated successfully";
        }
        return "invalid reference arguments";
    }



    // Update Campaign Name and Status
    @PutMapping("/campaign/{campaignId}")
    public String updateCampaign(@PathVariable long campaignId, @RequestParam String name, @RequestParam String status) {
        boolean isUpdated = adService.updateCampaign(campaignId, name, status);
        if (isUpdated) {
            return "campaign updated successfully";
        } else {
            return "campaign does not exist";
        }
    }
}
