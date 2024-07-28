package com.report.service.services;

import com.report.service.models.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BuildingBatchServiceTest {
    private BuildingBatchService buildingBatchService;

    @BeforeEach
    void setUp() {
        buildingBatchService = BuildingBatchService.getInstance();
        Building.Builder builder1 = new Building.Builder();
        builder1.withBuildDuration(5).withContractId("contract1")
                .withGeoZone("geo1").withTeamCode("BlueTeam")
                .withCustomerId("customer1").build();
        Building building = new Building(builder1);
        buildingBatchService.processBatch(Arrays.asList(building));
    }

    @Test
    void testGetUniqueCustomerCountByContract() {
        Map<String, Long> result = buildingBatchService.getUniqueCustomerCountByContract();
        assertEquals(1, result.get("contract1"));
    }

    @Test
    void testGetUniqueCustomerCountByGeoZone() {
        Map<String, Long> result = buildingBatchService.getUniqueCustomerCountByGeoZone();
        assertEquals(1, result.get("geo1"));
    }

    @Test
    void testGetAverageBuildDurationByGeoZone() {
        Map<String, Double> result = buildingBatchService.getAverageBuildDurationByGeoZone();
        assertEquals(5.0, result.get("geo1"));
    }

    @Test
    void testGetUniqueCustomersByGeoZone() {
        Map<String, Set<String>> result = buildingBatchService.getUniqueCustomersByGeoZone();
        assertTrue(result.get("geo1").containsAll(Arrays.asList("customer1")));
    }
}
