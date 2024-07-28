package com.report.service.handlers;

import com.report.service.constants.Constants;
import com.report.service.mappers.BuildInfoParser;
import com.report.service.models.Building;
import com.report.service.services.BuildingBatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuildingBatchHandlerTest {

    @Mock
    private ExecutorService executorService;

    @Mock
    private BuildingBatchService buildingBatchService;

    @Mock
    private BuildInfoParser parser;

    @InjectMocks
    private BuildingBatchHandler buildingBatchHandler;

    private static final int BATCH_SIZE = 10; // Set a small batch size for testing
    Building building;

    @BeforeEach
    public void setUp() {
        buildingBatchHandler = new BuildingBatchHandler(executorService, buildingBatchService);
        building = new Building.Builder()
                .withCustomerId("customer1")
                .withContractId("contract1")
                .withGeoZone("geo1")
                .withTeamCode("team1")
                .withProjectCode("project1")
                .withBuildDuration(30)
                .build();
    }

    @Test
    public void testHandleBatch() throws Exception {
        List<Building> batch = new ArrayList<>();
            batch.add(building); // Add dummy buildings

        Future<?> future = mock(Future.class);
      //  when(executorService.submit(any(Runnable.class))).thenReturn(future);
        when(future.get()).thenReturn(null);

        buildingBatchHandler.handleBatch(batch);

        verify(executorService).submit(any(Runnable.class));
        verify(future).get();
    }

    @Test
    public void testProcessFile() throws Exception {
        // Simulate file content
        String fileContent = "line1\nline2\n";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(fileContent));

        // Mock BufferedReader and FileReader
        when(parser.parseLine(anyString())).thenReturn(building); // Return a dummy Building for each line

        buildingBatchHandler.processFile(System.getProperty(Constants.USER_DIR).concat(Constants.RESOURCE_PATH).concat(Constants.INPUT_FILE_NAME));

        verify(parser, times(2)).parseLine(anyString()); // Called 2 times: for each line
    }

    @Test
    public void testGenerateReport() {
        buildingBatchHandler.generateReport();

        verify(buildingBatchService).generateReport();
    }
}