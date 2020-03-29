package io.jayms.dbsc.server.service;

import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.service.SpreadsheetService;
import io.jayms.dbsc.interfaces.model.Spreadsheet;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/spreadsheet")
public class SpreadsheetEndpoint implements SpreadsheetService {

    @Inject
    private SpreadsheetRepository spreadsheetRepository;

    @Override
    public List<Spreadsheet> listAll() {
        return spreadsheetRepository.findAll();
    }

    @Override
    @Path("/{id}")
    public Spreadsheet getSpreadsheet(@PathParam("id") long id) {
        return spreadsheetRepository.find(id);
    }

    @Override
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        spreadsheetRepository.delete(id);
        return Response.ok().build();
    }
}
