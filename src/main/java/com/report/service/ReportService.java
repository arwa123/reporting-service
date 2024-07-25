package com.report.service;

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

    public void processBatch(List<BuildInfo> batch) {
        for (BuildInfo buildInfo : batch) {
            customerCountByContract.computeIfAbsent(buildInfo.getContractId(), k -> ConcurrentHashMap.newKeySet()).add(buildInfo.getCustomerId());
            customerCountByGeoZone.computeIfAbsent(buildInfo.getGeoZone(), k -> ConcurrentHashMap.newKeySet()).add(buildInfo.getCustomerId());
            buildDurationByGeoZone.computeIfAbsent(buildInfo.getGeoZone(), k -> Collections.synchronizedList(new ArrayList<>())).add(buildInfo.getBuildDuration());
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
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0)));
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