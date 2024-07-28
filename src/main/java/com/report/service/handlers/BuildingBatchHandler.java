package com.report.service.handlers;

import com.report.service.constants.Constants;
import com.report.service.mappers.BuildInfoParser;
import com.report.service.models.Building;
import com.report.service.services.BuildingBatchService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildingBatchHandler extends BatchHandler {

    private final ExecutorService executorService;
    private final BuildingBatchService buildingBatchService;
    private static final Logger logger = Logger.getLogger(BuildingBatchHandler.class.getName());

    public BuildingBatchHandler(ExecutorService executorService, BuildingBatchService buildingBatchService) {
        this.executorService = executorService;
        this.buildingBatchService = buildingBatchService;
    }

    @Override
    public void handleBatch(List<Building> batch) {
        Future<?> future = executorService.submit(() -> buildingBatchService.processBatch(batch));
        try {
            future.get();  // Wait for the batch processing to complete
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.WARNING,"Error occurred while processing batch  "+e.getMessage());
        }
    }

    @Override
    public void processFile(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<Building> batch = new ArrayList<>();
            BuildInfoParser parser = new BuildInfoParser();

            while ((line = reader.readLine()) != null) {
                Building building = parser.parseLine(line);
                batch.add(building);

                if (batch.size() >= Constants.BATCH_SIZE) {
                    handleBatch(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                handleBatch(batch);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error occurred while file parsing "+e.getMessage());
        }
    }

    @Override
    public void generateReport() {
        buildingBatchService.generateReport();

    }
}
