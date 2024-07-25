package com.report.service.processors;

import com.report.service.model.Building;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ParallelReportService{
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public Map<String, Long> getUniqueCustomerCountByContract(List<Building> buildingList) {
        return forkJoinPool.submit(() -> buildingList.parallelStream()
                .collect(Collectors.groupingBy(Building::getContractId, Collectors.mapping(Building::getCustomerId, Collectors.toSet())))
                .entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()))).join();
    }

    public Map<String, Long> getUniqueCustomerCountByGeoZone(List<Building> buildingList) {
        return forkJoinPool.submit(() -> buildingList.parallelStream()
                .collect(Collectors.groupingBy(Building::getGeoZone, Collectors.mapping(Building::getCustomerId, Collectors.toSet())))
                .entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (long) e.getValue().size()))).join();
    }


    public Map<String, Double> getAverageBuildDurationByGeoZone(List<Building> buildingList) {
        return forkJoinPool.submit(() -> buildingList.parallelStream()
                .collect(Collectors.groupingBy(Building::getGeoZone, Collectors.averagingInt(Building::getBuildDuration)))).join();
    }


    public Map<String, Set<String>> getUniqueCustomersByGeoZone(List<Building> buildingList) {
        return forkJoinPool.submit(() -> buildingList.parallelStream()
                .collect(Collectors.groupingBy(Building::getGeoZone, Collectors.mapping(Building::getCustomerId, Collectors.toSet())))).join();
    }
}