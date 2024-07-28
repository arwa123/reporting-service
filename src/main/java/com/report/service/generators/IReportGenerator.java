package com.report.service.generators;

import java.util.Map;

public interface IReportGenerator {
    void generateReport(Map<String, ?> data, String outputPath);
}
