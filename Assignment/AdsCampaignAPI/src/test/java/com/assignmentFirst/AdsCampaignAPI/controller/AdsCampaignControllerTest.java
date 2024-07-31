package com.assignmentFirst.AdsCampaignAPI.controller;

import com.assignmentFirst.AdsCampaignAPI.model.Account;
import com.assignmentFirst.AdsCampaignAPI.model.Campaign;
import com.assignmentFirst.AdsCampaignAPI.model.Organization;
import com.assignmentFirst.AdsCampaignAPI.service.AdService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdsCampaignController.class)
class AdsCampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdService adService;


    @Test
    void testAddOrganization() throws Exception {
        Mockito.when(adService.addOrganization(any(Organization.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization")
                        .contentType("application/json")
                        .content("{\"orgId\": 1, \"orgName\": \"Org1\", \"orgCountry\": \"Country1\", \"orgStatus\": \"Active\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("organization added successfully"));

        Mockito.when(adService.addOrganization(any(Organization.class))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization")
                        .contentType("application/json")
                        .content("{\"orgId\": 1, \"orgName\": \"Org1\", \"orgCountry\": \"Country1\", \"orgStatus\": \"Active\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("organization already exists"));
    }

    @Test
    void testUpdateOrganizationStatus() throws Exception {
        Mockito.when(adService.updateOrganizationStatus(anyInt(), anyString())).thenReturn(true);

        mockMvc.perform(put("/api/organization/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("organization status updated successfully"));

        Mockito.when(adService.updateOrganizationStatus(anyInt(), anyString())).thenReturn(false);

        mockMvc.perform(put("/api/organization/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid organization Id reference"));
    }

    @Test
    void testAddAccount_AccountAddedSuccessfully() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("orgExist", true);
        isAdded.put("acExist", false);

        Mockito.when(adService.addaccount(Mockito.any(Account.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization/1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountName\": \"Acc1\", \"accountStatus\": \"Active\"}"))
                .andExpect(status().isCreated()) // Expecting HTTP 201 Created
                .andExpect(content().string("Account added successfully"));
    }

    @Test
    void testAddAccount_AccountAlreadyExists() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("orgExist", true);
        isAdded.put("acExist", true);

        Mockito.when(adService.addaccount(Mockito.any(Account.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization/1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountName\": \"Acc1\", \"accountStatus\": \"Active\"}"))
                .andExpect(status().isConflict()) // Expecting HTTP 409 Conflict
                .andExpect(content().string("Account already exists"));
    }

    @Test
    void testAddAccount_InvalidOrganizationReference() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("orgExist", false);
        isAdded.put("acExist", false);

        Mockito.when(adService.addaccount(Mockito.any(Account.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization/1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountName\": \"Acc1\", \"accountStatus\": \"Active\"}"))
                .andExpect(status().isBadRequest()) // Expecting HTTP 400 Bad Request
                .andExpect(content().string("Invalid organization reference"));
    }
    @Test
    void testAddCampaign_Success() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("org", true);
        isAdded.put("acc", true);
        isAdded.put("camp", false);

        Mockito.when(adService.addcampaign(anyLong(), anyLong(), any(Campaign.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("campaign added successfully"));
    }

    @Test
    void testAddCampaign_CampaignExists() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("org", true);
        isAdded.put("acc", true);
        isAdded.put("camp", true);

        Mockito.when(adService.addcampaign(anyLong(), anyLong(), any(Campaign.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("campaign already exists"));
    }

    @Test
    void testAddCampaign_InvalidAccount() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("org", true);
        isAdded.put("acc", false);
        isAdded.put("camp", false);

        Mockito.when(adService.addcampaign(anyLong(), anyLong(), any(Campaign.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid account Id reference"));
    }

    @Test
    void testAddCampaign_InvalidOrganization() throws Exception {
        HashMap<String, Boolean> isAdded = new HashMap<>();
        isAdded.put("org", false);
        isAdded.put("acc", false);
        isAdded.put("camp", false);

        Mockito.when(adService.addcampaign(anyLong(), anyLong(), any(Campaign.class))).thenReturn(isAdded);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid organization Id reference"));
    }

    @Test
    void testUpdateAccountStatus_Success() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("orgExist", true);
        updateResult.put("acExist", true);

        Mockito.when(adService.updateAccountStatus(anyInt(), anyLong(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/org/1/account/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("account status updated successfully"));
    }

    @Test
    void testUpdateAccountStatus_InvalidAccount() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("orgExist", true);
        updateResult.put("acExist", false);

        Mockito.when(adService.updateAccountStatus(anyInt(), anyLong(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/org/1/account/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid account id"));
    }

    @Test
    void testUpdateAccountStatus_InvalidOrganization() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("orgExist", false);
        updateResult.put("acExist", false);

        Mockito.when(adService.updateAccountStatus(anyInt(), anyLong(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/org/1/account/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid organization id"));
    }

    @Test
    void testUpdateCampaign_Success() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("org", true);
        updateResult.put("acc", true);
        updateResult.put("camp", true);

        Mockito.when(adService.updateCampaign(anyInt(), anyLong(), anyLong(), anyString(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orgs/1/account/1/campaign/1")
                        .param("name", "NewName")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("campaign updated successfully"));
    }

    @Test
    void testUpdateCampaign_CampaignDoesNotExist() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("org", true);
        updateResult.put("acc", true);
        updateResult.put("camp", false);

        Mockito.when(adService.updateCampaign(anyInt(), anyLong(), anyLong(), anyString(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orgs/1/account/1/campaign/1")
                        .param("name", "NewName")
                        .param("status", "Inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("campaign doesn't exist"));
    }

    @Test
    void testUpdateCampaign_InvalidAccount() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("org", true);
        updateResult.put("acc", false);
        updateResult.put("camp", false);

        Mockito.when(adService.updateCampaign(anyInt(), anyLong(), anyLong(), anyString(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orgs/1/account/1/campaign/1")
                        .param("name", "NewName")
                        .param("status", "Inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid account Id reference"));
    }

    @Test
    void testUpdateCampaign_InvalidOrganization() throws Exception {
        HashMap<String, Boolean> updateResult = new HashMap<>();
        updateResult.put("org", false);
        updateResult.put("acc", false);
        updateResult.put("camp", false);

        Mockito.when(adService.updateCampaign(anyInt(), anyLong(), anyLong(), anyString(), anyString())).thenReturn(updateResult);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/orgs/1/account/1/campaign/1")
                        .param("name", "NewName")
                        .param("status", "Inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid organization Id reference"));
    }
}
