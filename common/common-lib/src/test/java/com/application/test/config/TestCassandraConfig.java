package com.application.test.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.application.config.CassandraConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class TestCassandraConfig {

    @InjectMocks
    CassandraConfig config;

    @Mock
    Environment env;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testClusterBeanCreation() {
        Mockito.when(env.getProperty(Matchers.anyString()))
        .thenReturn("environmentVariables");
        Mockito.when(env.getProperty("cassandra.port")).thenReturn("9042");
        CassandraClusterFactoryBean cluster = config.cluster();
        Assert.assertNotNull(cluster);
    }
}
