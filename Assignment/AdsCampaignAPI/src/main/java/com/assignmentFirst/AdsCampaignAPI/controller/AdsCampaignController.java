package com.assignmentFirst.AdsCampaignAPI.controller;
import com.assignmentFirst.AdsCampaignAPI.model.*;
import com.assignmentFirst.AdsCampaignAPI.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.http.HttpResponse;
import java.util.HashMap;
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
    public ResponseEntity<String> addOrganization(@RequestBody Organization organization) {
        if (adService.addOrganization(organization)) {
            return new ResponseEntity<>("organization added successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("organization already exists", HttpStatus.CONFLICT);
    }

    // Add Account - (Provide OrgID and account related features)
    @PostMapping("/organization/{orgId}/account")
    public ResponseEntity<String> addAccount(@PathVariable Long orgId, @RequestBody Account account) {
        account.setOrgId(orgId);
        HashMap<String, Boolean> isAdded = adService.addaccount(account);
        Boolean accountExists = isAdded.get("acExist");
        Boolean organizationExists = isAdded.get("orgExist");

        if (!accountExists && organizationExists) {
            return new ResponseEntity<>("Account added successfully", HttpStatus.CREATED);
        } else if (accountExists && organizationExists) {
            return new ResponseEntity<>("Account already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Invalid organization reference", HttpStatus.BAD_REQUEST);
    }

    // Add Campaign - (provide organizationId, accountId and other related features)
    @PostMapping("orgs/{orgId}/account/{accId}/campaign")
    public ResponseEntity<String> addCampaign(@PathVariable Long orgId, @PathVariable Long accId, @RequestBody Campaign campaign) {
        campaign.setAccId(orgId);
        campaign.setAccId(accId);
        HashMap<String, Boolean> isAdded = adService.addcampaign(campaign);

        Boolean orgexist = isAdded.get("org");
        Boolean accexist = isAdded.get("acc");
        Boolean campexist = isAdded.get("camp");

        if (orgexist && accexist && !campexist) {
            return new ResponseEntity<>("campaign added successfully", HttpStatus.OK);
        } else if (orgexist && accexist && campexist) {
            return new ResponseEntity<>("campaign already exists", HttpStatus.CONFLICT);
        } else if (orgexist && !accexist) {
            return new ResponseEntity<>("invalid account Id reference", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("invalid organization Id reference", HttpStatus.BAD_REQUEST);
    }

    // Update Organization Status
    @PutMapping("/organization/{orgId}/status")
    public ResponseEntity<String> updateOrganizationStatus(@PathVariable int orgId, @RequestParam String status) {
        boolean isUpdated = adService.updateOrganizationStatus(orgId, status);
        if (isUpdated) {
            return new ResponseEntity<>("organization status updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("invalid organization Id reference", HttpStatus.BAD_REQUEST);
    }

    // Update Account Status
    @PutMapping("org/{orgId}/account/{accountId}/status")
    public ResponseEntity<String> updateAccountStatus(@PathVariable int orgId, @PathVariable long accountId, @RequestParam String status) {
        HashMap<String, Boolean> isUpdated = adService.updateAccountStatus(orgId, accountId, status);
        if (isUpdated.get("orgExist") && isUpdated.get("acExist")) {
            return new ResponseEntity<>("account status updated successfully", HttpStatus.OK);
        } else if (isUpdated.get("orgExist") && !isUpdated.get("acExist")) {
            return new ResponseEntity<>("invalid account id", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("invalid organization id", HttpStatus.BAD_REQUEST);
    }

    // Update Campaign Name and Status
    @PutMapping("/orgs/{orgId}/account/{accountId}/campaign/{campaignId}")
    public ResponseEntity<String> updateCampaign(@PathVariable int orgId, @PathVariable long accountId, @PathVariable long campaignId, @RequestParam String name, @RequestParam String status) {
        HashMap<String, Boolean> isUpdated = adService.updateCampaign(orgId, accountId, campaignId, name, status);

        Boolean orgexist = isUpdated.get("org");
        Boolean accexist = isUpdated.get("acc");
        Boolean campexist = isUpdated.get("camp");

        if (orgexist && accexist && campexist) {
            return new ResponseEntity<>("campaign updated successfully", HttpStatus.OK);
        } else if (orgexist && accexist && !campexist) {
            return new ResponseEntity<>("campaign doesn't exist", HttpStatus.BAD_REQUEST);
        } else if (orgexist && !accexist) {
            return new ResponseEntity<>("invalid account Id reference", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("invalid organization Id reference", HttpStatus.BAD_REQUEST);
    }

}







