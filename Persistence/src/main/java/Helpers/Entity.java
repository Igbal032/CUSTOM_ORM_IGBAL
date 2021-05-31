package Helpers;

import Annotation.MyColumn;
import Annotation.MyEntity;
import Annotation.MyId;
import Services.QueryBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Entity {

    private Class<?> entityClass;
    private Object entityObject;

    public Entity(Class entityClass) {
        if (entityClass.getAnnotation(MyEntity.class) != null) {
            this.entityClass = entityClass;
            try {
                this.entityObject = entityClass.newInstance();
                if (!Table.isTableExist(this.getModelAnnotation().name().toLowerCase()))
                    Table.createTableFromEntity(this);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    public Entity(Object object) {
        if (object.getClass().getAnnotation(MyEntity.class) != null) {
            this.entityClass = object.getClass();
            entityObject = object;
            if (!Table.isTableExist(this.getModelAnnotation().name()))
                Table.createTableFromEntity(this);
        } else {
        }
    }


    public List<String> getFieldsNames() {
        List<String> nameFields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MyColumn.class)) {
                nameFields.add(field.getAnnotation(MyColumn.class).name());
            }
        }
        return nameFields;
    }

    public List<String> getFieldTypes() {
        List<String> typesFields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MyColumn.class)) {
                typesFields.add(field.getType().getSimpleName());
            }
        }
        return typesFields;
    }

    public Integer getPrimaryKeyValue() {
        Integer value = 0;
        for (Field column : getEntityClass().getDeclaredFields()) {
            if (column.isAnnotationPresent(MyId.class)) {
                try {
                    column.setAccessible(true);
                    value = ((Integer) column.get(getEntityObject()));
                } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public String getParsedFieldsLine() {
        StringBuilder parsedFields = new StringBuilder();

        for (Field parsedField : entityClass.getDeclaredFields()) {
            if (parsedField.isAnnotationPresent(MyColumn.class)) {
                final String COLUMN_NAME = parsedField.getAnnotation(MyColumn.class).name();
                try {
                    parsedFields.append(COLUMN_NAME.toLowerCase() + ", ");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return parsedFields.toString().trim().substring(0, parsedFields.toString().length() - 2); // return with delete
    }

    public String getParsedValuesLine() {
        StringBuilder preparedValues = new StringBuilder();
        for (Field parsedField : entityClass.getDeclaredFields()) {
            if (parsedField.isAnnotationPresent(MyColumn.class)) {
                final String COLUMN_NAME = parsedField.getAnnotation(MyColumn.class).name();
                try {
                    parsedField.setAccessible(true);
                    Object fieldValue = (Object) parsedField.get(entityObject); /* getting value that we need to push */
                    preparedValues.append("'" + fieldValue + "'" + ", ");
                } catch (IllegalArgumentException | IllegalAccessException | ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
        return preparedValues.toString().trim().substring(0, preparedValues.toString().length() - 2); // return with
    }

    public MyEntity getModelAnnotation() {
        return entityClass.getAnnotation(MyEntity.class);
    }


    public Class getEntityClass() {
        return entityClass;
    }

    public Object getEntityObject() {
        return entityObject;
    }

    public String tableName() {
        return getModelAnnotation().name();
    }

    public String primaryKey() {
        return getModelAnnotation().primaryK();
    }

    public Class<? extends Annotation> annotationType() {
        return MyEntity.class;
    }

    public QueryBuilder column(String fieldName) {
        String columnName = "";
        for (Field parsedField : entityClass.getDeclaredFields()) {
            if (parsedField.getName().equals(fieldName)) {
                columnName = getAnnotationAttribute(parsedField);
            } else {

            }
        }
        return new QueryBuilder(columnName);
    }

    private String getAnnotationAttribute(Field parsedField) {
        String columnName = "";
        if (parsedField.isAnnotationPresent(MyId.class)) {
            columnName = this.primaryKey();
        } else {
            columnName = parsedField.getAnnotation(MyColumn.class).name();
        }
        return columnName;
    }
}
