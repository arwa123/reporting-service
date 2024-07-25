package com.report.service.processors;

import com.report.service.mapper.BuildInfoParser;
import com.report.service.model.Building;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class BatchProcessor {
    private static final int BATCH_SIZE = 10;
    private static final int THREAD_POOL_SIZE = 10;
    private static final String FILE_PATH = "/Users/asaify/Documents/my-workspace/reporting-service/src/main/resources/";


    public interface BatchHandler {
        void handleBatch(List<Building> batch);
    }


    public void startProcess() throws IOException {
        String file = FILE_PATH.concat("input.txt");
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ReportService reportService = ReportService.getInstance();

        try {
            processFile(file, batch -> {
                Future<?> future = executorService.submit(() -> reportService.processBatch(batch));
                try {
                    future.get();  // Wait for the batch processing to complete
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
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


        Map<String, Long> uniqueCustomerCountByContract = reportService.getUniqueCustomerCountByContract();
        Map<String, Long> uniqueCustomerCountByGeoZone = reportService.getUniqueCustomerCountByGeoZone();
        Map<String, Double> averageBuildDurationByGeoZone = reportService.getAverageBuildDurationByGeoZone();
        Map<String, Set<String>> uniqueCustomersByGeoZone = reportService.getUniqueCustomersByGeoZone();

        System.out.println("Unique Customer Count by Contract: " + uniqueCustomerCountByContract);
        System.out.println("Unique Customer Count by GeoZone: " + uniqueCustomerCountByGeoZone);
        System.out.println("Average Build Duration by GeoZone: " + averageBuildDurationByGeoZone);
        System.out.println("Unique Customers by GeoZone: " + uniqueCustomersByGeoZone);

        reportService.generateReport("text", uniqueCustomerCountByGeoZone, FILE_PATH);
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
