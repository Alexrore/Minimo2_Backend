package edu.upc.dsa.dao;

import java.sql.SQLException;
import java.util.List;

public interface Session<E> {
    void save(Object entity) throws SQLException;             // INSERT
    void close();
    Object get(Class theClass, int id);   // SELECT * FROM ... WHERE id = ?
    void update(Object object);           // UPDATE
    void delete(Object object);           // DELETE
    List<Object> findAll(Class theClass); // SELECT * FROM ...
    // Puedes añadir más: findAllByField, etc.
}