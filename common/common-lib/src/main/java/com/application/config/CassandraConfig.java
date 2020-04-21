package com.application.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.application.common.CassandraSessionFactory;
import com.datastax.driver.core.Cluster;

/**
 * @author abhishek
 *
 */
@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
@EnableCassandraRepositories(basePackages = { "com.application" })
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Autowired
	private Environment env;

	@Override
	@Bean
	public CassandraClusterFactoryBean cluster() {

		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();

		cluster.setContactPoints(env.getProperty("cassandra.contactpoints"));
		cluster.setKeyspaceCreations(getKeyspaceCreations());
		cluster.setPort(Integer.parseInt(env.getProperty("cassandra.port")));

		return cluster;
	}

	@Override
	@Bean
	public CassandraSessionFactory session() throws ClassNotFoundException {
		CassandraSessionFactory session = new CassandraSessionFactory();
		Cluster cluster = cluster().getObject();
		System.out.println("Cluster value: " + cluster);
		session.setCluster(cluster);
		session.setConverter(cassandraConverter());
		session.setKeyspaceName(getKeyspaceName());
		return session;
	}

	/*
	 * @Override
	 * 
	 * @Bean public CassandraSessionFactoryBean session() throws
	 * ClassNotFoundException { CassandraSessionFactoryBean session = new
	 * CassandraSessionFactoryBean(); Cluster cluster = cluster().getObject();
	 * System.out.println("Cluster value: "+cluster); session.setCluster(cluster);
	 * session.setConverter(cassandraConverter());
	 * session.setKeyspaceName(getKeyspaceName());
	 * session.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS); return session; }
	 */

	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		CreateKeyspaceSpecification keyspaceSpecification = CreateKeyspaceSpecification.createKeyspace()
				.name(getKeyspaceName()).withSimpleReplication(1).ifNotExists();
		List<CreateKeyspaceSpecification> keyspaces = new ArrayList<CreateKeyspaceSpecification>();
		keyspaces.add(keyspaceSpecification);
		return keyspaces;
	}

	@Override
	protected String getKeyspaceName() {
		return env.getProperty("cassandra.keyspace");
	}

	@Override
	public String[] getEntityBasePackages() {
		String[] entityBasePackages = env.getProperty("cassandra.entitypackages").split(",");
		return entityBasePackages;
	}

}
