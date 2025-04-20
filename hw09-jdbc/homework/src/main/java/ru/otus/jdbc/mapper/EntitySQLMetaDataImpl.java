package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityMeta;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityMeta) {
        this.entityMeta = entityMeta;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityMeta.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + entityMeta.getName() +
                " WHERE " + entityMeta.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        var fields = entityMeta.getFieldsWithoutId();

        String columnNames = fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String placeholders = fields.stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));

        return "INSERT INTO " + entityMeta.getName() +
                " (" + columnNames + ") VALUES (" + placeholders + ")";
    }

    @Override
    public String getUpdateSql() {
        var fields = entityMeta.getFieldsWithoutId();

        String setClause = fields.stream()
                .map(f -> f.getName() + " = ?")
                .collect(Collectors.joining(", "));

        return "UPDATE " + entityMeta.getName() +
                " SET " + setClause +
                " WHERE " + entityMeta.getIdField().getName() + " = ?";
    }
}