package br.com.authspringsecurity.db.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "datasource", ignoreUnknownFields = true)
public class PropertiesDb {
	
	private String database;
    private String driverClassName;
    private boolean formatSql = false;
    private String mappingResources;
    private String password;
    private boolean showSql = false;
    private String url;
    private String username;
    
    
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public boolean isFormatSql() {
		return formatSql;
	}
	public void setFormatSql(boolean formatSql) {
		this.formatSql = formatSql;
	}
	public String getMappingResources() {
		return mappingResources;
	}
	public void setMappingResources(String mappingResources) {
		this.mappingResources = mappingResources;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isShowSql() {
		return showSql;
	}
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    
}
