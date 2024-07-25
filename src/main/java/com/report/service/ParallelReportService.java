package com.report.service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ParallelReportService{
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public Map<String, Long> getUniqueCustomerCountByContract(List<BuildInfo> buildInfoList) {
        return forkJoinPool.submit(() -> buildInfoList.parallelStream()
                .collect(Collectors.groupingBy(BuildInfo::getContractId, Collectors.mapping(BuildInfo::getCustomerId, Collectors.toSet())))
                .entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()))).join();
    }

    public Map<String, Long> getUniqueCustomerCountByGeoZone(List<BuildInfo> buildInfoList) {
        return forkJoinPool.submit(() -> buildInfoList.parallelStream()
                .collect(Collectors.groupingBy(BuildInfo::getGeoZone, Collectors.mapping(BuildInfo::getCustomerId, Collectors.toSet())))
                .entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()))).join();
    }


    public Map<String, Double> getAverageBuildDurationByGeoZone(List<BuildInfo> buildInfoList) {
        return forkJoinPool.submit(() -> buildInfoList.parallelStream()
                .collect(Collectors.groupingBy(BuildInfo::getGeoZone, Collectors.averagingInt(BuildInfo::getBuildDuration)))).join();
    }


    public Map<String, Set<String>> getUniqueCustomersByGeoZone(List<BuildInfo> buildInfoList) {
        return forkJoinPool.submit(() -> buildInfoList.parallelStream()
                .collect(Collectors.groupingBy(BuildInfo::getGeoZone, Collectors.mapping(BuildInfo::getCustomerId, Collectors.toSet())))).join();
    }
}