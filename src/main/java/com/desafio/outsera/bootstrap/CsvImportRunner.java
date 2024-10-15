package com.desafio.outsera.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.desafio.outsera.service.CsvImportService;

@Component
@Profile("!test")
public class CsvImportRunner implements CommandLineRunner {

    private final CsvImportService csvImportService;

    public CsvImportRunner(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @Override
    public void run(String... args) {
        csvImportService.importCsv();
    }
}


