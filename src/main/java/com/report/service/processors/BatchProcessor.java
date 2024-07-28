package com.report.service.processors;

import com.report.service.constants.Constants;
import com.report.service.handlers.BatchFactory;
import com.report.service.handlers.IBatchHandler;

import java.io.IOException;
import java.util.concurrent.*;

public class BatchProcessor {

    public void startProcess(String file, String batchType) {
        ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
        try {
            IBatchHandler handler = BatchFactory.getBatchHandler(executorService, batchType);
            handler.processFile(file);
            handler.generateReport();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
