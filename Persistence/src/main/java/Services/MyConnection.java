package Services;


import Helpers.Settings;

import java.sql.*;

public class MyConnection implements AutoCloseable {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public MyConnection() {
        setConnection();
    }

    private void setConnection(){
        Settings settings = new Settings();
        try {
            this.connection = DriverManager.getConnection(settings.getValue("url"),
                    settings.getValue("user"), settings.getValue("password"));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
