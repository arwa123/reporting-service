package com.report.service;

import com.report.service.mapper.BuildInfoParser;
import com.report.service.model.Building;
import com.report.service.processors.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportServiceTest {
    private BuildInfoParser parser;
    private ReportService reportService;
    private List<Building> buildingList;

    @BeforeEach
    public void setup() throws IOException {
        parser = new BuildInfoParser();
        reportService = ReportService.getInstance();
        String data1 = "2343225,2345,us_east,RedTeam,ProjectApple,3445s";
        String data2 = "2343225,2345,us_east,RedTeam,ProjectApple,3445s";
        buildingList = new ArrayList<>();
        buildingList.add(parser.parseLine(data1));
        buildingList.add(parser.parseLine(data2));
        MainApplication.main(null);
    }

    @Test
    public void testUniqueCustomerCountByContract() {
        Map<String, Long> result = reportService.getUniqueCustomerCountByContract();
        assertEquals(3, result.get("2345"));
    }

    @Test
    public void testUniqueCustomerCountByGeoZone() {
        Map<String, Long> result = reportService.getUniqueCustomerCountByGeoZone();
        assertEquals(1, result.get("us_east"));
        assertEquals(2, result.get("us_west"));
        assertEquals(2, result.get("eu_west"));
    }

    @Test
    public void testAverageBuildDurationByGeoZone() {
        Map<String, Double> result = reportService.getAverageBuildDurationByGeoZone();
        assertEquals(3445.0, result.get("us_east"));
        assertEquals(2216.0, result.get("us_west"));
        assertEquals(4222.0, result.get("eu_west"));
    }

    @Test
    public void testUniqueCustomersByGeoZone() {
        Map<String, Set<String>> result = reportService.getUniqueCustomersByGeoZone();
        assertEquals(Set.of("2343225"), result.get("us_east"));
        assertEquals(Set.of("1223456", "1233456"), result.get("us_west"));
        assertEquals(Set.of("3244332", "3244132"), result.get("eu_west"));
    }
}