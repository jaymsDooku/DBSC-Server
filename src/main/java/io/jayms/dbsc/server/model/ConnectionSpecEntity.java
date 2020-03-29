package io.jayms.dbsc.server.model;

import java.util.Set;

import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.model.Database;

public class ConnectionSpecEntity implements ConnectionSpec {

	private long id;
	private String hostname;
	private Set<Database> databases;
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getHostname() {
		return hostname;
	}
	
	@Override
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public Set<Database> getDatabases() {
		return databases;
	}
	
	@Override
	public void setDatabases(Set<Database> databases) {
		this.databases = databases;
	}
	
}
