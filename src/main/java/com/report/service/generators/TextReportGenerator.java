package com.report.service.generators;

import com.report.service.constants.Constants;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextReportGenerator implements IReportGenerator {

    private static final Logger logger = Logger.getLogger(TextReportGenerator.class.getName());

    @Override
    public void generateReport(Map<String, ?> data, String filePath) {
        String fileName = "output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+fileName))) {
            writer.write("Report" + "\n");
            writer.write("--------------\n");
            writer.write("GEO-ZONE" + ": " + "Customers" + "\n");
            for (Map.Entry<String, ?> entry : data.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            System.out.println("Text report generated successfully.");
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error occurred while generating report  "+e.getMessage());
        }
    }
}