package io.jayms.dbsc.server.service;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.service.ReportService;
import io.jayms.dbsc.interfaces.spreadsheet.SpreadsheetGenerator;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/report")
public class ReportEndpoint implements ReportService {

    private static final Logger LOG = Logger.getLogger(DatabaseEndpoint.class.getName());

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private SpreadsheetRepository spreadsheetRepository;

    @Inject
    private SpreadsheetGenerator spreadsheetGenerator;

    @Override
    public List<Report> listAllReports() {
        LOG.log(Level.FINE, "REST request to get all Reports");
        return reportRepository.findAll();
    }

    @Override
    @Path("/{reportId}/spreadsheet")
    public List<Spreadsheet> getAllSpreadsheets(@PathParam("reportId") long reportId) {
        return spreadsheetRepository.findAll(reportId);
    }

    @Override
    @Path("/{id}")
    public Report getReport(@PathParam("id") long id) {
        Report db = reportRepository.find(id);
        LOG.log(Level.FINE, "REST request to get Report : {0}", db);
        return db;
    }

    @Override
    public Response create(Report report) {
        LOG.log(Level.FINE, "REST request to save Report : {0}", report);
        reportRepository.create(report);
        return Response.ok().build();
    }

    @Override
    public Response update(Report report) {
        LOG.log(Level.FINE, "REST request to update Report : {0}", report);
        reportRepository.update(report);
        return Response.ok().build();
    }

    @Override
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        LOG.log(Level.FINE, "REST request to delete Report : {0}", id);
        reportRepository.delete(id);
        return Response.ok().build();
    }

    @Override
    @Path("/{id}")
    public Response execute(@PathParam("id") long id) {
        Report report = reportRepository.find(id);
        Spreadsheet spreadsheet = spreadsheetGenerator.generate(report);

        return Response.accepted().build();
    }

    @Override
    @Path("/{id}")
    public Response schedule(@PathParam("id") long id) {
        return Response.ok().build();
    }

}

