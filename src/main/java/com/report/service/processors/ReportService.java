package com.report.service.processors;

import com.report.service.generators.ReportGenerator;
import com.report.service.generators.ReportGeneratorFactory;
import com.report.service.model.Building;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ReportService {
    private static ReportService instance;

    private ConcurrentMap<String, Set<String>> customerCountByContract = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Set<String>> customerCountByGeoZone = new ConcurrentHashMap<>();
    private ConcurrentMap<String, List<Integer>> buildDurationByGeoZone = new ConcurrentHashMap<>();

    private ReportService() {}

    public static ReportService getInstance() {
        if (instance == null) {
            synchronized (ReportService.class) {
                if (instance == null) {
                    instance = new ReportService();
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

    public void generateReport(String format, Map<String, ?> data, String outputPath) {
        ReportGenerator reportGenerator = ReportGeneratorFactory.getReportGenerator(format);
        reportGenerator.generateReport(data, outputPath);
    }
}