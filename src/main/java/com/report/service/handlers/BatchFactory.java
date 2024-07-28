package com.report.service.handlers;

import com.report.service.services.BuildingBatchService;

import java.util.concurrent.ExecutorService;

public class BatchFactory {

    public static IBatchHandler getBatchHandler(ExecutorService executorService, String batchType) {

        switch (batchType.toLowerCase()) {
            case "building":
                BuildingBatchService buildingBatchService = BuildingBatchService.getInstance();
                return new BuildingBatchHandler(executorService, buildingBatchService);
            default:
                throw new IllegalArgumentException("Unknown report format: " + batchType);
        }
    }
}
