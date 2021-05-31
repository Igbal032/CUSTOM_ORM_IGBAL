package Helpers;

import Services.MyConnection;
import Services.SQLBuilder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Table {

    public static boolean createTableFromEntity(Entity entity) {

        boolean flag = false;

        if (!isTableExist(entity.tableName())) {
            try (Statement statement = new MyConnection().getConnection().createStatement()) {
                statement.executeUpdate(SQLBuilder.buildCreateTableRequest(entity));
                flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean isTableExist(String tableName) {
        boolean flag = false;

        try {
            DatabaseMetaData metaData = new MyConnection().getConnection().getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            if (isResultContainsTableName(resultSet, tableName)) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean deleteEntityTable(String tableName) {

        boolean flag = false;
        if (isTableExist(tableName)) {
            final String QUERY_DELETE_TABLE = "DROP TABLE " + tableName + " RESTRICT ;";

            try (final PreparedStatement statement = getConnection().prepareStatement(QUERY_DELETE_TABLE)) {
                statement.executeUpdate();
                flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;

    }

    public static List<Entity> readAllDataFromTable(Entity entity) {
        List<Entity> objects = new ArrayList<>();
        return objects;
    }

    public static Entity getEntityFromFieldWithCollection(Field field) {
        Class dependentClassName = null;
        try {
            String fullDesiredFieldName = field.getGenericType().toString();
            String genericClassNameFormList = fullDesiredFieldName.substring(fullDesiredFieldName.indexOf("<") + 1, fullDesiredFieldName.indexOf(">"));
            dependentClassName = Class.forName(genericClassNameFormList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Entity(dependentClassName);
    }

    private static boolean isResultContainsTableName(ResultSet resultSet, String tableName) throws SQLException {
        final byte TABLE_NAME_COLUMN_INDEX = 3;

        while (resultSet.next()) {
            if (resultSet.getString(TABLE_NAME_COLUMN_INDEX).equals(tableName)) {
                return true;
            }
        }
        return false;
    }


    private static Connection getConnection() throws SQLException {
        return new MyConnection().getConnection();
    }
}
