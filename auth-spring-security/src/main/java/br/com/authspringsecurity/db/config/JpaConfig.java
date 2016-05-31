package br.com.authspringsecurity.db.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import br.com.authspringsecurity.db.properties.PropertiesDb;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = JpaConfig.PACKAGES_TO_SCAN,
        transactionManagerRef = JpaConfig.TRANSACTION_MANAGER_NAME,
        entityManagerFactoryRef = JpaConfig.ENTITY_MANAGER_FACTORY_NAME
    )
public class JpaConfig {
	
	static final String TRANSACTION_MANAGER_NAME = "tm_security";
    public static final String ENTITY_MANAGER_FACTORY_NAME = "emf_security";

    static final String PACKAGES_TO_SCAN = "br.com.authspringsecurity.db";
    private static final String PERSISTENCE_UNIT_NAME = "em_security";
	
	@Autowired
	private PropertiesDb propertiesDb;
	
	@Bean
	public JpaVendorAdapter creataJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(getDatabaseVendor(propertiesDb));
        hibernateJpaVendorAdapter.setShowSql(propertiesDb.isShowSql());
        return hibernateJpaVendorAdapter;
    }

	@Bean
    public Properties createJpaProperties(PropertiesDb databaseProperties) {
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.format_sql", databaseProperties.isFormatSql());
        // jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        // jpaProperties.put("hibernate.cache.use_second_level_cache", true);
        // jpaProperties.put("hibernate.cache.use_query_cache", true);
        // jpaProperties.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        return jpaProperties;
    }

	@Bean
    public DataSource createDataSource() {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(propertiesDb.getDriverClassName());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        dataSource.setJdbcUrl(propertiesDb.getUrl());
        dataSource.setUser(propertiesDb.getUsername());
        dataSource.setPassword(propertiesDb.getPassword());

        // Quantidade inicial de conexões.
        dataSource.setInitialPoolSize(5);
        // Quantidade máxima de conexões.
        dataSource.setMaxPoolSize(10);
        // Quantidade mínima de conexões.
        dataSource.setMinPoolSize(5);

        dataSource.setAcquireIncrement(5);
        dataSource.setAcquireRetryAttempts(30);
        dataSource.setMaxStatements(0);
        dataSource.setMaxStatementsPerConnection(100);
        dataSource.setMaxIdleTime(180); // 3 minutos
        dataSource.setMaxConnectionAge(10); // 10 segundos
        dataSource.setNumHelperThreads(10);
        dataSource.setCheckoutTimeout(10000);
        
        dataSource.setPreferredTestQuery("SELECT 1");

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean createEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(createDataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(creataJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaDialect(new org.springframework.orm.jpa.vendor.HibernateJpaDialect());
        localContainerEntityManagerFactoryBean.setJpaProperties(createJpaProperties(propertiesDb));
        localContainerEntityManagerFactoryBean.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
        if (propertiesDb.getMappingResources() != null) {
            localContainerEntityManagerFactoryBean.setMappingResources(propertiesDb.getMappingResources());
        }
        // localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);

        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager createTransactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(createEntityManagerFactory().getObject());
        txManager.setDataSource(createDataSource());
        return txManager;
    }

    private Database getDatabaseVendor(PropertiesDb databaseProperties) {
        String database = databaseProperties.getDatabase();
        if ("SQLSERVER".equalsIgnoreCase(database)) {
            return Database.SQL_SERVER;
        } else if ("MYSQL".equalsIgnoreCase(database)) {
            return Database.MYSQL;
        } else {
            throw new RuntimeException("Não foi possível identificar a base de dados através do parâmetro \"database\" {\"" + database
                    + "\"}. Valores possíveis: \"" + Database.SQL_SERVER + "\" e \"" + Database.MYSQL + "\"");
        }
    }

}
