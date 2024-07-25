package com.report.service;

import com.report.service.processors.BatchProcessor;

import java.io.IOException;

public class MainApplication {

    private static final String FILE_PATH = "/Users/asaify/Documents/my-workspace/reporting-service/src/main/resources/";

    public static void main(String[] args) throws IOException {
        String file = FILE_PATH.concat("input.txt");
        BatchProcessor processor = new BatchProcessor();
        processor.startProcess(file);
    }

}
