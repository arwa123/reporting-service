package com.report.service.services;

import com.report.service.constants.Constants;
import com.report.service.generators.IReportGenerator;
import com.report.service.generators.ReportGeneratorFactory;
import com.report.service.models.Building;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class BuildingBatchService {
    private static BuildingBatchService instance;
    private ConcurrentMap<String, Set<String>> customerCountByContract = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Set<String>> customerCountByGeoZone = new ConcurrentHashMap<>();
    private ConcurrentMap<String, List<Integer>> buildDurationByGeoZone = new ConcurrentHashMap<>();

    private BuildingBatchService() {
    }

    public static BuildingBatchService getInstance() {
        if (instance == null) {
            synchronized (BuildingBatchService.class) {
                if (instance == null) {
                    instance = new BuildingBatchService();
                }
            }
        }
        return instance;
    }

    public void processBatch(List<Building> batch) {
        for (Building building : batch) {
            customerCountByContract.computeIfAbsent(building.getContractId(), k -> ConcurrentHashMap.newKeySet()).add(building.getCustomerId());
            customerCountByGeoZone.computeIfAbsent(building.getGeoZone(), k -> ConcurrentHashMap.newKeySet()).add(building.getCustomerId());
            buildDurationByGeoZone.computeIfAbsent(building.getGeoZone(), k -> Collections.synchronizedList(new ArrayList<>())).add(building.getBuildDuration());
        }
    }

    public Map<String, Long> getUniqueCustomerCountByContract() {
        return customerCountByContract.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()));
    }

    public Map<String, Long> getUniqueCustomerCountByGeoZone() {
        return customerCountByGeoZone.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()));
    }

    public Map<String, Double> getAverageBuildDurationByGeoZone() {
        return buildDurationByGeoZone.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().collect(Collectors.averagingDouble(f -> f))));
    }

    public Map<String, Set<String>> getUniqueCustomersByGeoZone() {
        return customerCountByGeoZone.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void generateReport() {
        Map<String, Long> uniqueCustomerCountByContract = getUniqueCustomerCountByContract();
        Map<String, Long> uniqueCustomerCountByGeoZone = getUniqueCustomerCountByGeoZone();
        Map<String, Double> averageBuildDurationByGeoZone = getAverageBuildDurationByGeoZone();
        Map<String, Set<String>> uniqueCustomersByGeoZone = getUniqueCustomersByGeoZone();

        System.out.println("Unique Customer Count by Contract: " + uniqueCustomerCountByContract);
        System.out.println("Unique Customer Count by GeoZone: " + uniqueCustomerCountByGeoZone);
        System.out.println("Average Build Duration by GeoZone: " + averageBuildDurationByGeoZone);
        System.out.println("Unique Customers by GeoZone: " + uniqueCustomersByGeoZone);

        IReportGenerator reportGenerator = ReportGeneratorFactory.getReportGenerator("text");
        reportGenerator.generateReport(uniqueCustomerCountByGeoZone, System.getProperty("user.dir").concat(Constants.RESOURCE_PATH));
    }
}