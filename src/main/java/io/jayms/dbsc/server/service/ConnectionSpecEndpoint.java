package io.jayms.dbsc.server.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.EntityTools;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.repository.ConnectionSpecRepository;
import io.jayms.dbsc.interfaces.repository.DatabaseRepository;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.service.ConnectionSpecService;

@ApplicationScoped
@Path("/connSpec")
@Produces({MediaType.APPLICATION_JSON})
public class ConnectionSpecEndpoint implements ConnectionSpecService {

	private static final Logger LOG = Logger.getLogger(ConnectionSpecEndpoint.class.getName());
	
	@Inject
	private ConnectionSpecRepository connSpecRepo;

	@Inject
	private DatabaseRepository dbRepo;
	
	@Override
	@GET
	public Response listAllConnSpecs() {
		List<ConnectionSpec> connSpecs = connSpecRepo.findAll();
		List<JsonObject> jsonObjects = EntityTools.toJson(connSpecs);
		System.out.println("REST request to get all ConnectionSpec");
		return Response.ok(jsonObjects).build();
	}

	@Override
	@Path("/{id}")
	@GET
	public Response getConnSpec(@PathParam("id") long id) {
		ConnectionSpec spec = connSpecRepo.find(id);
		JsonObject jsonObject = spec.toJson();
		//LOG.log(Level.FINE, "REST request to get ConnectionSpec : {0}", spec);
		System.out.println("REST request to get ConnectionSpec : " + spec);
		return Response.ok(jsonObject).build();
	}

	@Override
	@Path("/{connSpecId}/dbs")
	@GET
	public Response getAllDatabases(@PathParam("connSpecId") long connSpecId) {
		List<Database> databases = dbRepo.findAll(connSpecId);
		List<JsonObject> jsonObjects = EntityTools.toJson(databases);
		//LOG.log(Level.FINE, "REST request to get all Databases for Connection Spec {0}", connSpecId);
		System.out.printf("REST request to get all Databases for Connection Spec {0}\n" , connSpecId);
		return Response.ok(jsonObjects).build();
	}

	@Override
	@POST
	public Response create(ConnectionSpec spec) {
		LOG.log(Level.FINE, "REST request to save ConnectionSpec : {0}", spec);
		connSpecRepo.create(spec);

		JsonObject jsonObject = spec.toJson();
		return Response.ok(jsonObject).build();
	}

	@Override
	@PUT
	public Response update(ConnectionSpec spec) {
		LOG.log(Level.FINE, "REST request to update ConnectionSpec : {0}", spec);
		connSpecRepo.update(spec);
		return Response.ok().build();
	}

	@Override
	@Path("/delete/{id}")
	@DELETE
	public Response delete(@PathParam("id") long id) {
		LOG.log(Level.FINE, "REST request to delete ConnectionSpec : {0}", id);
		connSpecRepo.delete(id);
		return Response.ok().build();
	}

}
