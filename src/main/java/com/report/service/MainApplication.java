package com.report.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class MainApplication {


    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        String filePath = "/Users/asaify/Documents/my-workspace/reporting-service/src/main/resources/input.txt";
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ReportService reportService = ReportService.getInstance();
        BatchProcessor batchProcessor = new BatchProcessor();

        try {
            batchProcessor.processFile(filePath, batch -> {
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

        // Generate the report
        Map<String, Long> uniqueCustomerCountByContract = reportService.getUniqueCustomerCountByContract();
        Map<String, Long> uniqueCustomerCountByGeoZone = reportService.getUniqueCustomerCountByGeoZone();
        Map<String, Double> averageBuildDurationByGeoZone = reportService.getAverageBuildDurationByGeoZone();
        Map<String, Set<String>> uniqueCustomersByGeoZone = reportService.getUniqueCustomersByGeoZone();

        // Output the results (for demonstration purposes, you might want to use a ReportGenerator)
        System.out.println("Unique Customer Count by Contract: " + uniqueCustomerCountByContract);
        System.out.println("Unique Customer Count by GeoZone: " + uniqueCustomerCountByGeoZone);
        System.out.println("Average Build Duration by GeoZone: " + averageBuildDurationByGeoZone);
        System.out.println("Unique Customers by GeoZone: " + uniqueCustomersByGeoZone);

        String outputPath = "/Users/asaify/Documents/my-workspace/reporting-service/src/main/resources/";
        reportService.generateReport("text",uniqueCustomerCountByGeoZone,outputPath);
    }

}
