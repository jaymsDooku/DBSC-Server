package io.jayms.dbsc.server.service;

import io.jayms.dbsc.interfaces.model.EntityTools;
import io.jayms.dbsc.interfaces.repository.SpreadsheetRepository;
import io.jayms.dbsc.interfaces.service.SpreadsheetService;
import io.jayms.dbsc.interfaces.model.Spreadsheet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/spreadsheet")
@Produces({MediaType.APPLICATION_JSON})
public class SpreadsheetEndpoint implements SpreadsheetService {

    @Inject
    private SpreadsheetRepository spreadsheetRepository;

    @Override
    @GET
    public Response listAllSpreadsheets() {
        List<Spreadsheet> spreadsheets = spreadsheetRepository.findAll();
        List<JsonObject> jsonObjects = EntityTools.toJson(spreadsheets);
        return Response.ok(jsonObjects).build();
    }

    @Override
    @Path("/{id}")
    @GET
    public Response getSpreadsheet(@PathParam("id") long id) {
        Spreadsheet spreadsheet = spreadsheetRepository.find(id);
        JsonObject jsonObject = spreadsheet.toJson();
        return Response.ok(jsonObject).build();
    }

    @Override
    @Path("/delete/{id}")
    @DELETE
    public Response delete(@PathParam("id") long id) {
        spreadsheetRepository.delete(id);
        return Response.ok().build();
    }
}
