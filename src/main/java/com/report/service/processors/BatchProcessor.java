package com.report.service.processors;

import com.report.service.mapper.BuildInfoParser;
import com.report.service.model.Building;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BatchProcessor {
    private static final int BATCH_SIZE = 1000;
    private static final int THREAD_POOL_SIZE = 10;
    ReportService reportService = ReportService.getInstance();

    public interface BatchHandler {
        void handleBatch(List<Building> batch);
    }


    public void startProcess(String file) throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            processFile(file, batch -> {
                Future<?> future = executorService.submit(() -> reportService.processBatch(batch));
                try {
                    future.get();  // Wait for the batch processing to complete
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
            reportService.generateReport();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void processFile(String filePath, BatchHandler handler) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Building> batch = new ArrayList<>();
            BuildInfoParser parser = new BuildInfoParser();

            while ((line = reader.readLine()) != null) {
                Building building = parser.parseLine(line);
                batch.add(building);

                if (batch.size() >= BATCH_SIZE) {
                    handler.handleBatch(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                handler.handleBatch(batch);
            }
        }
    }
}
