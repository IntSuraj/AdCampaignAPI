package com.assignmentFirst.AdsCampaignAPI.util;

import com.opencsv.bean.AbstractBeanField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.ErrorManager;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final Logger logger = LoggerFactory.getLogger(CSVUtil.class);
    @Override
    protected LocalDate convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null; // Return null if the input string is null or empty
        }
        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            // Log the error or handle it as needed

            logger.error("Error parsing date: " + value, e);
            return null; // Return null if the date cannot be parsed
        }
    }
}
