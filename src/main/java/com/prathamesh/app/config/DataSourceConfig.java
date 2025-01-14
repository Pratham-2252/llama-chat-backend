package com.prathamesh.app.config;

import java.io.Serializable;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig implements Serializable {

	private static final long serialVersionUID = -7713315875320842650L;

	private final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	@Value("${spring.datasource.hikari.data-source-properties.cachePrepStmts}")
	private String cachePrepStmts;

	@Value("${spring.datasource.hikari.data-source-properties.prepStmtCacheSize}")
	private String prepStmtCacheSize;

	@Value("${spring.datasource.hikari.data-source-properties.prepStmtCacheSqlLimit}")
	private String prepStmtCacheSqlLimit;

	@Value("${spring.datasource.hikari.data-source-properties.useServerPrepStmts}")
	private String useServerPrepStmts;

	@Value("${spring.datasource.hikari.data-source-properties.useLocalSessionState}")
	private String useLocalSessionState;

	@Value("${spring.datasource.hikari.data-source-properties.rewriteBatchedStatements}")
	private String rewriteBatchedStatements;

	@Value("${spring.datasource.hikari.data-source-properties.cacheResultSetMetadata}")
	private String cacheResultSetMetadata;

	@Value("${spring.datasource.hikari.data-source-properties.cacheServerConfiguration}")
	private String cacheServerConfiguration;

	@Value("${spring.datasource.hikari.data-source-properties.elideSetAutoCommits}")
	private String elideSetAutoCommits;

	@Value("${spring.datasource.hikari.data-source-properties.maintainTimeStats}")
	private String maintainTimeStats;

	@Value("${spring.datasource.hikari.poolName}")
	private String poolName;

	@Value("${spring.datasource.hikari.connection-timeout}")
	private String connectionTimeout;

	@Value("${spring.datasource.hikari.idle-timeout}")
	private String idleTimeout;

	@Value("${spring.datasource.hikari.max-lifetime}")
	private String maxLifetime;

	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private String maxPoolSize;

	@Value("${spring.datasource.hikari.minimum-idle}")
	private String minIdle;

	@Bean
	DataSource dataSource() {
		logger.info("This is a data source url dataSourceUrl: {} ", this.dataSourceUrl);

		Properties dbProperties = new Properties();

		dbProperties.put("jdbcUrl", this.dataSourceUrl);
		dbProperties.put("user", this.username);
		dbProperties.put("password", this.password);
		dbProperties.put("driverClassName", this.driverClassName);
		dbProperties.put("cachePrepStmts", this.cachePrepStmts);
		dbProperties.put("prepStmtCacheSize", this.prepStmtCacheSize);
		dbProperties.put("prepStmtCacheSqlLimit", this.prepStmtCacheSqlLimit);
		dbProperties.put("useServerPrepStmts", this.useServerPrepStmts);
		dbProperties.put("useLocalSessionState", this.useLocalSessionState);
		dbProperties.put("rewriteBatchedStatements", this.rewriteBatchedStatements);
		dbProperties.put("cacheResultSetMetadata", this.cacheResultSetMetadata);
		dbProperties.put("cacheServerConfiguration", this.cacheServerConfiguration);
		dbProperties.put("elideSetAutoCommits", this.elideSetAutoCommits);
		dbProperties.put("maintainTimeStats", this.maintainTimeStats);

		Properties hikariProperties = new Properties();

		logger.info("this.jdbcUrl: {} ", this.dataSourceUrl);

		hikariProperties.put("jdbcUrl", this.dataSourceUrl);

		logger.info("this.driverClassName: {} ", this.driverClassName);

		hikariProperties.put("driverClassName", this.driverClassName);
		hikariProperties.put("poolName", this.poolName);
		hikariProperties.put("maximumPoolSize", this.maxPoolSize);
		hikariProperties.put("minimumIdle", this.minIdle);
		hikariProperties.put("connectionTimeout", this.connectionTimeout);
		hikariProperties.put("idleTimeout", this.idleTimeout);
		hikariProperties.put("dataSourceProperties", dbProperties);
		hikariProperties.put("maxLifetime", this.maxLifetime);

		HikariConfig hikariConfig = new HikariConfig(hikariProperties);

		return new HikariDataSource(hikariConfig);
	}

}
