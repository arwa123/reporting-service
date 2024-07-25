package com.report.service;

public class ReportGeneratorFactory {
    public static ReportGenerator getReportGenerator(String format) {
        switch (format.toLowerCase()) {
            case "excel":
                return new ExcelReportGenerator();
            case "pdf":
                return new PdfReportGenerator();
            case "word":
                return new WordReportGenerator();
            default:
                throw new IllegalArgumentException("Unknown report format: " + format);
        }
    }
}