package edu.upc.dsa.dao;

import edu.upc.dsa.util.DBConnection;
import edu.upc.dsa.util.ObjectHelper;
import edu.upc.dsa.util.QueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl() {
        this.conn = DBConnection.getConnection();
    }

    @Override
    public void save(Object entity) throws SQLException { // 1. Añade throws
        String insertQuery = QueryHelper.createInsertQuery(entity);

        // Eliminamos el try-catch para que el error suba
        PreparedStatement pstm = conn.prepareStatement(insertQuery);
        int i = 1;
        for (String field : ObjectHelper.getFields(entity)) {
            if (field.equals("id")) continue;
            Object value = ObjectHelper.getter(entity, field);
            pstm.setObject(i++, value);
        }

        // Usamos execute() o executeUpdate() para INSERTs
        pstm.execute();

        // Si llega aquí es que NO hubo error
        System.out.println("Objeto guardado correctamente: " + entity.getClass().getSimpleName());
    }

    @Override
    public void close() {
        // En un entorno simple, a veces no cerramos la conexión si es compartida,
        // pero aquí podríamos cerrar recursos si fuera necesario.
    }

    @Override
    public Object get(Class theClass, int id) {
        try {
            String selectQuery = QueryHelper.createSelectQuery(theClass, id);
            PreparedStatement pstm = conn.prepareStatement(selectQuery);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Object obj = theClass.newInstance(); // Crea instancia vacía
                ResultSetMetaData rsmd = rs.getMetaData();

                // Recorremos columnas de la DB y llenamos el objeto
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i); // ej: "nombre"
                    Object value = rs.getObject(i);            // ej: "Guillermo"

                    // Solo llamamos al setter si el valor no es nulo
                    if (value != null) {
                        ObjectHelper.setter(obj, columnName, value);
                    }
                }
                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Object entity) {
        String updateQuery = QueryHelper.createUpdateQuery(entity);
        try {
            PreparedStatement pstm = conn.prepareStatement(updateQuery);
            int i = 1;
            // Params del SET
            for (String field : ObjectHelper.getFields(entity)) {
                if (field.equals("id")) continue;
                Object value = ObjectHelper.getter(entity, field);
                pstm.setObject(i++, value);
            }
            // Param del WHERE
            Object idValue = ObjectHelper.getter(entity, "id");
            pstm.setObject(i, idValue);

            pstm.executeUpdate();
            System.out.println("Objeto actualizado: " + entity.getClass().getSimpleName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Object entity) {
        String deleteQuery = QueryHelper.createDeleteQuery(entity);
        try {
            PreparedStatement pstm = conn.prepareStatement(deleteQuery);
            Object idValue = ObjectHelper.getter(entity, "id");
            pstm.setObject(1, idValue);
            pstm.executeUpdate();
            System.out.println("Objeto eliminado: " + entity.getClass().getSimpleName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Object> findAll(Class theClass) {
        List<Object> resultList = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM " + theClass.getSimpleName(); // ej: SELECT * FROM User

        try {
            PreparedStatement pstm = conn.prepareStatement(selectAllQuery);
            ResultSet rs = pstm.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                Object obj = theClass.newInstance(); // Instancia vacía
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String column = rsmd.getColumnName(i);
                    Object value = rs.getObject(i);
                    if (value != null) {
                        ObjectHelper.setter(obj, column, value);
                    }
                }
                resultList.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}