package io.jayms.dbsc.server.spreadsheet;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.spreadsheet.SpreadsheetGenerator;

import java.time.Instant;

public class ReportExecutionRunnable implements Runnable {

    private SpreadsheetGenerator spreadsheetGenerator;
    private ReportRepository reportRepository;
    private SpreadsheetRepository spreadsheetRepository;
    private Report report;

    public ReportExecutionRunnable(SpreadsheetGenerator spreadsheetGenerator,
                                   ReportRepository reportRepository,
                                   SpreadsheetRepository spreadsheetRepository,
                                   Report report) {
        this.spreadsheetGenerator = spreadsheetGenerator;
        this.reportRepository = reportRepository;
        this.spreadsheetRepository = spreadsheetRepository;
        this.report = report;
    }

    @Override
    public void run() {
        Spreadsheet spreadsheet = spreadsheetGenerator.generate(report);
        spreadsheetRepository.create(spreadsheet);
        System.out.println("Generated spreadsheet for Report: " + report.getId());

        report.setLastExecutionTime(Instant.now());
        reportRepository.update(report);
        System.out.println("Updated Report's last execution time: " + report.getId());
    }
}
