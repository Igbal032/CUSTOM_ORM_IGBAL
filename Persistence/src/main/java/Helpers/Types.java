package Helpers;

import java.util.HashMap;
import java.util.Map;

public class Types {

    private static Types instance;
    private static Map<String, String> dataTypes;
    private Types(){}

    public static Types getInstance() {
        if(instance == null){
            instance = new Types();

        }
        return instance;
    }
    private static void addDefaultDataTypes(){

        dataTypes.put("Long", "BIGINT");
        dataTypes.put("long", "BIGINT");
        dataTypes.put("Float", "DOUBLE PRECISION");
        dataTypes.put("char", "CHAR(5)");
        dataTypes.put("String", "VARCHAR(45)");
        dataTypes.put("Date", "DATE");
        dataTypes.put("boolean", "BOOLEAN");
        dataTypes.put("float", "DOUBLE PRECISION");
        dataTypes.put("Double", "DOUBLE PRECISION");
        dataTypes.put("double", "DOUBLE PRECISION");
        dataTypes.put("Character", "CHAR(5)");
        dataTypes.put("Byte", "SMALLINT");
        dataTypes.put("byte", "SMALLINT");
        dataTypes.put("Short", "SMALLINT");
        dataTypes.put("short", "SMALLINT");
        dataTypes.put("Integer", "INTEGER");
        dataTypes.put("int", "INTEGER");

    }
    public Map<String, String> getTypes()
    {
        if(dataTypes == null || dataTypes.isEmpty()){
            dataTypes = new HashMap<>();
            addDefaultDataTypes();
        }
        return dataTypes;
    }



}
