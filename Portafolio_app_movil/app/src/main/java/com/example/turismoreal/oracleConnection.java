package com.example.turismoreal;

import android.widget.Toast;
import android.os.StrictMode;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class oracleConnection {
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.6:1522:XE";
    private static final String USERNAME = "turismo_real_dev";
    private static final String PASSWORD = "123";
    private static Connection connection;

    public static Connection getConn() {
        try
        {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection != null)
            {
                return connection;
            } else
            {
                return null;
            }
        } catch (ClassNotFoundException | SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }

    }


}
