package com.report.service.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildingTest {

    @Test
    void testBuildingCreation() {
        Building building = new Building.Builder()
                .withCustomerId("customer1")
                .withContractId("contract1")
                .withGeoZone("geo1")
                .withTeamCode("team1")
                .withProjectCode("project1")
                .withBuildDuration(30)
                .build();

        assertEquals("customer1", building.getCustomerId());
        assertEquals("contract1", building.getContractId());
        assertEquals("geo1", building.getGeoZone());
        assertEquals("team1", building.getTeamCode());
        assertEquals("project1", building.getProjectCode());
        assertEquals(30, building.getBuildDuration());
    }

    @Test
    void testPartialBuildingCreation() {
        Building building = new Building.Builder()
                .withCustomerId("customer2")
                .withGeoZone("geo2")
                .withBuildDuration(45)
                .build();

        assertEquals("customer2", building.getCustomerId());
        assertEquals(null, building.getContractId());
        assertEquals("geo2", building.getGeoZone());
        assertEquals(null, building.getTeamCode());
        assertEquals(null, building.getProjectCode());
        assertEquals(45, building.getBuildDuration());
    }

    @Test
    void testEmptyBuildingCreation() {
        Building building = new Building.Builder().build();

        assertEquals(null, building.getCustomerId());
        assertEquals(null, building.getContractId());
        assertEquals(null, building.getGeoZone());
        assertEquals(null, building.getTeamCode());
        assertEquals(null, building.getProjectCode());
        assertEquals(0, building.getBuildDuration());
    }
}
