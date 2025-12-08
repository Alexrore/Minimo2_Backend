package edu.upc.dsa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;

    // Datos de tu base de datos local
    private static final String USER = "root"; // Tu usuario de DB
    private static final String PASSWORD = "root"; // Tu contraseña de DB
    private static final String URL = "jdbc:mariadb://localhost:3306/void_gate";

    public DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Cargar el driver explícitamente suele ser necesario en proyectos web antiguos
                Class.forName("org.mariadb.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión a base de datos 'void_gate' establecida.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}