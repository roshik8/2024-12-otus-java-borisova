package ru.otus.jdbc.mapper;

import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.name = clazz.getSimpleName().toLowerCase(); // имя таблицы — маленькими
        this.constructor = findNoArgsConstructor();
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = findIdField();
        this.fieldsWithoutId = allFields.stream()
                .filter(f -> !f.equals(idField))
                .collect(Collectors.toList());
    }

    private Constructor<T> findNoArgsConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No default constructor found in class: " + clazz.getName(), e);
        }
    }

    private Field findIdField() {
        return allFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No field with @Id in class: " + clazz.getName()));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}