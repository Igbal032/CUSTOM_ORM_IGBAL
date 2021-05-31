package Services;

import Annotation.MyColumn;
import Helpers.Entity;
import Helpers.Types;

import java.lang.reflect.Field;

public final class SQLBuilder {
    private final static String CREATETABLE = "CREATE TABLE ";
    private final static String PRIMARYKEY = "PRIMARY KEY";
    private final static String _ID = "_id";
    private final static String ID = " (id)";
    private final static String ALTERTABLE = "ALTER TABLE ";
    private final static String ADDCONSTRAINT = " ADD CONSTRAINT ";
    private final static String FOREIGNKEY = " FOREIGN KEY";
    private final static String INTEGER = " INTEGER, ";
    private final static String LEFTBRACKET = " (";
    private final static String RIGHTBRACKET = ")";
    private final static String SERIAL = " serial, ";
    private final static String INSERTINTO = "INSERT INTO ";


    public static String buildCreateTableRequest(Entity entity) {

        String tableName = entity.tableName();
        String primaryKey = entity.primaryKey();

        StringBuilder SQLRequest = new StringBuilder(CREATETABLE + tableName
                + LEFTBRACKET + primaryKey + SERIAL);
        SQLRequest.append(buildEntityFieldLine(entity));
        SQLRequest.append(PRIMARYKEY).append(LEFTBRACKET).append(primaryKey).append(RIGHTBRACKET);
        SQLRequest.append(RIGHTBRACKET);
        return SQLRequest.toString();
    }


    private static String buildEntityFieldLine(Entity entity) {
        StringBuilder fieldLine = new StringBuilder();

        for (int i = 0; i < entity.getFieldsNames().size(); i++) {
            fieldLine.append(entity.getFieldsNames().get(i));
            fieldLine.append(" ").append(Types.getInstance().getTypes().get(entity.getFieldTypes().get(i))).append(", ");
        }
        return fieldLine.toString();
    }


    public static String buildFieldValuesLine(Entity entity) {
        StringBuilder valuesLine = new StringBuilder();
        String columnName;
        Object fieldValue;

        for (Field parsedField : entity.getEntityClass().getDeclaredFields()) {
            if (parsedField.isAnnotationPresent(MyColumn.class)) {
                columnName = parsedField.getAnnotation(MyColumn.class).name();

                try {
                    parsedField.setAccessible(true);
                    fieldValue = ((Object) parsedField.get(entity.getEntityObject()));
                    valuesLine.append(columnName + " = '" + fieldValue + "'" + ", ");
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return valuesLine.toString().substring(0, valuesLine.length() - 2);
    }
}
