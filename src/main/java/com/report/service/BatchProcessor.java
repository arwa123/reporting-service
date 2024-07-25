package com.report.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class BatchProcessor {
        private static final int BATCH_SIZE = 1000;

        public interface BatchHandler {
            void handleBatch(List<BuildInfo> batch);
        }

        public void processFile(String filePath, BatchHandler handler) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                List<BuildInfo> batch = new ArrayList<>();
                BuildInfoParser parser = new BuildInfoParser();

                while ((line = reader.readLine()) != null) {
                    BuildInfo buildInfo = parser.parseLine(line);
                    batch.add(buildInfo);

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
}
