package edu.upc.dsa;

import edu.upc.dsa.modelos.User;
import java.util.*;
import edu.upc.dsa.dao.*;
import org.apache.log4j.Logger;

public class UserManagerImpl implements UserManager {
    private static UserManagerImpl instance;
    private List<User> usuarios;
    final static Logger logger = Logger.getLogger(UserManagerImpl.class);

    private UserManagerImpl() {
        usuarios = new ArrayList<>();
    }

    public static UserManagerImpl getInstance() {
        if (instance == null) instance = new UserManagerImpl();
        return instance;
    }

    @Override
    public User registrarUsuario(String nombre, String email, String password) {
        /*for (User u : usuarios) {
            if (u.getEmail().equals(email))
                return null;
        }

        User nuevo = new User(UUID.randomUUID().toString(), nombre, email, password);
        nuevo.setEmailVerificado(false);
        usuarios.add(nuevo);
        return nuevo;*/
        Session session = null;
        try {
            session = FactorySession.openSession();

            // 1. Crear el objeto Java
            User u = new User(UUID.randomUUID().toString(), nombre, email, password);

            // 2. Guardar en DB
            session.save(u);

            logger.info("Usuario registrado en DB: " + email);
            return u;

        } catch (Exception e) {
            // Probablemente error por email duplicado (Constraint Violation)
            logger.error("Error al registrar usuario: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close(); // IMPORTANTE: Cerrar sesión
        }
    }

    @Override
    public User loginUsuario(String email, String password) {
        // Buscamos al usuario por email
       /* User u = this.getUsuario(email);

        if (u != null) {
            // Si el usuario existe, se comprueba la contraseña
            if (u.getPassword().equals(password)) {
                return u; //Login correcto
            } else {
                return null; //Contraseña incorrecta
            }
        }
        return null; // Usuario no encontrado*/
        Session session = null;
        try {
            session = FactorySession.openSession();

            // Como no tenemos 'findByEmail', traemos todos y filtramos
            // (No es lo más eficiente pero es lo estándar en este nivel de ORM)
            List<Object> usersList = session.findAll(User.class);

            for (Object obj : usersList) {
                User u = (User) obj;
                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                    logger.info("Login exitoso: " + email);
                    return u;
                }
            }
        } catch (Exception e) {
            logger.error("Error en login: " + e.getMessage());
        } finally {
            if (session != null) session.close();
        }

        logger.warn("Usuario o password incorrectos: " + email);
        return null;
    }

    @Override
    public User getUsuario(String email) {
        /*for (User u : usuarios) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;*/
        Session session = null;
        try {
            session = FactorySession.openSession();
            List<Object> usersList = session.findAll(User.class);

            for (Object obj : usersList) {
                User u = (User) obj;
                // Comparamos el email del usuario de la DB con el que buscamos
                if (u.getEmail().equals(email)) {
                    return u; // ¡Encontrado!
                }
            }

            // Si termina el bucle y no lo encuentra
            return null;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return null;
    }

    @Override
    public List<User> getUsuarios() {
        return usuarios;
    }

    public boolean enviarCodigoVerificacion(String email) {
        User user = this.getUsuario(email);

        if (user == null) {
            return false;
        }

        Random random = new Random();
        String codigo = String.format("%06d", random.nextInt(999999));

        user.setCodigoVerificacion(codigo);


        System.out.println("═══════════════════════════════════════");
        System.out.println("📧 CÓDIGO DE VERIFICACIÓN");
        System.out.println("Email: " + email);
        System.out.println("Código: " + codigo);
        System.out.println("═══════════════════════════════════════");

        return true;
    }
    public boolean verificarCodigo(String email, String codigo) {
        User user = this.getUsuario(email);

        if (user == null) {
            return false;
        }

        if (codigo.equals(user.getCodigoVerificacion())) {
            user.setEmailVerificado(true);
            user.setCodigoVerificacion(null);
            System.out.println("Email verificado: " + email);
            return true;
        }

        System.out.println("Código incorrecto para: " + email);
        return false;
    }
}
