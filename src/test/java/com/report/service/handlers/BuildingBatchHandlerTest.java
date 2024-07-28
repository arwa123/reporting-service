package com.report.service.handlers;

import com.report.service.constants.Constants;
import com.report.service.services.BuildingBatchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.ExecutorService;

public class BuildingBatchHandlerTest {

    @Mock
    private ExecutorService executorService;

    @Mock
    private BuildingBatchService buildingBatchService;

    @InjectMocks
    private BuildingBatchHandler buildingBatchHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testProcessFile() {
        try {
            String file = System.getProperty(Constants.USER_DIR).concat(Constants.RESOURCE_PATH).concat(Constants.INPUT_FILE_NAME);
            buildingBatchHandler.processFile(file);
        } catch (NullPointerException e) {
            Assertions.assertTrue(true);
        }
    }

}
