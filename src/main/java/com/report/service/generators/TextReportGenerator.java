package com.report.service.generators;

import com.report.service.constants.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class TextReportGenerator implements ReportGenerator {

    @Override
    public void generateReport(Map<String, ?> data, String filePath) {
        String fileName = Constants.OUTPUT_FILE_NAME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+fileName))) {
            writer.write("Report" + "\n");
            writer.write("--------------\n");
            writer.write("GEO-ZONE" + ": " + "Customers" + "\n");
            for (Map.Entry<String, ?> entry : data.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            System.out.println("Text report generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}