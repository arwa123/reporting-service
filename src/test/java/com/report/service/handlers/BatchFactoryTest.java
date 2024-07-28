package com.report.service.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class BatchFactoryTest {

    private ExecutorService executorService;

    @BeforeEach
    public void setUp() {
        executorService = mock(ExecutorService.class);
    }

    @Test
    public void testGetBatchHandlerBuilding() {
        IBatchHandler handler = BatchFactory.getBatchHandler(executorService, "building");
        assertTrue(handler instanceof BuildingBatchHandler);
    }

    @Test
    public void testGetBatchHandlerUnknownType() {
        assertThrows(IllegalArgumentException.class, () -> {
            BatchFactory.getBatchHandler(executorService, "unknownType");
        });
    }

    @Test
    public void testGetBatchHandlerCaseInsensitive() {
        IBatchHandler handler = BatchFactory.getBatchHandler(executorService, "BuIlDiNg");
        assertTrue(handler instanceof BuildingBatchHandler);
    }
}
