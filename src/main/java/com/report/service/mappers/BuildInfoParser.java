package com.report.service.mappers;

import com.report.service.models.Building;

public class BuildInfoParser {
    public Building parseLine(String line) {
        String[] parts = line.split(",");
        return new Building.Builder()
                .withCustomerId(parts[0])
                .withContractId(parts[1])
                .withGeoZone(parts[2])
                .withTeamCode(parts[3])
                .withProjectCode(parts[4])
                .withBuildDuration(Integer.parseInt(parts[5].replace("s", "")))
                .build();
    }
}
