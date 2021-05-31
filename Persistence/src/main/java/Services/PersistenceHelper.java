package Services;

import Annotation.MyColumn;
import Annotation.MyEntity;
import Models.Column;
import Models.Table;
import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
@Data
public class PersistenceHelper {


    public void getColumns(){


    }
    public static  List<Column> columnNames;
    private String name;

    public PersistenceHelper(Class cl) throws Exception {
        this.columnNames = getColumnName(cl);
        this.name = getTableName(cl);
    }

    static Object getFieldValue(Object object, String fieldName) {
        Class<?> cls = object.getClass();
        try {
            Field name = FieldUtils.getField(cls, fieldName, true);
            return FieldUtils.readField(name, object, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTableName(Class cls) throws Exception {
        MyEntity entity = (MyEntity) cls.getAnnotation(MyEntity.class);
        if (entity != null) {
            return entity.name();
        }
        throw new Exception("This class is not exportable class");
    }

    private List<Column> getColumnName(Class<?> cls) throws Exception {
        List<Column> cellNames = new ArrayList<>();
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(cls, MyColumn.class);
        if (fields.size() == 0){
           throw new Exception("There is no exportable fields!!");
        }
        for (Field field : fields) {
            MyColumn exportFiled = field.getAnnotation(MyColumn.class);
            columnNames.add(new Column(exportFiled.name(),field.getName()));
        }
        return cellNames;
    }
}
