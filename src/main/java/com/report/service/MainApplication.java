package com.report.service;

import com.report.service.constants.Constants;
import com.report.service.processors.BatchProcessor;

import java.io.IOException;

public class MainApplication {


    public static void main(String[] args) throws IOException {
        String file = System.getProperty("user.dir").concat(Constants.RESOURCE_PATH).concat("input.txt");
        BatchProcessor processor = new BatchProcessor();
        processor.startProcess(file);
    }

}
