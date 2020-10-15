package io.jayms.dbsc.server.service;

import io.jayms.dbsc.interfaces.model.*;
import io.jayms.dbsc.interfaces.repository.ConnectionSpecRepository;
import io.jayms.dbsc.interfaces.repository.DatabaseRepository;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.service.DatabaseService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/db")
@Produces({MediaType.APPLICATION_JSON})
public class DatabaseEndpoint implements DatabaseService {

    private static final Logger LOG = Logger.getLogger(DatabaseEndpoint.class.getName());

    @Inject
    private ConnectionSpecRepository connectionSpecRepository;

    @Inject
    private DatabaseRepository databaseRepository;

    @Inject
    private ReportRepository reportRepo;

    @Override
    @GET
    public Response listAllDatabases() {
        LOG.log(Level.FINE, "REST request to get all Database");
        List<Database> databases = databaseRepository.findAll();
        List<JsonObject> jsonObjects = EntityTools.toJson(databases);
        return Response.ok(jsonObjects).build();
    }

    @Override
    @Path("/{id}")
    @GET
    public Response getDatabase(@PathParam("id") long id) {
        Database db = databaseRepository.find(id);
        JsonObject jsonObject = db.toJson();
        LOG.log(Level.FINE, "REST request to get Database : {0}", db);
        return Response.ok(jsonObject).build();
    }

    @Override
    @Path("/{databaseId}/reports")
    @GET
    public Response getAllReports(@PathParam("databaseId") long databaseId) {
        List<Report> reports = reportRepo.findAll(databaseId);
        List<JsonObject> jsonObjects = EntityTools.toJson(reports);
        LOG.log(Level.FINE, "REST request to get all Reports for Database {0}", databaseId);
        return Response.ok(jsonObjects).build();
    }

    @Override
    @POST
    public Response create(JsonObject databaseJson) {
        //LOG.log(Level.FINE, "REST request to save Database : {0}", db);
        Database db = new Database();
        db.setName(databaseJson.getString(Database.NAME));
        db.setUsername(databaseJson.getString(Database.USERNAME));
        db.setPassword(databaseJson.getString(Database.PASSWORD));
        db.setPort(databaseJson.getInt(Database.PORT));
        db.setType(DatabaseType.valueOf(databaseJson.getString(Database.DATABASE_TYPE)));
        String dbPath = databaseJson.getString(Database.DATABASE_PATH);
        if (dbPath == "null") {
            dbPath = null;
        }
        db.setDatabasePath(dbPath);

        long connSpecId = databaseJson.getJsonNumber(Database.CONN_SPEC).longValue();
        ConnectionSpec connSpec = connectionSpecRepository.find(connSpecId);
        db.setConnectionSpec(connSpec);
        connSpec.getDatabases().add(db);

        databaseRepository.create(db);

        JsonObject jsonObject = db.toJson();
        return Response.ok(jsonObject).build();
    }

    @Override
    @PUT
    public Response update(Database db) {
        LOG.log(Level.FINE, "REST request to update Database : {0}", db);
        databaseRepository.update(db);
        return Response.ok().build();
    }

    @Override
    @Path("/delete/{id}")
    @DELETE
    public Response delete(@PathParam("id") long id) {
        LOG.log(Level.FINE, "REST request to delete Database : {0}", id);
        databaseRepository.delete(id);
        return Response.ok().build();
    }

}
