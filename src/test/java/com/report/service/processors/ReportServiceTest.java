package com.report.service.processors;

import com.report.service.models.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceTest {
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = ReportService.getInstance();
        Building.Builder builder1 = new Building.Builder();
        builder1.withBuildDuration(5).withContractId("contract1")
                .withGeoZone("geo1").withTeamCode("BlueTeam")
                .withCustomerId("customer1").build();
        Building building = new Building(builder1);
        reportService.processBatch(Arrays.asList(building));
    }

    @Test
    void testGetUniqueCustomerCountByContract() {
        Map<String, Long> result = reportService.getUniqueCustomerCountByContract();
        assertEquals(1, result.get("contract1"));
    }

    @Test
    void testGetUniqueCustomerCountByGeoZone() {
        Map<String, Long> result = reportService.getUniqueCustomerCountByGeoZone();
        assertEquals(1, result.get("geo1"));
    }

    @Test
    void testGetAverageBuildDurationByGeoZone() {
        Map<String, Double> result = reportService.getAverageBuildDurationByGeoZone();
        assertEquals(5.0, result.get("geo1"));
    }

    @Test
    void testGetUniqueCustomersByGeoZone() {
        Map<String, Set<String>> result = reportService.getUniqueCustomersByGeoZone();
        assertTrue(result.get("geo1").containsAll(Arrays.asList("customer1")));
    }
}
