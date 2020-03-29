package io.jayms.dbsc.server.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.repository.ConnectionSpecRepository;
import io.jayms.dbsc.interfaces.repository.DatabaseRepository;
import io.jayms.dbsc.interfaces.repository.ReportRepository;
import io.jayms.dbsc.interfaces.service.ConnectionSpecService;

@Path("/connSpec")
public class ConnectionSpecEndpoint implements ConnectionSpecService {

	private static final Logger LOG = Logger.getLogger(ConnectionSpecEndpoint.class.getName());
	
	@Inject
	private ConnectionSpecRepository connSpecRepo;

	@Inject
	private DatabaseRepository dbRepo;
	
	@Override
	public List<ConnectionSpec> listAllConnSpecs() {
		LOG.log(Level.FINE, "REST request to get all Database");
		return connSpecRepo.findAll();
	}

	@Override
	@Path("/{id}")
	public ConnectionSpec getConnSpec(@PathParam("id") long id) {
		ConnectionSpec spec = connSpecRepo.find(id);
		LOG.log(Level.FINE, "REST request to get ConnectionSpec : {0}", spec);
		return spec;
	}

	@Override
	@Path("/{connSpecId}/dbs")
	public List<Database> getAllDatabases(@PathParam("connSpecId") long connSpecId) {
		List<Database> databases = dbRepo.findAll(connSpecId);
		LOG.log(Level.FINE, "REST request to get all Databases for Connection Spec {0}", connSpecId);
		return databases;
	}

	@Override
	public Response create(ConnectionSpec spec) {
		LOG.log(Level.FINE, "REST request to save ConnectionSpec : {0}", spec);
		connSpecRepo.create(spec);
		return Response.ok().build();
	}

	@Override
	public Response update(ConnectionSpec spec) {
		LOG.log(Level.FINE, "REST request to update ConnectionSpec : {0}", spec);
		connSpecRepo.update(spec);
		return Response.ok().build();
	}

	@Override
	@Path("/{id}")
	public Response delete(@PathParam("id") long id) {
		LOG.log(Level.FINE, "REST request to delete ConnectionSpec : {0}", id);
		connSpecRepo.delete(id);
		return Response.ok().build();
	}

}
