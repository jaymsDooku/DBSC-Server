package io.jayms.dbsc.server.model;

import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;

public class SpreadsheetEntity implements Spreadsheet {

    private long id;
    private String location;
    private Report report;
    private Instant creation;

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public File getFile() {
        return new File(location);
    }

    @Override
    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Report getReport() {
        return report;
    }

    @Override
    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    @Override
    public Instant getCreation() {
        return creation;
    }
}
