package com.assignmentFirst.AdsCampaignAPI.service;

import com.assignmentFirst.AdsCampaignAPI.Repository.CSVRepository;
import com.assignmentFirst.AdsCampaignAPI.model.Account;
import com.assignmentFirst.AdsCampaignAPI.model.Campaign;
import com.assignmentFirst.AdsCampaignAPI.model.CampaignSummary;
import com.assignmentFirst.AdsCampaignAPI.model.Organization;
import com.assignmentFirst.AdsCampaignAPI.model.campaignMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdServiceTest {

    @InjectMocks
    private AdService adService;

    @Mock
    private CSVRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrganizations() {
        List<Organization> organizations = Arrays.asList(new Organization(1, "Org1", "Country1", "Active"));
        when(repository.getAllOrganizations()).thenReturn(organizations);

        List<Organization> result = adService.getAllOrganizations();
        assertEquals(1, result.size());
        assertEquals("Org1", result.get(0).getOrgName());
    }

    @Test
    void testGetAllAccountsByOrgId() {
        List<Account> accounts = Arrays.asList(new Account(1, 1, "Acc1", "Active"));
        when(repository.getAllAccounts()).thenReturn(accounts);

        List<Account> result = adService.getAllAccountsByOrgId(1L);
        assertEquals(1, result.size());
        assertEquals("Acc1", result.get(0).getName());
    }

    @Test
    void testGetAllCampaignsByAccountId() {
        List<Campaign> campaigns = Arrays.asList(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        when(repository.getAllCampaigns()).thenReturn(campaigns);

        List<Campaign> result = adService.getAllCampaignsByAccountId(1L);
        assertEquals(1, result.size());
        assertEquals("Camp1", result.get(0).getName());
    }

    @Test
    void testAddOrganization() {
        List<Organization> organizations = Collections.emptyList();
        when(repository.getAllOrganizations()).thenReturn(organizations);

        boolean result = adService.addOrganization(new Organization(1, "Org1", "Country1", "Active"));
        assertTrue(result);
    }



    @Test
    void testAddAccount_ValidOrganization_AccountDoesNotExist() {
        List<Organization> organizations = List.of(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = Collections.emptyList();

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(repository.getAllAccounts()).thenReturn(accounts);

        int result = adService.addaccount(new Account(1, 1, "Acc1", "Active"));
        assertEquals(0, result);

        verify(repository, times(1)).addAccount(any(Account.class));
    }

    private CSVRepository verify(CSVRepository repository, VerificationMode times) {
        return repository;
    }

    @Test
    void testAddAccount_ValidOrganization_AccountExists() {
        List<Organization> organizations = List.of(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = List.of(new Account(1, 1, "Acc1", "Active"));

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(repository.getAllAccounts()).thenReturn(accounts);

        int result = adService.addaccount(new Account(1, 1, "Acc1", "Active"));
        assertEquals(1, result);

        verify(repository, never()).addAccount(any(Account.class));
    }

    @Test
    void testAddAccount_InvalidOrganization() {
        List<Organization> organizations = Collections.emptyList();

        when(repository.getAllOrganizations()).thenReturn(organizations);

        int result = adService.addaccount(new Account(1, 1, "Acc1", "Active"));
        assertEquals(2, result);

        verify(repository, never()).addAccount(any(Account.class));
    }


    @Test
    void testAddCampaign_ValidOrganization_ValidAccount_CampaignDoesNotExist() {
        List<Organization> organizations = List.of(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = List.of(new Account(1, 1, "Acc1", "Active"));
        List<Campaign> campaigns = Collections.emptyList();

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(adService.getAllAccountsByOrgId(1L)).thenReturn(accounts);
        when(adService.getAllCampaignsByAccountId(1L)).thenReturn(campaigns);

        int result = adService.addcampaign(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        assertEquals(0, result);

        verify(repository, times(1)).addCampaign(any(Campaign.class));
    }

    @Test
    void testAddCampaign_ValidOrganization_ValidAccount_CampaignExists() {
        List<Organization> organizations = List.of(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = List.of(new Account(1, 1, "Acc1", "Active"));
        List<Campaign> campaigns = List.of(new Campaign(1, 1, 1, "Camp1", "Active", 100));

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(adService.getAllAccountsByOrgId(1L)).thenReturn(accounts);
        when(adService.getAllCampaignsByAccountId(1L)).thenReturn(campaigns);

        int result = adService.addcampaign(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        assertEquals(1, result);

        verify(repository, never()).addCampaign(any(Campaign.class));
    }

    @Test
    void testAddCampaign_ValidOrganization_InvalidAccount() {
        List<Organization> organizations = List.of(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = Collections.emptyList();

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(adService.getAllAccountsByOrgId(1L)).thenReturn(accounts);

        int result = adService.addcampaign(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        assertEquals(2, result);

        verify(repository, never()).addCampaign(any(Campaign.class));
    }

    @Test
    void testAddCampaign_InvalidOrganization() {
        List<Organization> organizations = Collections.emptyList();

        when(repository.getAllOrganizations()).thenReturn(organizations);

        int result = adService.addcampaign(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        assertEquals(3, result);

        verify(repository, never()).addCampaign(any(Campaign.class));
    }

    @Test
    void testUpdateOrganizationStatus() {
        List<Organization> organizations = Arrays.asList(new Organization(1, "Org1", "Country1", "Active"));
        when(repository.getAllOrganizations()).thenReturn(organizations);

        boolean result = adService.updateOrganizationStatus(1, "Inactive");
        assertTrue(result);
    }

    @Test
    void testUpdateAccountStatus_Success() {
        List<Organization> organizations = Arrays.asList(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = Arrays.asList(new Account(1, 1, "Acc1", "Active"));

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(repository.getAllAccounts()).thenReturn(accounts);

        int result = adService.updateAccountStatus(1, 1L, "Inactive");
        assertEquals(0, result);
        verify(repository, times(1)).updateAccountStatus(1L, "Inactive");
    }

    @Test
    void testUpdateAccountStatus_InvalidOrganization() {
        List<Organization> organizations = Arrays.asList(new Organization(2, "Org2", "Country2", "Active"));

        when(repository.getAllOrganizations()).thenReturn(organizations);

        int result = adService.updateAccountStatus(1, 1L, "Inactive");
        assertEquals(1, result);
    }

    @Test
    void testUpdateAccountStatus_InvalidAccount() {
        List<Organization> organizations = Arrays.asList(new Organization(1, "Org1", "Country1", "Active"));
        List<Account> accounts = Arrays.asList(new Account(1, 2, "Acc2", "Active"));

        when(repository.getAllOrganizations()).thenReturn(organizations);
        when(repository.getAllAccounts()).thenReturn(accounts);

        int result = adService.updateAccountStatus(1, 1L, "Inactive");
        assertEquals(1, result);
    }

    @Test
    void testUpdateCampaign() {
        List<Campaign> campaigns = Arrays.asList(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        when(repository.getAllCampaigns()).thenReturn(campaigns);

        boolean result = adService.updateCampaign(1L, "NewName", "Inactive");
        assertTrue(result);
    }

    @Test
    void testGetSortedCampaignMetrics() {
        List<CampaignSummary> summaries = Arrays.asList(new CampaignSummary(1L, "Camp1"));
        when(repository.aggregateMetricsSummary()).thenReturn(summaries);

        List<CampaignSummary> result = adService.getSortedCampaignMetrics("spend", "asc");
        assertEquals(1, result.size());
        assertEquals("Camp1", result.get(0).getCampaignName());
    }

    @Test
    void testGetAllCampaigns() {
        List<Campaign> campaigns = Arrays.asList(new Campaign(1, 1, 1, "Camp1", "Active", 100));
        when(repository.getAllCampaigns()).thenReturn(campaigns);

        List<Campaign> result = adService.getAllCampaigns();
        assertEquals(1, result.size());
        assertEquals("Camp1", result.get(0).getName());
    }

    @Test
    void testGetAllcampaignMetrics() {
        List<campaignMetrics> metrics = Arrays.asList(new campaignMetrics(1L, "Camp1", 100.0, 200.0, 300, 400));
        when(repository.getAllCampaignMetrics()).thenReturn(metrics);

        List<campaignMetrics> result = adService.getAllcampaignMetrics();
        assertEquals(1, result.size());
        assertEquals("Camp1", result.get(0).getCampaignName());
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = Arrays.asList(new Account(1, 1, "Acc1", "Active"));
        when(repository.getAllAccounts()).thenReturn(accounts);

        List<Account> result = adService.getAllAccounts();
        assertEquals(1, result.size());
        assertEquals("Acc1", result.get(0).getName());
    }
}
