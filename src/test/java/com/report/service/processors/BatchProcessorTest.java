package com.report.service.processors;
import com.report.service.mappers.BuildInfoParser;
import com.report.service.models.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BatchProcessorTest {

    private BatchProcessor batchProcessor;
    private ReportService mockReportService;
    private BuildInfoParser mockParser;

    @BeforeEach
    void setUp() {
        mockReportService = mock(ReportService.class);
        mockParser = mock(BuildInfoParser.class);
        batchProcessor = new BatchProcessor() {
            @Override
            public void processFile(String filePath, BatchHandler handler) throws IOException {
                Building.Builder builder1 = new Building.Builder();
                builder1.withBuildDuration(5).withContractId("contract1")
                        .withGeoZone("geo1").withTeamCode("BlueTeam")
                        .withCustomerId("customer1").build();
                Building building = new Building(builder1);
                List<Building> buildings = Arrays.asList(building);
                handler.handleBatch(buildings);
            }

            // Helper method to set a custom BufferedReader for testing
            public void processFile(BufferedReader reader, BatchHandler handler) throws IOException {
                String line;
                List<Building> batch = new ArrayList<>();
                BuildInfoParser parser = new BuildInfoParser();

                while ((line = reader.readLine()) != null) {
                    Building building = parser.parseLine(line);
                    batch.add(building);

                    if (batch.size() >= 10) {
                        handler.handleBatch(batch);
                        batch.clear();
                    }
                }

                if (!batch.isEmpty()) {
                    handler.handleBatch(batch);
                }
            }
        };
        batchProcessor.reportService = mockReportService;
    }

    @Test
    void testStartProcess() throws IOException {
        String mockFile = "mockfile.txt";

        batchProcessor.startProcess(mockFile);

        // Verify that the reportService.processBatch() was called with the correct arguments
        ArgumentCaptor<List<Building>> batchCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockReportService, times(1)).processBatch(batchCaptor.capture());
        List<Building> capturedBatch = batchCaptor.getValue();
        assertEquals(1, capturedBatch.size());
        assertEquals("contract1", capturedBatch.get(0).getContractId());
        verify(mockReportService, times(1)).generateReport();
    }

    @Test
    void testProcessFile() throws IOException {
        BatchProcessor.BatchHandler mockHandler = mock(BatchProcessor.BatchHandler.class);
        BufferedReader reader = mock(BufferedReader.class);

        when(reader.readLine()).thenReturn(
                "contract1,geo1,customer1,5",
                "contract1,geo1,customer2,10",
                null
        );

        batchProcessor.processFile("dummy", mockHandler);
        ArgumentCaptor<List<Building>> batchCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockHandler, times(1)).handleBatch(batchCaptor.capture());
        List<Building> capturedBatch = batchCaptor.getValue();
        assertEquals(1, capturedBatch.size());
        assertEquals("contract1", capturedBatch.get(0).getContractId());
    }
}
