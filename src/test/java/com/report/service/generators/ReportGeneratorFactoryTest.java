package com.report.service.generators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorFactoryTest {

    @Test
    void testGetReportGeneratorForExcel() {
        IReportGenerator generator = ReportGeneratorFactory.getReportGenerator("excel");
        assertTrue(generator instanceof ExcelReportGenerator);
    }

    @Test
    void testGetReportGeneratorForPdf() {
        IReportGenerator generator = ReportGeneratorFactory.getReportGenerator("pdf");
        assertTrue(generator instanceof PdfReportGenerator);
    }

    @Test
    void testGetReportGeneratorForWord() {
        IReportGenerator generator = ReportGeneratorFactory.getReportGenerator("word");
        assertTrue(generator instanceof WordReportGenerator);
    }

    @Test
    void testGetReportGeneratorForText() {
        IReportGenerator generator = ReportGeneratorFactory.getReportGenerator("text");
        assertTrue(generator instanceof TextReportGenerator);
    }

    @Test
    void testGetReportGeneratorForUnknownFormat() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ReportGeneratorFactory.getReportGenerator("unknown");
        });
        assertEquals("Unknown report format: unknown", exception.getMessage());
    }

    @Test
    void testGetReportGeneratorCaseInsensitive() {
        IReportGenerator generator = ReportGeneratorFactory.getReportGenerator("TeXt");
        assertTrue(generator instanceof TextReportGenerator);
    }
}
