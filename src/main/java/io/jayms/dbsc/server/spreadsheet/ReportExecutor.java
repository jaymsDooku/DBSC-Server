package io.jayms.dbsc.server.spreadsheet;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.spreadsheet.SpreadsheetGenerator;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

@Stateless
public class ReportExecutor {

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private SpreadsheetRepository spreadsheetRepository;

    @Inject
    private SpreadsheetGenerator spreadsheetGenerator;

    @Asynchronous
    public void executeReport(Report report) {
        new ReportExecutionRunnable(spreadsheetGenerator, reportRepository, spreadsheetRepository, report).run();
    }

}
