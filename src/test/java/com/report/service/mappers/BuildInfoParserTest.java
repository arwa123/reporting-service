package com.report.service.mappers;

import com.report.service.models.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuildInfoParserTest {

    private BuildInfoParser parser;

    @BeforeEach
    void setUp() {
        parser = new BuildInfoParser();
    }

    @Test
    void testParseLine() {
        String line = "customer1,contract1,geo1,team1,project1,30s";
        Building building = parser.parseLine(line);

        assertEquals("customer1", building.getCustomerId());
        assertEquals("contract1", building.getContractId());
        assertEquals("geo1", building.getGeoZone());
        assertEquals("team1", building.getTeamCode());
        assertEquals("project1", building.getProjectCode());
        assertEquals(30, building.getBuildDuration());
    }

    @Test
    void testParseLineWithDifferentValues() {
        String line = "customer2,contract2,geo2,team2,project2,45s";
        Building building = parser.parseLine(line);

        assertEquals("customer2", building.getCustomerId());
        assertEquals("contract2", building.getContractId());
        assertEquals("geo2", building.getGeoZone());
        assertEquals("team2", building.getTeamCode());
        assertEquals("project2", building.getProjectCode());
        assertEquals(45, building.getBuildDuration());
    }

    @Test
    void testParseLineWithInvalidDuration() {
        String line = "customer3,contract3,geo3,team3,project3,not_a_number";

        assertThrows(NumberFormatException.class, () -> parser.parseLine(line));
    }

    @Test
    void testParseLineWithMissingFields() {
        String line = "customer4,contract4,geo4,team4,project4";

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> parser.parseLine(line));
    }
}
