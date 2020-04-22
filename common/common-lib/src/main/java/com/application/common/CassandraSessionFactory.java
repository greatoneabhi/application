package com.application.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CqlStringUtils;
import org.springframework.data.cassandra.core.mapping.CassandraPersistentEntity;
import org.springframework.data.cassandra.core.mapping.CassandraPersistentProperty;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.mapping.PropertyHandler;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.TableMetadata;

public class CassandraSessionFactory extends CassandraSessionFactoryBean {

    @Override
    public void afterPropertiesSet() throws Exception {

        super.afterPropertiesSet();

        //Assert.notNull(getConverter());

        //admin = new CassandraAdminTemplate(getSession(), getConverter());

        schemaSync();
    }

    private void schemaSync() {
        Collection<? extends CassandraPersistentEntity<?>> entities = getConverter()
                .getMappingContext().getTableEntities();
        for (CassandraPersistentEntity<?> entity : entities) {
            final Optional<TableMetadata> tableMetadata = getCassandraAdminOperations().getTableMetadata(
                    getKeyspaceName(), entity.getTableName());
            final List<String> queryList = alterTable(entity.getTableName().toString(),
                    entity, tableMetadata);
            if(!queryList.isEmpty()) {
                for(String query : queryList) {
                    getCassandraAdminOperations().getCqlOperations().execute(query);
                }
            }
            getCassandraAdminOperations().createTable(true, entity.getTableName(), entity.getType(), null);
        }
    }

    private static List<String> alterTable(
            final String tableName,
            final CassandraPersistentEntity<?> entity,
            final Optional<TableMetadata> tableMetadata) {
        final List<String> result = new ArrayList<String>();

        if(!tableMetadata.isPresent()) {
            return result;
        }
        entity.doWithProperties(new PropertyHandler<CassandraPersistentProperty>() {
            @Override
            public void doWithPersistentProperty(CassandraPersistentProperty prop) {

                if(prop.isAnnotationPresent(PrimaryKeyClass.class) || prop.isAnnotationPresent(PrimaryKey.class)) {
                    return;
                }

                String columnName = prop.getColumnName().toCql();
                DataType columnDataType = prop.getDataType();
                ColumnMetadata columnMetadata = tableMetadata.get().getColumn(columnName.toLowerCase());

                if (columnMetadata != null && columnDataType.equals(columnMetadata.getType())) {
                    return;
                }

                final StringBuilder str = new StringBuilder();
                str.append("ALTER TABLE ");
                str.append(tableName);
                if (columnMetadata == null) {
                    str.append(" ADD ");
                } else {
                    str.append(" ALTER ");
                }

                str.append(columnName);
                str.append(' ');

                if (columnMetadata != null) {
                    str.append("TYPE ");
                }

                str.append(CqlStringUtils.escapeSingle(columnDataType));

                str.append(';');
                result.add(str.toString());

            }
        });

        return result;
    }
}
