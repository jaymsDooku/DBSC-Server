package io.jayms.dbsc.server.service;

import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.repository.ConnectionSpecRepository;
import io.jayms.dbsc.interfaces.repository.DatabaseRepository;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.service.DatabaseService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/db")
public class DatabaseEndpoint implements DatabaseService {

    private static final Logger LOG = Logger.getLogger(DatabaseEndpoint.class.getName());

    @Inject
    private DatabaseRepository repo;

    @Inject
    private ReportRepository reportRepo;

    @Override
    public List<Database> listAll() {
        LOG.log(Level.FINE, "REST request to get all Database");
        return repo.findAll();
    }

    @Override
    @Path("/{id}")
    public Database getDatabase(@PathParam("id") long id) {
        Database db = repo.find(id);
        LOG.log(Level.FINE, "REST request to get Database : {0}", db);
        return db;
    }

    @Override
    @Path("/{databaseId}/reports")
    public List<Report> getAllReports(@PathParam("databaseId") long databaseId) {
        List<Report> reports = reportRepo.findAll(databaseId);
        LOG.log(Level.FINE, "REST request to get all Reports for Database {0}", databaseId);
        return reports;
    }

    @Override
    public Response create(Database db) {
        LOG.log(Level.FINE, "REST request to save Database : {0}", db);
        repo.create(db);
        return Response.ok().build();
    }

    @Override
    public Response update(Database db) {
        LOG.log(Level.FINE, "REST request to update Database : {0}", db);
        repo.update(db);
        return Response.ok().build();
    }

    @Override
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        LOG.log(Level.FINE, "REST request to delete Database : {0}", id);
        repo.delete(id);
        return Response.ok().build();
    }

}
