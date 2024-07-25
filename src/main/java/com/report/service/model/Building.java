package com.report.service.model;

public class Building {
    private final String customerId;
    private final String contractId;
    private final String geoZone;
    private final String teamCode;
    private final String projectCode;
    private final int buildDuration;

    private Building(Builder builder) {
        this.customerId = builder.customerId;
        this.contractId = builder.contractId;
        this.geoZone = builder.geoZone;
        this.teamCode = builder.teamCode;
        this.projectCode = builder.projectCode;
        this.buildDuration = builder.buildDuration;
    }

    public String getCustomerId() { return customerId; }
    public String getContractId() { return contractId; }
    public String getGeoZone() { return geoZone; }
    public String getTeamCode() { return teamCode; }
    public String getProjectCode() { return projectCode; }
    public int getBuildDuration() { return buildDuration; }

    public static class Builder {
        private String customerId;
        private String contractId;
        private String geoZone;
        private String teamCode;
        private String projectCode;
        private int buildDuration;

        public Builder withCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder withContractId(String contractId) {
            this.contractId = contractId;
            return this;
        }

        public Builder withGeoZone(String geoZone) {
            this.geoZone = geoZone;
            return this;
        }

        public Builder withTeamCode(String teamCode) {
            this.teamCode = teamCode;
            return this;
        }

        public Builder withProjectCode(String projectCode) {
            this.projectCode = projectCode;
            return this;
        }

        public Builder withBuildDuration(int buildDuration) {
            this.buildDuration = buildDuration;
            return this;
        }

        public Building build() {
            return new Building(this);
        }
    }
}