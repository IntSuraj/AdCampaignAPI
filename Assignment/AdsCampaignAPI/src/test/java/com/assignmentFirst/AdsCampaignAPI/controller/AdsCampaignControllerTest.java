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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
                .andExpect(status().isOk())
                .andExpect(content().string("organization added successfully"));

        Mockito.when(adService.addOrganization(any(Organization.class))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization")
                        .contentType("application/json")
                        .content("{\"orgId\": 1, \"orgName\": \"Org1\", \"orgCountry\": \"Country1\", \"orgStatus\": \"Active\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("organization already exists"));
    }

    @Test
    void testUpdateOrganizationStatus() throws Exception {
        Mockito.when(adService.updateOrganizationStatus(anyInt(), any(String.class))).thenReturn(true);

        mockMvc.perform(put("/api/organization/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("organization status updated successfully"));

        Mockito.when(adService.updateOrganizationStatus(anyInt(), any(String.class))).thenReturn(false);

        mockMvc.perform(put("/api/organization/2/status")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("organization does not exist"));
    }

    @Test
    void testAddAccount() throws Exception {
        Mockito.when(adService.addaccount(any(Account.class))).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization/1/account")
                        .contentType("application/json")
                        .content("{\"orgId\": 1, \"accountId\": 1, \"accountName\": \"Acc1\", \"accountStatus\": \"Active\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("account added successfully"));

        Mockito.when(adService.addaccount(any(Account.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/organization/1/account")
                        .contentType("application/json")
                        .content("{\"orgId\": 1, \"accountId\": 1, \"accountName\": \"Acc1\", \"accountStatus\": \"Active\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("account already exists"));
    }

    @Test
    void testAddCampaign_Success() throws Exception {
        Mockito.when(adService.addcampaign(any(Campaign.class))).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("campaign added successfully"));
    }

    @Test
    void testAddCampaign_CampaignExists() throws Exception {
        Mockito.when(adService.addcampaign(any(Campaign.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("campaign already exists"));
    }

    @Test
    void testAddCampaign_InvalidAccount() throws Exception {
        Mockito.when(adService.addcampaign(any(Campaign.class))).thenReturn(2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("invalid account Id reference"));
    }

    @Test
    void testAddCampaign_InvalidOrganization() throws Exception {
        Mockito.when(adService.addcampaign(any(Campaign.class))).thenReturn(3);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orgs/1/account/1/campaign")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"campaignId\": 1, \"campaignName\": \"Camp1\", \"campaignStatus\": \"Active\", \"budget\": 100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("invalid organization Id reference"));
    }

    @Test
    void testUpdateAccountStatus_Success() throws Exception {
        Mockito.when(adService.updateAccountStatus(anyInt(), anyLong(), anyString())).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/org/1/account/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("account status updated successfully"));
    }

    @Test
    void testUpdateAccountStatus_InvalidReferenceArguments() throws Exception {
        Mockito.when(adService.updateAccountStatus(anyInt(), anyLong(), anyString())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/org/1/account/1/status")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("invalid reference arguments"));
    }

    @Test
    void testUpdateCampaign() throws Exception {
        Mockito.when(adService.updateCampaign(anyLong(), any(String.class), any(String.class))).thenReturn(true);

        mockMvc.perform(put("/api/campaign/1")
                        .param("name", "NewName")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("campaign updated successfully"));

        Mockito.when(adService.updateCampaign(anyLong(), any(String.class), any(String.class))).thenReturn(false);

        mockMvc.perform(put("/api/campaign/2")
                        .param("name", "NewName")
                        .param("status", "Inactive"))
                .andExpect(status().isOk())
                .andExpect(content().string("campaign does not exist"));
    }
}
