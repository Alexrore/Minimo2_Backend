package edu.upc.dsa.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectHelper {

    public static String[] getFields(Object entity) {
        Class c = entity.getClass();
        Field[] fields = c.getDeclaredFields();
        String[] sFields = new String[fields.length];
        int i = 0;
        for (Field f : fields) sFields[i++] = f.getName();
        return sFields;
    }

    public static void setter(Object object, String property, Object value) {
        // Construimos el nombre del método: "setId", "setNombre", etc.
        String methodName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);

        try {
            // INTENTO 1: Búsqueda directa (coincidencia exacta de tipos)
            // Ej: Si el valor es String y el setter espera String
            Method method = object.getClass().getMethod(methodName, value.getClass());
            method.invoke(object, value);
        } catch (NoSuchMethodException e) {
            // INTENTO 2: Falló la búsqueda exacta. Probamos tipos primitivos comunes.
            try {
                // Caso especial: El valor es un Integer (Objeto), pero el método espera int (primitivo)
                if (value instanceof Integer) {
                    Method method = object.getClass().getMethod(methodName, int.class);
                    method.invoke(object, value);
                }
                // Caso especial: El valor es Boolean (Objeto), pero el método espera boolean (primitivo)
                else if (value instanceof Boolean) {
                    Method method = object.getClass().getMethod(methodName, boolean.class);
                    method.invoke(object, value);
                }
                // Caso especial: A veces la DB devuelve Long para IDs, pero en Java usamos int
                else if (value instanceof Long) {
                    Method method = object.getClass().getMethod(methodName, int.class);
                    method.invoke(object, ((Long) value).intValue());
                }
                else {
                    // Si llegamos aquí, realmente el método no existe o el tipo es incompatible
                    System.err.println("No se encontró método para: " + methodName + " con tipo " + value.getClass());
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getter(Object object, String property) {
        // Probamos primero con "get" (ej: getNombre)
        String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        try {
            Method method = object.getClass().getMethod(methodName);
            return method.invoke(object);
        } catch (NoSuchMethodException e) {
            // Si no existe el "get", probamos con "is" (para booleanos, ej: isEmailVerificado)
            String isMethodName = "is" + property.substring(0, 1).toUpperCase() + property.substring(1);
            try {
                Method method = object.getClass().getMethod(isMethodName);
                return method.invoke(object);
            } catch (Exception ex) {
                // No existe ni get ni is
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}