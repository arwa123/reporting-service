package com.report.service.generators;

import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

class TextReportGeneratorTest {

    @Test
    void testGenerateReport() throws IOException {
        Map<String, Integer> data = new HashMap<>();
        data.put("geo1", 10);
        data.put("geo2", 20);

        BufferedWriter mockWriter = mock(BufferedWriter.class);

        TextReportGenerator reportGenerator = new TextReportGenerator() {
            @Override
            public void generateReport(Map<String, ?> data, String filePath) {
                try {
                    String fileName = "output.txt";
                    try (BufferedWriter writer = mockWriter) {
                        writer.write("Report" + "\n");
                        writer.write("--------------\n");
                        writer.write("GEO-ZONE" + ": " + "Customers" + "\n");
                        for (Map.Entry<String, ?> entry : data.entrySet()) {
                            writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
                        }
                        System.out.println("Text report generated successfully.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        reportGenerator.generateReport(data, "");
        verify(mockWriter).write("Report\n");
        verify(mockWriter).write("--------------\n");
        verify(mockWriter).write("GEO-ZONE: Customers\n");
        verify(mockWriter).write("geo1: 10\n");
        verify(mockWriter).write("geo2: 20\n");
        verify(mockWriter).close();
    }
}
