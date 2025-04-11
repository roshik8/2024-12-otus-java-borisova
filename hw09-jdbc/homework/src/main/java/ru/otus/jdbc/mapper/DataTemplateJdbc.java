package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> findById(Connection connection, long id) {
        return (Optional<T>) dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id),
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return Optional.of(createInstance(resultSet));
                        }
                        return Optional.empty();
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }
        ).orElse(Optional.empty());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var result = new ArrayList<T>();
            try {
                while (rs.next()) {
                    result.add(createInstance(rs));
                }
                return result;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElse((ArrayList<T>) Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(client));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(client));
            }
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            Object id = idField.get(client);
            params.add(id);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createInstance(ResultSet rs) throws Exception {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        T instance = constructor.newInstance();

        for (Field field : entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            Object value = rs.getObject(field.getName());
            field.set(instance, value);
        }

        return instance;
    }
}