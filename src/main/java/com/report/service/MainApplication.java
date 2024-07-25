package com.report.service;

import com.report.service.processors.BatchProcessor;
import com.report.service.processors.ReportService;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class MainApplication {


    private static final int THREAD_POOL_SIZE = 10;
    private static final String FILE_PATH = "/Users/asaify/Documents/my-workspace/reporting-service/src/main/resources/";


    public static void main(String[] args) {
        String file = FILE_PATH.concat("input.txt");
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ReportService reportService = ReportService.getInstance();
        BatchProcessor batchProcessor = new BatchProcessor();

        try {
            batchProcessor.processFile(file, batch -> {
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

}
