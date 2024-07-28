package com.report.service.processors;
import com.report.service.handlers.BatchFactory;
import com.report.service.handlers.IBatchHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;


public class BatchProcessorTest {

    @Mock
    private ExecutorService executorService;

    @Mock
    private IBatchHandler batchHandler;

    @InjectMocks
    private BatchProcessor batchProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testStartProcess() throws IOException {
        String file = "testFilePath";
        String batchType = "building";

        try (MockedStatic<BatchFactory> batchFactoryMockedStatic = mockStatic(BatchFactory.class)) {
            batchFactoryMockedStatic.when(() -> BatchFactory.getBatchHandler(any(ExecutorService.class), eq(batchType)))
                    .thenReturn(batchHandler);

            batchProcessor.startProcess(file, batchType);

            verify(batchHandler).processFile(file);
            verify(batchHandler).generateReport();
        }
    }

    @Test
    public void testStartProcessWithIOException() throws IOException {
        String file = "testFilePath";
        String batchType = "building";

        try (MockedStatic<BatchFactory> batchFactoryMockedStatic = mockStatic(BatchFactory.class)) {
            batchFactoryMockedStatic.when(() -> BatchFactory.getBatchHandler(any(ExecutorService.class), eq(batchType)))
                    .thenReturn(batchHandler);

            doThrow(new IOException("IO Exception")).when(batchHandler).processFile(file);

            batchProcessor.startProcess(file, batchType);

            verify(batchHandler).processFile(file);
            verify(batchHandler, never()).generateReport();
        }
    }
}
