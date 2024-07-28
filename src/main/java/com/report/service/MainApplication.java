package com.report.service;

import com.report.service.constants.Constants;
import com.report.service.processors.BatchProcessor;
public class MainApplication {


    public static void main(String[] args) {
        String file = System.getProperty(Constants.USER_DIR).concat(Constants.RESOURCE_PATH).concat(Constants.INPUT_FILE_NAME);
        BatchProcessor processor = new BatchProcessor();
        processor.startProcess(file, Constants.BUILDING);
    }

}
