package com.report.service.generators;

public class ReportGeneratorFactory {
    public static IReportGenerator getReportGenerator(String format) {
        switch (format.toLowerCase()) {
            case "excel":
                return new ExcelReportGenerator();
            case "pdf":
                return new PdfReportGenerator();
            case "word":
                return new WordReportGenerator();
            case "text":
                return new TextReportGenerator();
            default:
                throw new IllegalArgumentException("Unknown report format: " + format);
        }
    }
}