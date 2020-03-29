package io.jayms.dbsc.server.model;

import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;

public class ReportEntity implements Report {

	private long id;
	private String name;
	private String query;
	private String destination;
	private Instant lastExecutionTime;
	private Duration executionInterval;

	private Database database;
	private Set<Spreadsheet> spreadsheets;
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String getDestination() {
		return destination;
	}

	@Override
	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public Instant getLastExecutionTime() {
		return lastExecutionTime;
	}

	@Override
	public void setLastExecutionTime(Instant lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	@Override
	public Duration getExecutionInterval() {
		return executionInterval;
	}

	@Override
	public void setExecutionInterval(Duration executionInterval) {
		this.executionInterval = executionInterval;
	}

	@Override
	public Database getDatabase() {
		return database;
	}

	@Override
	public void setDatabase(Database database) {
		this.database = database;
	}

	@Override
	public Set<Spreadsheet> getSpreadsheets() {
		return spreadsheets;
	}

	@Override
	public void setSpreadsheets(Set<Spreadsheet> spreadsheets) {
		this.spreadsheets = spreadsheets;
	}
}
