package com.globits.resourceserver.hr.configs;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
//		// Core
		"com.globits.core.domain", "com.globits.core.auditing", "com.globits.core.service.impl",
		// Security
		"com.globits.security.domain", "com.globits.security.service.impl", "com.globits.security.service.impl",
		"com.globits.security.service",
//		//HR
		"com.globits.hr.domain", "com.globits.hr.service", "com.globits.hr.service.impl",
		"com.globits.hr.timesheet.domain", "com.globits.hr.timesheet.service", "com.globits.hr.timesheet.service.impl",
		"com.globits.hr.exercise.domain", "com.globits.hr.exercise.service", "com.globits.hr.exercise.service.impl",

		"com.globits.resourceserver.sample.model", "com.globits.resourceserver.hr.service",
		"com.globits.resourceserver.hr.service.impl", "com.globits.resourceserver.sample.service",
		"com.globits.resourceserver.sample.service.impl" })

@EnableJpaRepositories(basePackages = { "com.globits.hr.repository", "com.globits.hr.timesheet.repository",
		"com.globits.hr.exercise.repository", "com.globits.core.repository", "com.globits.security.repository",
		"com.globits.resourceserver.sample.dao" })
public class DatabaseConfig {

	@Autowired
	private Environment env;

	@Value("classpath:schema.sql")
	private Resource schemaScript;

//	@Value("classpath:mysql_data.sql")
//	private Resource dataScript;

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		if (schemaScript.exists() && schemaScript.isReadable()) {
			populator.addScript(schemaScript);
		}
//		populator.addScript(dataScript);
		return populator;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator());
		return initializer;
	}

	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		EntityManagerFactory factory = entityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.TRUE);

		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		// factory.setPackagesToScan("com.globits.resourceserver.auth0.model","com.globits.hr.domain","com.globits.hr.timesheet.domain","com.globits.core.domain",
		// "com.globits.security.domain");
		factory.setPackagesToScan("com.globits.resourceserver.sample.model", "com.globits.hr.domain",
				"com.globits.hr.timesheet.domain", "com.globits.hr.exercise.domain", "com.globits.core.domain", "com.globits.security.domain");

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		jpaProperties.put("hibernate.max_fetch_depth",
				env.getProperty("spring.jpa.properties.hibernate.max_fetch_depth"));
		jpaProperties.put("hibernate.jdbc.fetch_size",
				env.getProperty("spring.jpa.properties.hibernate.jdbc.fetch_size"));
		jpaProperties.put("hibernate.jdbc.batch_size",
				env.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.ddl-auto"));
		jpaProperties.put("hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show-sql"));
		jpaProperties.put("hibernate.c3p0.min_size", env.getProperty("spring.jpa.properties.hibernate.c3p0.min_size"));
		jpaProperties.put("hibernate.c3p0.max_size", env.getProperty("spring.jpa.properties.hibernate.c3p0.max_size"));
		jpaProperties.put("hibernate.c3p0.timeout", env.getProperty("spring.jpa.properties.hibernate.c3p0.timeout"));
		jpaProperties.put("hibernate.c3p0.max_statements",
				env.getProperty("spring.jpa.properties.hibernate.c3p0.max_statements"));

		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

		return factory;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

}
