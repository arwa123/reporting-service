package com.report.service.processors;
import com.report.service.mapper.BuildInfoParser;
import com.report.service.model.Building;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class BatchProcessor {
        private static final int BATCH_SIZE = 10;

        public interface BatchHandler {
            void handleBatch(List<Building> batch);
        }

        public void processFile(String filePath, BatchHandler handler) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                List<Building> batch = new ArrayList<>();
                BuildInfoParser parser = new BuildInfoParser();

                while ((line = reader.readLine()) != null) {
                    Building building = parser.parseLine(line);
                    batch.add(building);

                    if (batch.size() >= BATCH_SIZE) {
                        handler.handleBatch(batch);
                        batch.clear();
                    }
                }

                if (!batch.isEmpty()) {
                    handler.handleBatch(batch);
                }
            }
        }
    }
