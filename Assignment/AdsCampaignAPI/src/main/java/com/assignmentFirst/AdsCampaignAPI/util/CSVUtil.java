package com.assignmentFirst.AdsCampaignAPI.util;

import com.assignmentFirst.AdsCampaignAPI.model.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CSVUtil {

    // Headers for different entities in the CSV
    public static final String FILE_PATH = "src/main/resources/dataset.csv";
    private static final Logger logger = LoggerFactory.getLogger(CSVUtil.class);
    private static final String[] HEADERS = {
            "orgId", "orgName", "orgCountry", "orgStatus",
            "accountId", "accountName", "accountStatus",
            "campaignId", "campaignName", "campaignStatus", "budget",
            "reportDate", "spend", "revenue", "impressions", "clicks"
    };

    // Read organizations from CSV
    public static List<Organization>readOrganizations(String filePath) {
        List<Organization> organizations = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            HeaderColumnNameMappingStrategy<Organization> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Organization.class);
            organizations = new CsvToBeanBuilder<Organization>(reader)
                    .withMappingStrategy(strategy)
                    .build()
                    .parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Filtering out duplicates based on orgId
        Set<Integer> uniqueOrgIds = new HashSet<>();
        organizations.removeIf(org -> !uniqueOrgIds.add(org.getOrgId()));

        return organizations;
    }

    // Read accounts from CSV
    public static List<Account> readAccounts(String filePath) {
        List<Account> accounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            HeaderColumnNameMappingStrategy<Account> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Account.class);

            CsvToBean<Account> csvToBean = new CsvToBeanBuilder<Account>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            accounts = csvToBean.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Filtering out accounts with null or empty accountIds and duplicates based on accountId
        Set<Long> uniqueAccIds = new HashSet<>();
        accounts.removeIf(acc -> acc.getAccId() == 0 || !uniqueAccIds.add(acc.getAccId()));
        return accounts;
    }


    // Read campaigns from CSV
    public static List<Campaign> readCampaigns(String filePath) {
        List<Campaign> campaigns = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            HeaderColumnNameMappingStrategy<Campaign> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Campaign.class);
            campaigns = new CsvToBeanBuilder<Campaign>(reader)
                    .withMappingStrategy(strategy)
                    .build()
                    .parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Filtering out campaigns with null or empty campaignIds and duplicates based on (orgId, campaignId) combination
        Set<String> uniqueOrgCampaignIds = new HashSet<>();
        campaigns.removeIf(camp -> camp.getCampaignId() == 0 || !uniqueOrgCampaignIds.add(camp.getOrgId() + "-" + camp.getCampaignId()));

        return campaigns;
    }


    // Read campaign metrics from CSV
    public static List<campaignMetrics> readCampaignMetrics(String filePath) {
        List<campaignMetrics> metricsList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            HeaderColumnNameMappingStrategy<campaignMetrics> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(campaignMetrics.class);

            CsvToBean<campaignMetrics> csvToBean = new CsvToBeanBuilder<campaignMetrics>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            metricsList = csvToBean.parse();

            for (campaignMetrics metrics : metricsList) {
                // Check and set null values for missing data
                if (metrics.getCampaignName() == null || metrics.getCampaignName().isEmpty()) {
                    metrics.setCampaignName(null);
                }
                if (metrics.getReportDate() == null) {
                    metrics.setReportDate(null);
                }
                if (metrics.getSpend() == null) {
                    metrics.setSpend(null);
                }
                if (metrics.getRevenue() == null) {
                    metrics.setRevenue(null);
                }
                if (metrics.getImpressions() == null) {
                    metrics.setImpressions(null);
                }
                if (metrics.getClicks() == null) {
                    metrics.setClicks(null);
                }
            }
            // Filter out metrics with null campaignId
            metricsList = metricsList.stream()
                    .filter(metrics -> metrics.getCampaignId() != null)
                    .collect(Collectors.toList());

            return metricsList;
        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    // Method to aggregate metrics and return a list of CampaignSummary
    public static List<CampaignSummary> aggregateMetrics(List<campaignMetrics> metricsList) {
        Map<Long, CampaignSummary> summaryMap = new HashMap<>();
        List<Campaign> campaignss = readCampaigns(FILE_PATH);
        for (campaignMetrics metrics : metricsList) {
            if (metrics.getCampaignId() == null) {
                continue;
            }

            long campaignId = metrics.getCampaignId();
            String campaignName = metrics.getCampaignName() != null ? metrics.getCampaignName() : "Unknown";
            Campaign campaign = campaignss.stream()
                    .filter(c -> c.getCampaignId() == campaignId)
                    .findFirst()
                    .orElse(null);

            long budget = campaign != null ? campaign.getBudget() : 0;

            CampaignSummary summary = summaryMap.getOrDefault(campaignId, new CampaignSummary(campaignId, campaignName));

            if (metrics.getReportDate() != null || metrics.getSpend() != null || metrics.getRevenue() != null ||
                    metrics.getImpressions() != null || metrics.getClicks() != null) {
                // Case 1: Presence of data
                if (summary.getBudget() == (long) 0.0){
                    summary.setBudget(summary.getBudget() + budget);
                }
                summary.setSpend(summary.getSpend() + (metrics.getSpend() != null ? metrics.getSpend() : 0));
                summary.setRevenue(summary.getRevenue() + (metrics.getRevenue() != null ? metrics.getRevenue() : 0));
                summary.setImpressions(summary.getImpressions() + (metrics.getImpressions() != null ? metrics.getImpressions() : 0));
                summary.setClicks(summary.getClicks() + (metrics.getClicks() != null ? metrics.getClicks() : 0));
            } else {
                // Case 2: No data, assign null or zero
                summary.setBudget(summary.getBudget() + 0);
                summary.setSpend(summary.getSpend() + 0);
                summary.setRevenue(summary.getRevenue() + 0);
                summary.setImpressions(summary.getImpressions() + 0);
                summary.setClicks(summary.getClicks() + 0);
            }

            summaryMap.put(campaignId, summary);
        }

        return new ArrayList<>(summaryMap.values());
    }


    // Add organization to CSV if it doesn't already exist
    public static void addOrganizationwriting(Organization organization) {
        List<String[]> allRows = new ArrayList<>();

        // Read the existing data
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        // Check if the organization already exists
        boolean exists = allRows.stream()
                .anyMatch(row -> row[0].equals(String.valueOf(organization.getOrgId())));

        // If the organization doesn't exist, append it
        if (!exists) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
                writer.writeNext(new String[]{
                        String.valueOf(organization.getOrgId()),
                        organization.getOrgName(),
                        organization.getOrgCountry(),
                        organization.getOrgStatus(),
                        "", "", "", "", "", "", "", "", "", "", "", ""
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Add account to CSV if it doesn't already exist
    public static void addAccountWriting(Account account) {
        List<Organization> organizations = readOrganizations(FILE_PATH);

        Organization org = organizations.stream()
                .filter(o -> o.getOrgId() == account.getOrgId())
                .findFirst()
                .orElse(null);

        if (org != null) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
                writer.writeNext(new String[]{
                        String.valueOf(org.getOrgId()),
                        org.getOrgName(),
                        org.getOrgCountry(),
                        org.getOrgStatus(),
                        String.valueOf(account.getAccId()),
                        account.getName(),
                        account.getStatus(),
                        "", "", "", "", "", "", "", "", ""
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Add campaign to CSV if it doesn't already exist
    public static void addCampaignWriting(Campaign camp) {
        List<Organization> organizations = readOrganizations(FILE_PATH);
        List<Account> accounts = readAccounts(FILE_PATH);
        List<Campaign> campaigns = readCampaigns(FILE_PATH);

        // Check if the campaign already exists within the same organization and account
        boolean exists = campaigns.stream()
                .anyMatch(c -> c.getCampaignId() == camp.getCampaignId() && c.getOrgId() == camp.getOrgId() && c.getAccId() == camp.getAccId());

        if (!exists) {
            Account acc = accounts.stream()
                    .filter(a -> a.getAccId() == camp.getAccId())
                    .findFirst()
                    .orElse(null);

            Organization org = (acc != null) ? organizations.stream()
                    .filter(o -> o.getOrgId() == camp.getOrgId())
                    .findFirst()
                    .orElse(null) : null;

            if (org != null && acc != null) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
                    writer.writeNext(new String[]{
                            String.valueOf(org.getOrgId()),
                            org.getOrgName(),
                            org.getOrgCountry(),
                            org.getOrgStatus(),
                            String.valueOf(acc.getAccId()),
                            acc.getName(),
                            acc.getStatus(),
                            String.valueOf(camp.getCampaignId()),
                            camp.getName(),
                            camp.getStatus(),
                            String.valueOf(camp.getBudget()),
                            "", "", "", "", ""
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    // Update organization status in CSV
    public static void updateOrganizationStatus(int orgId, String newStatus) {
        List<String[]> allRows = new ArrayList<>();

        // Read the existing data
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Remove the existing header before processing
        if (!allRows.isEmpty() && allRows.get(0)[0].equals("orgId")) {
            allRows.remove(0);
        }

        // Update the status for the specified organization
        for (String[] row : allRows) {
            if (row.length > 3 && row[0].equals(String.valueOf(orgId))) {
                row[3] = newStatus;
            }
        }

        // Write the updated data back to the CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            // Write the header once
            writer.writeNext(HEADERS);
            // Write the updated data
            writer.writeAll(allRows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Update account status in CSV
    public static void updateAccountStatus(long accountId, String newStatus) {
        List<String[]> allRows = new ArrayList<>();

        // Read the existing data
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Remove the existing header before processing
        if (!allRows.isEmpty() && allRows.get(0)[0].equals("orgId")) {
            allRows.remove(0);
        }

        // Update the status for the specified account
        for (String[] row : allRows) {
            if (row.length > 6 && row[4].equals(String.valueOf(accountId))) {
                row[6] = newStatus;
            }
        }

        // Write the updated data back to the CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            // Write the header once
            writer.writeNext(HEADERS);
            // Write the updated data
            writer.writeAll(allRows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Update campaign name and status in CSV
    public static void updateCampaign(long campaignId, String newName, String newStatus) {
        List<String[]> allRows = new ArrayList<>();

        // Read the existing data
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Remove the existing header before processing
        if (!allRows.isEmpty() && allRows.get(0)[0].equals("orgId")) {
            allRows.remove(0);
        }

        // Update the name and status for the specified campaign
        for (String[] row : allRows) {
            if (row.length > 8 && row[7].equals(String.valueOf(campaignId))) {
                row[8] = newName;
                row[9] = newStatus;
            }
        }

        // Write the updated data back to the CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            // Write the header once
            writer.writeNext(HEADERS);
            // Write the updated data
            writer.writeAll(allRows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
