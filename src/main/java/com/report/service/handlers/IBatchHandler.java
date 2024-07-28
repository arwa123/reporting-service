package com.report.service.handlers;

import com.report.service.models.Building;

import java.io.IOException;
import java.util.List;

public interface IBatchHandler {
    void processFile(String filePath);
    void handleBatch(List<Building> batch);
    void generateReport();
}
