package com.report.service.generators;

import com.report.service.generators.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorFactoryTest {

    @Test
    void testGetReportGeneratorForExcel() {
        ReportGenerator generator = ReportGeneratorFactory.getReportGenerator("excel");
        assertTrue(generator instanceof ExcelReportGenerator);
    }

    @Test
    void testGetReportGeneratorForPdf() {
        ReportGenerator generator = ReportGeneratorFactory.getReportGenerator("pdf");
        assertTrue(generator instanceof PdfReportGenerator);
    }

    @Test
    void testGetReportGeneratorForWord() {
        ReportGenerator generator = ReportGeneratorFactory.getReportGenerator("word");
        assertTrue(generator instanceof WordReportGenerator);
    }

    @Test
    void testGetReportGeneratorForText() {
        ReportGenerator generator = ReportGeneratorFactory.getReportGenerator("text");
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
        ReportGenerator generator = ReportGeneratorFactory.getReportGenerator("TeXt");
        assertTrue(generator instanceof TextReportGenerator);
    }
}
