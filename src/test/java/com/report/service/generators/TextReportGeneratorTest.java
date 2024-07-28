package com.report.service.generators;

import com.report.service.constants.Constants;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextReportGeneratorTest {

    @Test
    void testGenerateReport() throws IOException {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("zone1", "5");
        data.put("zone2", "10");
        TextReportGenerator textReportGenerator = new TextReportGenerator();
        String filePath = System.getProperty("user.dir").concat(Constants.TEST_RESOURCE_PATH);
        String fileName = "output.txt";
        Path outputPath = Paths.get(filePath + fileName);
        textReportGenerator.generateReport(data, filePath);
        String expectedContent = "Report\n--------------\nGEO-ZONE: Customers\nzone1: 5\nzone2: 10\n";
        String actualContent = Files.readString(outputPath);
        assertEquals(expectedContent, actualContent);
    }
}
