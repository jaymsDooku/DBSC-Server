package io.jayms.dbsc.server.spreadsheet;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.spreadsheet.SpreadsheetGenerator;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ReportScheduler {

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private SpreadsheetRepository spreadsheetRepository;

    @Inject
    private SpreadsheetGenerator spreadsheetGenerator;

    @Resource
    private ManagedScheduledExecutorService managedScheduledExecutorService;

    private Map<Long, ScheduledFuture<?>> scheduledReports = new HashMap<>();

    public void scheduleReport(Report report) {
        if (scheduledReports.containsKey(report.getId())) {
            return;
        }

        ReportExecutionRunnable reportExecutionRunnable = new ReportExecutionRunnable(spreadsheetGenerator, reportRepository, spreadsheetRepository, report);
        Duration reportExecutionInterval = report.getExecutionInterval();
        ScheduledFuture<?> scheduledFuture = managedScheduledExecutorService.scheduleAtFixedRate(reportExecutionRunnable, 0, reportExecutionInterval.toMillis(), TimeUnit.MILLISECONDS);
        scheduledReports.put(report.getId(), scheduledFuture);
    }

    public boolean stopSchedulingReport(Report report) {
        ScheduledFuture<?> scheduledFuture = scheduledReports.remove(report.getId());
        return scheduledFuture.cancel(true);
    }

    public Collection<Long> getScheduledReports() {
        return scheduledReports.keySet();
    }

}
