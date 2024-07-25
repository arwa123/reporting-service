package com.report.service.generators;

import java.util.Map;

public interface ReportGenerator {
    void generateReport(Map<String, ?> data, String outputPath);
}
