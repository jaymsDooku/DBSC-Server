package io.jayms.dbsc.server.service;

import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.EntityTools;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.repository.DatabaseRepository;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.service.ReportService;
import io.jayms.dbsc.interfaces.spreadsheet.SpreadsheetGenerator;
import io.jayms.dbsc.server.spreadsheet.ReportExecutor;
import io.jayms.dbsc.server.spreadsheet.ReportScheduler;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/report")
@Produces({MediaType.APPLICATION_JSON})
public class ReportEndpoint implements ReportService {

    private static final Logger LOG = Logger.getLogger(DatabaseEndpoint.class.getName());

    @Inject
    private DatabaseRepository databaseRepository;

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private SpreadsheetRepository spreadsheetRepository;

    @Inject
    private ReportExecutor reportExecutor;

    @Inject
    private ReportScheduler reportScheduler;

    @Override
    @GET
    public Response listAllReports() {
        LOG.log(Level.FINE, "REST request to get all Reports");
        List<Report> reports = reportRepository.findAll();
        List<JsonObject> jsonObjects = EntityTools.toJson(reports);
        return Response.ok(jsonObjects).build();
    }

    @Override
    @Path("/{reportId}/spreadsheet")
    @GET
    public Response getAllSpreadsheets(@PathParam("reportId") long reportId) {
        List<Spreadsheet> spreadsheets = spreadsheetRepository.findAll(reportId);
        List<JsonObject> jsonObjects = EntityTools.toJson(spreadsheets);
        return Response.ok(jsonObjects).build();
    }

    @Override
    @Path("/{id}")
    @GET
    public Response getReport(@PathParam("id") long id) {
        Report report = reportRepository.find(id);
        JsonObject jsonObject = report.toJson();
        LOG.log(Level.FINE, "REST request to get Report : {0}", report);
        return Response.ok(jsonObject).build();
    }

    @Override
    @POST
    public Response create(JsonObject reportJson) {
        //LOG.log(Level.FINE, "REST request to save Report : {0}", report);
        Report report = new Report();
        report.setName(reportJson.getString(Report.NAME));
        report.setQuery(reportJson.getString(Report.QUERY));
        report.setDestination(reportJson.getString(Report.DESTINATION));

        long lastExecutionTimeEpochMillis = reportJson.getJsonNumber(Report.LAST_EXECUTION_TIME).longValue();
        Instant lastExecutionTime = Instant.ofEpochMilli(lastExecutionTimeEpochMillis);
        report.setLastExecutionTime(lastExecutionTime);

        long executionIntervalMillis = reportJson.getJsonNumber(Report.EXECUTION_INTERVAL).longValue();
        Duration executionInterval = Duration.ofMillis(executionIntervalMillis);
        report.setExecutionInterval(executionInterval);

        long dbId = reportJson.getJsonNumber(Report.DB).longValue();
        Database database = databaseRepository.find(dbId);
        report.setDatabase(database);
        database.getReports().add(report);

        reportRepository.create(report);

        JsonObject jsonObject = report.toJson();
        return Response.ok(jsonObject).build();
    }

    @Override
    @PUT
    public Response update(Report report) {
        LOG.log(Level.FINE, "REST request to update Report : {0}", report);
        reportRepository.update(report);
        return Response.ok().build();
    }

    @Override
    @Path("/delete/{id}")
    @DELETE
    public Response delete(@PathParam("id") long id) {
        LOG.log(Level.FINE, "REST request to delete Report : {0}", id);
        reportRepository.delete(id);
        return Response.ok().build();
    }

    @Override
    @Path("/execute/{id}")
    @GET
    public Response execute(@PathParam("id") long id) {
        Report report = reportRepository.find(id);
        System.out.println("REST request to execute Report: " + id);
        reportExecutor.executeReport(report);
        return Response.accepted().build();
    }

    @Override
    @Path("/schedule")
    @GET
    public Response getScheduledReports() {
        Collection<Long> scheduledReports = reportScheduler.getScheduledReports();
        System.out.println("REST request to get all scheduled reports.");
        return Response.ok(scheduledReports).build();
    }

    @Override
    @Path("/schedule/{id}")
    @GET
    public Response schedule(@PathParam("id") long id) {
        Report report = reportRepository.find(id);
        System.out.println("REST request to schedule Report: " + id);
        reportScheduler.scheduleReport(report);
        return Response.ok().build();
    }

    @Override
    @Path("/stopScheduling/{id}")
    @GET
    public Response stopScheduling(@PathParam("id") long id) {
        Report report = reportRepository.find(id);
        System.out.println("REST request to stop scheduling of Report: " + id);
        reportScheduler.stopSchedulingReport(report);
        return Response.ok().build();
    }
}

