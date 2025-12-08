package edu.upc.dsa.util;

public class QueryHelper {

    /**
     * Crea una consulta INSERT dinámica:
     * INSERT INTO User (nombre, email, password) VALUES (?, ?, ?)
     */
    public static String createInsertQuery(Object entity) {
        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" (");

        String[] fields = ObjectHelper.getFields(entity);

        // 1. Añadir nombres de columnas (saltando el ID)
        boolean first = true;
        for (String field : fields) {
            if (field.equals("id")) continue; // El ID es autoincremental, no se inserta

            if (!first) sb.append(", ");
            sb.append(field);
            first = false;
        }

        sb.append(") VALUES (");

        // 2. Añadir interrogantes ? para los valores
        first = true;
        for (String field : fields) {
            if (field.equals("id")) continue;

            if (!first) sb.append(", ");
            sb.append("?");
            first = false;
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * Crea una consulta SELECT simple:
     * SELECT * FROM User WHERE id = ?
     */
    public static String createSelectQuery(Class theClass, int id) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(theClass.getSimpleName());
        sb.append(" WHERE id = ?");
        return sb.toString();
    }

    /**
     * Crea una consulta UPDATE dinámica:
     * UPDATE User SET nombre = ?, email = ? WHERE id = ?
     */
    public static String createUpdateQuery(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(entity.getClass().getSimpleName()).append(" SET ");

        String[] fields = ObjectHelper.getFields(entity);

        // Añadir campos a actualizar (saltando el ID en el SET)
        boolean first = true;
        for (String field : fields) {
            if (field.equals("id")) continue; // No actualizamos el ID, es la clave

            if (!first) sb.append(", ");
            sb.append(field).append(" = ?");
            first = false;
        }

        // El ID se usa solo en la cláusula WHERE
        sb.append(" WHERE id = ?");

        return sb.toString();
    }

    /**
     * Crea una consulta DELETE:
     * DELETE FROM User WHERE id = ?
     */
    public static String createDeleteQuery(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE id = ?");
        return sb.toString();
    }
}