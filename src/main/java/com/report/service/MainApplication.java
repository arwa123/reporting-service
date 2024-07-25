package com.report.service;

import com.report.service.processors.BatchProcessor;
import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) throws IOException {
       BatchProcessor processor = new BatchProcessor();
       processor.startProcess();
    }

}
