package com.application.config;

import com.application.common.CassandraSessionFactory;
import com.datastax.driver.core.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

		cluster.setContactPoints(Objects.requireNonNull(env.getProperty("cassandra.contactpoints")));
		cluster.setKeyspaceCreations(getKeyspaceCreations());
		cluster.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("cassandra.port"))));
		cluster.setJmxReportingEnabled(false);
		return cluster;
	}

	@Override
	@Bean
	public CassandraSessionFactory session() {
		CassandraSessionFactory session = new CassandraSessionFactory();
		Cluster cluster = cluster().getObject();
		System.out.println("Cluster value: " + cluster);
		assert cluster != null;
		session.setCluster(cluster);
		session.setConverter(cassandraConverter());
		session.setKeyspaceName(getKeyspaceName());
		return session;
	}

	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		CreateKeyspaceSpecification keyspaceSpecification = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).withSimpleReplication(1).ifNotExists();
		List<CreateKeyspaceSpecification> keyspaces = new ArrayList<>();
		keyspaces.add(keyspaceSpecification);
		return keyspaces;
	}

	@Override
	protected String getKeyspaceName() {
		return env.getProperty("cassandra.keyspace");
	}

	@Override
	public String[] getEntityBasePackages() {
		return env.getProperty("cassandra.entitypackages").split(",");
	}

}
