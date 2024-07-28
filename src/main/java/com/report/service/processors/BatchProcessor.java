package com.report.service.processors;

import com.report.service.constants.Constants;
import com.report.service.handlers.BatchFactory;
import com.report.service.handlers.IBatchHandler;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BatchProcessor {

    private static final Logger logger = Logger.getLogger(BatchProcessor.class.getName());

    public void startProcess(String file, String batchType) {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
            IBatchHandler handler = BatchFactory.getBatchHandler(executorService, batchType);
            handler.processFile(file);
            handler.generateReport();
            executorService.shutdown();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Unknown Error occurred  "+e.getMessage());
        }
    }
}
