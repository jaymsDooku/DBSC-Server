package io.jayms.dbsc.server.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jayms.dbsc.interfaces.model.ConnectionSpec;
import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.DatabaseType;
import io.jayms.dbsc.interfaces.model.Report;

public class DatabaseEntity implements Database {

	private long id;
	private String name;
	private int port;
	private String username;
	private String password;
	private DatabaseType databaseType;
	private String databasePath;
	private ConnectionSpec connSpec;
	private Set<Report> reports;

	private HikariConfig hikariConfig;
	private HikariDataSource hikariDataSource;
	
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
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public DatabaseType getType() {
		return databaseType;
	}

	@Override
	public void setType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	@Override
	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	@Override
	public String getDatabasePath() {
		return databasePath;
	}

	@Override
	public ConnectionSpec getConnectionSpec() {
		return connSpec;
	}

	@Override
	public void setConnectionSpec(ConnectionSpec connSpec) {
		this.connSpec = connSpec;
	}

	@Override
	public Set<Report> getReports() {
		return reports;
	}

	@Override
	public void setReports(Set<Report> reports) {
		this.reports = reports;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (hikariDataSource == null) {
			Properties properties = new Properties();
			properties.setProperty("dataSourceClassName", databaseType.getDataSourceClassName());
			properties.setProperty("dataSource.user", username);
			properties.setProperty("dataSource.password", password);
			properties.setProperty("dataSource.databaseName", name);
			properties.setProperty("dataSource.portNumber", Integer.toString(port));
			properties.setProperty("dataSource.serverName", connSpec.getHostname());

			hikariConfig = new HikariConfig(properties);
			hikariDataSource = new HikariDataSource(hikariConfig);
		}
		return hikariDataSource.getConnection();
	}
}
